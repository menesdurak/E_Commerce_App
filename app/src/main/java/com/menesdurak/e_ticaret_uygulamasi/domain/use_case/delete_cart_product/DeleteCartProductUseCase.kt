package com.menesdurak.e_ticaret_uygulamasi.domain.use_case.delete_cart_product

import com.menesdurak.e_ticaret_uygulamasi.common.Resource
import com.menesdurak.e_ticaret_uygulamasi.domain.repository.LocalRepository
import java.io.IOException
import javax.inject.Inject

class DeleteCartProductUseCase @Inject constructor(
    private val localRepository: LocalRepository
) {

    suspend operator fun invoke(cartProductId: Int) {
        try {
            Resource.Success(localRepository.deleteCartProductWithId(cartProductId))
        } catch (e: IOException) {
            Resource.Error(e)
        }
    }
}