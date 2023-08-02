package com.menesdurak.e_ticaret_uygulamasi.presentation.cart.payment

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.menesdurak.e_ticaret_uygulamasi.R
import com.menesdurak.e_ticaret_uygulamasi.common.Resource
import com.menesdurak.e_ticaret_uygulamasi.common.addCurrencySign
import com.menesdurak.e_ticaret_uygulamasi.common.getFirstTwoChar
import com.menesdurak.e_ticaret_uygulamasi.common.getLastTwoChar
import com.menesdurak.e_ticaret_uygulamasi.common.round
import com.menesdurak.e_ticaret_uygulamasi.data.local.entity.CreditCardInfo
import com.menesdurak.e_ticaret_uygulamasi.data.mapper.CartProductListToBoughtProductListMapper
import com.menesdurak.e_ticaret_uygulamasi.data.remote.dto.UserInfo
import com.menesdurak.e_ticaret_uygulamasi.databinding.FragmentPaymentBinding
import com.menesdurak.e_ticaret_uygulamasi.presentation.cart.CartViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaymentFragment : Fragment() {
    private var _binding: FragmentPaymentBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    private lateinit var databaseReference: DatabaseReference

    private lateinit var userInfo: UserInfo

    private val checkOutAdapter: CheckOutAdapter by lazy { CheckOutAdapter() }

    private val creditCardAdapter: CreditCardAdapter by lazy {
        CreditCardAdapter(
            ::onCardClicked,
            ::onCardLongClicked,
            ::onCheckboxClicked
        )
    }

    private val cartViewModel: CartViewModel by viewModels()

    private val creditCardViewModel: CreditCardViewModel by viewModels()

    private var totalPrice: Double = 0.0

    private var isNewAddress = false

    private var userAddress = ""

    private var isInformationsOk = false

    private var bool = false

    private var activeCreditCardNumber = ""

    private val rotateLeft: Animation by lazy {
        AnimationUtils.loadAnimation(
            requireContext(),
            R.anim.rotate_left_anim
        )
    }
    private val rotateRight: Animation by lazy {
        AnimationUtils.loadAnimation(
            requireContext(),
            R.anim.rotate_right_anim
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase Auth
        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentPaymentBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        databaseReference = Firebase.database.reference

        databaseReference.child(auth.currentUser?.uid!!).child("userInfo").get()
            .addOnSuccessListener {
                userInfo = UserInfo("", "", "", "")
                userInfo.name = it.getValue<UserInfo>()!!.name
                userInfo.surName = it.getValue<UserInfo>()!!.surName
                userInfo.address = it.getValue<UserInfo>()!!.address.toString()
                userAddress = userInfo.address.toString()
                userInfo.phone = it.getValue<UserInfo>()!!.phone

                binding.etUserAddressInfo.setText(userAddress)
            }

        binding.linlayCheckOutContainer.visibility = View.VISIBLE

        binding.etUserAddressInfo.isClickable = false
        binding.etUserAddressInfo.isFocusable = false
        binding.etUserAddressInfo.isFocusableInTouchMode = false

        binding.rvCartItems.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = checkOutAdapter
        }

        cartViewModel.getAllCheckedCartProducts()
        cartViewModel.checkedAllCartProductsList.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    checkOutAdapter.updateList(it.data)

                    it.data.forEach {
                        totalPrice += it.price.toDouble() * it.amount
                    }

                    binding.tvTotalPrice.text =
                        (totalPrice round 2).toString().toDouble().addCurrencySign()
                }

                is Resource.Error -> {
                    Log.e("error", "Error in Payment Fragment")
                }

                Resource.Loading -> {
                }
            }
        }

        creditCardViewModel.getAllCreditCards()
        binding.rvCreditCards.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = creditCardAdapter
        }
        creditCardViewModel.creditCardList.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    creditCardAdapter.updateList(it.data)
                }

                is Resource.Error -> {
                    Log.e("error", "Error in Payment Fragment")
                }

                Resource.Loading -> {

                }
            }
        }

        binding.btnAddNewCreditCard.setOnClickListener {
            bool = !bool
            if (bool) {
                binding.constraintAddNewCard.visibility = View.VISIBLE
            } else {
                binding.constraintAddNewCard.visibility = View.GONE
            }
        }

        binding.btnSaveCard.setOnClickListener {
            if (binding.etCardHolder.text.isNotBlank() &&
                binding.etCardNumber.text.isNotBlank() &&
                binding.etSecurityCode.text.isNotBlank() &&
                binding.etExpireDate.text.isNotBlank()
            ) {
                val creditCard = CreditCardInfo(
                    number = binding.etCardNumber.text.toString(),
                    holderName = binding.etCardHolder.text.toString(),
                    cvc = binding.etSecurityCode.text.toString(),
                    expireMonth = binding.etExpireDate.text.toString().getFirstTwoChar(),
                    expireYear = binding.etExpireDate.text.toString().getLastTwoChar()
                )
                creditCardViewModel.addCreditCard(creditCard)
                creditCardAdapter.addNewCard(creditCard)
                bool = false
                binding.constraintAddNewCard.visibility = View.GONE
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.please_fill_your_credit_card_information),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.btnBuy.setOnClickListener {
            if (binding.etUserAddressInfo.text.isNotBlank()) {
                isInformationsOk = true
            } else {
                isInformationsOk = false
                Toast.makeText(
                    requireContext(),
                    getString(R.string.please_fill_your_address_information),
                    Toast.LENGTH_SHORT
                ).show()
            }

            if (isInformationsOk) {
                cartViewModel.checkedAllCartProductsList.observe(viewLifecycleOwner) {
                    when (it) {
                        is Resource.Success -> {
                            val ordersReference =
                                databaseReference.child(auth.currentUser?.uid!!).child("orders")
                            val newOrderReference = ordersReference.push()
                            val newOrderKey = newOrderReference.key
                            val orderList =
                                CartProductListToBoughtProductListMapper(
                                    binding.etUserAddressInfo.text.toString(),
                                    activeCreditCardNumber
                                ).map(it.data)

                            for (index in orderList.indices) {
                                val newOrderInsideReference =
                                    ordersReference.child(newOrderKey!!).push()
                                val newOrderInsideKey = newOrderInsideReference.key
                                ordersReference.child(newOrderKey).child(newOrderInsideKey!!)
                                    .setValue(orderList[index])
                            }
                            cartViewModel.deleteAllCheckedCartProducts()
                            totalPrice = 0.0
                            binding.tvTotalPrice.text =
                                (totalPrice round 2).toString().toDouble().addCurrencySign()
                            binding.linlayCheckOutContainer.visibility = View.GONE
                            findNavController().popBackStack()
                        }

                        is Resource.Error -> {
                            Log.e("error", "Error in Payment Fragment")
                        }

                        Resource.Loading -> {

                        }
                    }
                }
            }
        }

        binding.ivSwapAddress.setOnClickListener {
            isNewAddress = !isNewAddress
            setAnimation(isNewAddress)
            if (isNewAddress) {
                binding.tvUserAddress.textSize = 18f
                binding.tvUserAddress.typeface = Typeface.DEFAULT
                binding.etUserAddressInfo.isClickable = true
                binding.etUserAddressInfo.isFocusable = true
                binding.etUserAddressInfo.isFocusableInTouchMode = true
                binding.tvUserAddressNew.textSize = 22f
                binding.tvUserAddressNew.typeface = Typeface.DEFAULT_BOLD
                binding.btnSaveAdress.visibility = View.VISIBLE
            } else {
                binding.tvUserAddress.textSize = 22f
                binding.tvUserAddress.typeface = Typeface.DEFAULT_BOLD
                binding.etUserAddressInfo.isClickable = false
                binding.etUserAddressInfo.isFocusable = false
                binding.etUserAddressInfo.isFocusableInTouchMode = false
                binding.tvUserAddressNew.textSize = 18f
                binding.tvUserAddressNew.typeface = Typeface.DEFAULT
                binding.btnSaveAdress.visibility = View.GONE
            }
        }

        binding.btnSaveAdress.setOnClickListener {
            userInfo.address = binding.etUserAddressInfo.text.toString()
            databaseReference.child(auth.currentUser?.uid!!).child("userInfo").setValue(userInfo)
            setAnimation(!isNewAddress)
            Toast.makeText(
                requireContext(),
                getString(R.string.new_address_saved_to_your_user_information),
                Toast.LENGTH_SHORT
            ).show()
            binding.tvUserAddress.textSize = 22f
            binding.tvUserAddress.typeface = Typeface.DEFAULT_BOLD
            binding.etUserAddressInfo.isClickable = false
            binding.etUserAddressInfo.isFocusable = false
            binding.etUserAddressInfo.isFocusableInTouchMode = false
            binding.tvUserAddressNew.textSize = 18f
            binding.tvUserAddressNew.typeface = Typeface.DEFAULT
            binding.btnSaveAdress.visibility = View.GONE
        }

        //TextWatcher for credit card number is taken from:
        //Hannu Leinonen
        //https://gist.github.com/hleinone/5b445e5475ca9f8a3bdc6a44998f4edd
        binding.etCardNumber.addTextChangedListener(object : TextWatcher {
            private var current = ""

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable) {
                if (p0.toString() != current) {
                    val userInput = p0.toString().replace(PaymentFragment.nonDigits, "")
                    if (userInput.length <= 16) {
                        current = userInput.chunked(4).joinToString(" ")
                        p0.filters = arrayOfNulls<InputFilter>(0)
                    }
                    p0.replace(0, p0.length, current, 0, current.length)
                }
            }
        })

        binding.etExpireDate.addTextChangedListener(ExpiryDateTextWatcher())

    }

    //Set animation of address swapper image
    private fun setAnimation(clicked: Boolean) {
        if (!clicked) {
            binding.ivSwapAddress.startAnimation(rotateLeft)
        } else {
            binding.ivSwapAddress.startAnimation(rotateRight)
        }
    }

    companion object {
        private val nonDigits = Regex("[^\\d]")
    }

    private fun onCardClicked(position: Int, creditCardInfo: CreditCardInfo) {
        activeCreditCardNumber = creditCardAdapter.activeItem(position)
    }

    private fun onCheckboxClicked(position: Int, creditCardInfo: CreditCardInfo) {
        activeCreditCardNumber = creditCardAdapter.activeItem(position)
    }

    private fun onCardLongClicked(position: Int, creditCard: CreditCardInfo) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.delete_card))
        builder.setMessage(getString(R.string.do_you_want_to_delete_this_credit_card))
            .setPositiveButton(
                getString(R.string.yes),
                DialogInterface.OnClickListener { dialog, which ->
                    creditCardViewModel.deleteCreditCard(creditCard.id)
                    creditCardAdapter.deleteCard(position, creditCard)
                })
            .setNegativeButton(
                getString(R.string.no),
                DialogInterface.OnClickListener { dialog, which -> })
        builder.create()
        builder.show()
    }

    private inner class ExpiryDateTextWatcher : TextWatcher {
        private var lastInput = ""

        override fun afterTextChanged(editable: Editable?) {
            if (editable == null) return

            val input = editable.toString()
            if (input == lastInput || input.isEmpty()) return

            val cleanInput = input.replace(Regex("[^\\d/]"), "")
            val expiryDate = StringBuilder(cleanInput)

            if (cleanInput.length >= 3 && cleanInput[2] != '/') {
                expiryDate.insert(2, '/')
            }
            if (expiryDate.length > 5) {
                // Limit the input to MM/YY format
                expiryDate.delete(5, expiryDate.length)
            }

            lastInput = expiryDate.toString()
            editable.replace(0, editable.length, expiryDate.toString())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // Do nothing
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            // Do nothing
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}