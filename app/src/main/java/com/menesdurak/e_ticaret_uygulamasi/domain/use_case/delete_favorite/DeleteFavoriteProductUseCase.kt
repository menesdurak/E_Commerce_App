package com.menesdurak.e_ticaret_uygulamasi.domain.use_case.delete_favorite

import com.menesdurak.e_ticaret_uygulamasi.common.Resource
import com.menesdurak.e_ticaret_uygulamasi.domain.repository.LocalRepository
import java.io.IOException
import javax.inject.Inject

class DeleteFavoriteProductUseCase @Inject constructor(
    private val localRepository: LocalRepository
) {

    suspend operator fun invoke(favoriteProductId: Int){
        try {
            Resource.Success(localRepository.deleteFavoriteProductWithId(favoriteProductId))
        } catch (e: IOException) {
            Resource.Error(e)
        }
    }
}