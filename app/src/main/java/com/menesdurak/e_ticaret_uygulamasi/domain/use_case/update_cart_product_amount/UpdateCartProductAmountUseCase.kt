package com.menesdurak.e_ticaret_uygulamasi.domain.use_case.update_cart_product_amount

import com.menesdurak.e_ticaret_uygulamasi.common.Resource
import com.menesdurak.e_ticaret_uygulamasi.domain.repository.LocalRepository
import java.lang.Exception
import javax.inject.Inject

class UpdateCartProductAmountUseCase @Inject constructor(
    private val localRepository: LocalRepository,
) {

    suspend operator fun invoke(newAmount: Int, cartProductId: Int) {
        try {
            Resource.Success(
                localRepository.updateCartProductAmount(
                    newAmount,
                    cartProductId
                )
            )
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }
}