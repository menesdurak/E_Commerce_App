package com.menesdurak.e_ticaret_uygulamasi.domain.use_case.update_credit_cart_active_status

import com.menesdurak.e_ticaret_uygulamasi.common.Resource
import com.menesdurak.e_ticaret_uygulamasi.domain.repository.LocalRepository
import java.lang.Exception
import javax.inject.Inject

class UpdateCreditCardActiveStatusUseCase @Inject constructor(
    private val localRepository: LocalRepository,
) {

    suspend operator fun invoke(isActive: Boolean, creditCardId: Short) {
        try {
            Resource.Success(
                localRepository.updateCreditCardActiveStatus(
                    isActive,
                    creditCardId
                )
            )
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }
}