package com.menesdurak.e_ticaret_uygulamasi.domain.use_case.delete_all_credit_cards

import com.menesdurak.e_ticaret_uygulamasi.common.Resource
import com.menesdurak.e_ticaret_uygulamasi.domain.repository.LocalRepository
import java.io.IOException
import javax.inject.Inject

class DeleteAllCreditCardsUseCase @Inject constructor(
    private val localRepository: LocalRepository
) {

    suspend operator fun invoke() {
        try {
            Resource.Success(localRepository.deleteAllCreditCards())
        } catch (e: IOException) {
            Resource.Error(e)
        }
    }
}