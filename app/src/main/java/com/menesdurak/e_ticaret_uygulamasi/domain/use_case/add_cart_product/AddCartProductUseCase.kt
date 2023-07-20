package com.menesdurak.e_ticaret_uygulamasi.domain.use_case.add_cart_product

import com.menesdurak.e_ticaret_uygulamasi.common.Resource
import com.menesdurak.e_ticaret_uygulamasi.data.local.entity.CartProduct
import com.menesdurak.e_ticaret_uygulamasi.domain.repository.LocalRepository
import java.io.IOException
import javax.inject.Inject

class AddCartProductUseCase @Inject constructor(
    private val localRepository: LocalRepository
) {

    suspend operator fun invoke(cartProduct: CartProduct) {
        try {
            Resource.Success(localRepository.addCartProduct(cartProduct))
        } catch (e: IOException) {
            Resource.Error(e)
        }
    }
}