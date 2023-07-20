package com.menesdurak.e_ticaret_uygulamasi.domain.use_case.update_cart_product_checked_status

import com.menesdurak.e_ticaret_uygulamasi.common.Resource
import com.menesdurak.e_ticaret_uygulamasi.domain.repository.LocalRepository
import java.lang.Exception
import javax.inject.Inject

class UpdateCartProductCheckedStatusUseCase @Inject constructor(
    private val localRepository: LocalRepository,
) {

    suspend operator fun invoke(isCheckedStatus: Boolean, cartProductId: Int) {
        try {
            Resource.Success(
                localRepository.updateCartProductCheckedStatus(
                    isCheckedStatus,
                    cartProductId
                )
            )
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }
}