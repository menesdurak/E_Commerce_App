package com.menesdurak.e_ticaret_uygulamasi.domain.use_case.get_all_cart_products

import com.menesdurak.e_ticaret_uygulamasi.common.Resource
import com.menesdurak.e_ticaret_uygulamasi.data.local.entity.CartProduct
import com.menesdurak.e_ticaret_uygulamasi.domain.repository.LocalRepository
import java.io.IOException
import javax.inject.Inject

class GetAllCartProductsUseCase @Inject constructor(
    private val localRepository: LocalRepository
) {

    suspend operator fun invoke() : Resource<List<CartProduct>> {
        return try {
            Resource.Success(localRepository.getAllCartProducts())
        } catch (e: IOException) {
            Resource.Error(e)
        }
    }
}