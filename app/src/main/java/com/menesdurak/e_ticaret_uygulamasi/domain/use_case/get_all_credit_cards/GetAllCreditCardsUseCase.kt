package com.menesdurak.e_ticaret_uygulamasi.domain.use_case.get_all_credit_cards

import com.menesdurak.e_ticaret_uygulamasi.common.Resource
import com.menesdurak.e_ticaret_uygulamasi.data.local.entity.CreditCardInfo
import com.menesdurak.e_ticaret_uygulamasi.domain.repository.LocalRepository
import java.io.IOException
import javax.inject.Inject

class GetAllCreditCardsUseCase @Inject constructor(
    private val localRepository: LocalRepository
) {

    suspend operator fun invoke() : Resource<List<CreditCardInfo>> {
        return try {
            Resource.Success(localRepository.getAllCreditCards())
        } catch (e: IOException) {
            Resource.Error(e)
        }
    }
}