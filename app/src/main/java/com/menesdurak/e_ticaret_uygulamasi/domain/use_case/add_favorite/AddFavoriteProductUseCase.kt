package com.menesdurak.e_ticaret_uygulamasi.domain.use_case.add_favorite

import com.menesdurak.e_ticaret_uygulamasi.common.Resource
import com.menesdurak.e_ticaret_uygulamasi.data.local.entity.FavoriteProduct
import com.menesdurak.e_ticaret_uygulamasi.domain.repository.LocalRepository
import java.io.IOException
import javax.inject.Inject

class AddFavoriteProductUseCase @Inject constructor(
    private val localRepository: LocalRepository
) {

    suspend operator fun invoke(favoriteProduct: FavoriteProduct) {
        try {
            Resource.Success(localRepository.addFavoriteProduct(favoriteProduct))
        } catch (e: IOException) {
            Resource.Error(e)
        }
    }
}