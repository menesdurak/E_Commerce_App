package com.menesdurak.e_ticaret_uygulamasi.presentation.cart.payment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.menesdurak.e_ticaret_uygulamasi.common.Resource
import com.menesdurak.e_ticaret_uygulamasi.data.local.entity.CartProduct
import com.menesdurak.e_ticaret_uygulamasi.data.local.entity.CreditCardInfo
import com.menesdurak.e_ticaret_uygulamasi.domain.use_case.add_credit_card.AddCreditCardUseCase
import com.menesdurak.e_ticaret_uygulamasi.domain.use_case.delete_all_credit_cards.DeleteAllCreditCardsUseCase
import com.menesdurak.e_ticaret_uygulamasi.domain.use_case.delete_credit_card.DeleteCreditCardUseCase
import com.menesdurak.e_ticaret_uygulamasi.domain.use_case.get_all_credit_cards.GetAllCreditCardsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreditCardViewModel @Inject constructor(
    private val addCreditCardUseCase: AddCreditCardUseCase,
    private val deleteCreditCardUseCase: DeleteCreditCardUseCase,
    private val deleteAllCreditCardsUseCase: DeleteAllCreditCardsUseCase,
    private val getAllCreditCardsUseCase: GetAllCreditCardsUseCase
) : ViewModel() {

    private val _creditCardList = MutableLiveData<Resource<List<CreditCardInfo>>>(Resource.Loading)
    val creditCardList: LiveData<Resource<List<CreditCardInfo>>> = _creditCardList

    fun getAllCreditCards()  {
        viewModelScope.launch {
            _creditCardList.value = Resource.Loading
            _creditCardList.value = getAllCreditCardsUseCase()!!
        }
    }

    fun addCreditCard(creditCard: CreditCardInfo) {
        viewModelScope.launch {
            addCreditCardUseCase(creditCard)
        }
    }

    fun deleteCreditCard(creditCardId: Short) {
        viewModelScope.launch {
            deleteCreditCardUseCase(creditCardId)
        }
    }

    fun deleteAllCreditCards() {
        viewModelScope.launch {
            deleteAllCreditCardsUseCase()
        }
    }

    fun updateActiveStatus(isActive: Boolean, creditCardId: Short) {
        viewModelScope.launch {
            updateActiveStatus(isActive, creditCardId)
        }
    }
}