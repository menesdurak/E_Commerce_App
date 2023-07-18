package com.menesdurak.e_ticaret_uygulamasi.domain.use_case.get_all_favorite_products

import com.menesdurak.e_ticaret_uygulamasi.common.Resource
import com.menesdurak.e_ticaret_uygulamasi.data.local.entity.FavoriteProduct
import com.menesdurak.e_ticaret_uygulamasi.domain.repository.LocalRepository
import java.io.IOException
import javax.inject.Inject

class GetAllFavoriteProductsUseCase @Inject constructor(
    private val localRepository: LocalRepository
) {

    suspend operator fun invoke() : Resource<List<FavoriteProduct>> {
        return try {
            Resource.Success(localRepository.getAllFavoriteProducts())
        } catch (e: IOException) {
            Resource.Error(e)
        }
    }
}