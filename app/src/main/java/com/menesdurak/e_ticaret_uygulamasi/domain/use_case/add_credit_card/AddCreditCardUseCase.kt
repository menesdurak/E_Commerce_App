package com.menesdurak.e_ticaret_uygulamasi.domain.use_case.add_credit_card

import com.menesdurak.e_ticaret_uygulamasi.common.Resource
import com.menesdurak.e_ticaret_uygulamasi.data.local.entity.CartProduct
import com.menesdurak.e_ticaret_uygulamasi.data.local.entity.CreditCardInfo
import com.menesdurak.e_ticaret_uygulamasi.domain.repository.LocalRepository
import java.io.IOException
import javax.inject.Inject

class AddCreditCardUseCase @Inject constructor(
    private val localRepository: LocalRepository
) {

    suspend operator fun invoke(creditCard: CreditCardInfo) {
        try {
            Resource.Success(localRepository.addCreditCard(creditCard))
        } catch (e: IOException) {
            Resource.Error(e)
        }
    }
}