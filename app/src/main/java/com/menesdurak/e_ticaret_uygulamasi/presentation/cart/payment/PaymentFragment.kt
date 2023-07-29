package com.menesdurak.e_ticaret_uygulamasi.presentation.cart.payment

import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
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
import com.menesdurak.e_ticaret_uygulamasi.common.round
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

    private val cartViewModel: CartViewModel by viewModels()

    private var totalPrice: Double = 0.0

    private var isNewAddress = false

    private var userAddress = ""

    private var isInformationsOk = false

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

        binding.btnBuy.setOnClickListener {
            val creditCardNumber = binding.etCreditCardNumber.text
            if (creditCardNumber.isNotBlank() &&
                binding.etCreditCardCvc.text.isNotBlank() &&
                binding.etCreditCardExpiryMonth.text.isNotBlank() &&
                binding.etCreditCardExpiryYear.text.isNotBlank()
            ) {
                isInformationsOk = true
            } else {
                isInformationsOk = false
                Toast.makeText(
                    requireContext(),
                    getString(R.string.please_fill_your_credit_card_information),
                    Toast.LENGTH_SHORT
                ).show()
            }

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
                                    creditCardNumber.toString()
                                ).map(it.data)

                            for (index in orderList.indices) {
                                val newOrderInsideReference = ordersReference.child(newOrderKey!!).push()
                                val newOrderInsideKey = newOrderInsideReference.key
                                ordersReference.child(newOrderKey).child(newOrderInsideKey!!).setValue(orderList[index])
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

        //TextWatcher for creditcard number is taken from:
        //Hannu Leinonen
        //https://gist.github.com/hleinone/5b445e5475ca9f8a3bdc6a44998f4edd
        binding.etCreditCardNumber.addTextChangedListener(object : TextWatcher {
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

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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

}