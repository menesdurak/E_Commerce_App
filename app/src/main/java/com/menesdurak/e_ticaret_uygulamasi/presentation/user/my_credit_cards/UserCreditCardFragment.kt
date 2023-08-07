package com.menesdurak.e_ticaret_uygulamasi.presentation.user.my_credit_cards

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.menesdurak.e_ticaret_uygulamasi.R
import com.menesdurak.e_ticaret_uygulamasi.common.Resource
import com.menesdurak.e_ticaret_uygulamasi.common.getFirstTwoChar
import com.menesdurak.e_ticaret_uygulamasi.common.getLastTwoChar
import com.menesdurak.e_ticaret_uygulamasi.data.local.entity.CreditCardInfo
import com.menesdurak.e_ticaret_uygulamasi.databinding.FragmentUserCreditCardBinding
import com.menesdurak.e_ticaret_uygulamasi.presentation.cart.payment.CreditCardViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserCreditCardFragment : Fragment() {
    private var _binding: FragmentUserCreditCardBinding? = null
    private val binding get() = _binding!!

    private val creditCardAdapter: UserCreditCardAdapter by lazy {
        UserCreditCardAdapter(
            ::onCardClicked,
            ::onCardLongClicked,
            ::onCheckboxClicked
        )
    }

    private val creditCardViewModel: CreditCardViewModel by viewModels()

    private var bool = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentUserCreditCardBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvCreditCards.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = creditCardAdapter
        }

        creditCardViewModel.getAllCreditCards()

        creditCardViewModel.creditCardList.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    creditCardAdapter.updateList(it.data)
                }

                is Resource.Error -> {
                    Log.e("error", "Error in User Credit Card Fragment")
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

        //TextWatcher for credit card number is taken from:
        //Hannu Leinonen
        //https://gist.github.com/hleinone/5b445e5475ca9f8a3bdc6a44998f4edd
        binding.etCardNumber.addTextChangedListener(
            object : TextWatcher {
                private var current = ""

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(p0: Editable) {
                    if (p0.toString() != current) {
                        val userInput = p0.toString().replace(nonDigits, "")
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

    private fun onCardClicked(position: Int, creditCardInfo: CreditCardInfo) {

    }

    private fun onCheckboxClicked(position: Int, creditCardInfo: CreditCardInfo) {

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

    companion object {
        private val nonDigits = Regex("[^\\d]")
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