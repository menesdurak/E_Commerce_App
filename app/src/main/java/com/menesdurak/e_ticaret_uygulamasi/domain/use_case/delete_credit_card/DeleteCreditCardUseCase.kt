package com.menesdurak.e_ticaret_uygulamasi.domain.use_case.delete_credit_card

import com.menesdurak.e_ticaret_uygulamasi.common.Resource
import com.menesdurak.e_ticaret_uygulamasi.domain.repository.LocalRepository
import java.io.IOException
import javax.inject.Inject

class DeleteCreditCardUseCase @Inject constructor(
    private val localRepository: LocalRepository
) {

    suspend operator fun invoke(creditCardId: Short) {
        try {
            Resource.Success(localRepository.deleteCreditCardWithId(creditCardId))
        } catch (e: IOException) {
            Resource.Error(e)
        }
    }
}