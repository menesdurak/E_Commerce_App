package com.menesdurak.e_ticaret_uygulamasi.domain.use_case.get_all_favorite_products_id

import com.menesdurak.e_ticaret_uygulamasi.common.Resource
import com.menesdurak.e_ticaret_uygulamasi.domain.repository.LocalRepository
import java.io.IOException
import javax.inject.Inject

class GetAllFavoriteProductsIdUseCase @Inject constructor(
    private val localRepository: LocalRepository
) {

    suspend operator fun invoke() : Resource<List<Int>> {
        return try {
            Resource.Success(localRepository.getAllFavoriteProducts().map { favoriteProduct ->
                favoriteProduct.id
            })
        } catch (e: IOException) {
            Resource.Error(e)
        }
    }
}