package com.menesdurak.e_ticaret_uygulamasi.domain.use_case.get_products_from_category

import com.menesdurak.e_ticaret_uygulamasi.common.Resource
import com.menesdurak.e_ticaret_uygulamasi.data.remote.dto.Product
import com.menesdurak.e_ticaret_uygulamasi.domain.repository.RemoteRepository
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetProductsFromCategoryUseCase @Inject constructor(
    private val remoteRepository: RemoteRepository
) {

    suspend operator fun invoke(categoryName: String): Resource<List<Product>> {
        return try {
            Resource.Success(remoteRepository.getProductsFromCategory(categoryName))
        } catch (e: HttpException) {
            Resource.Error(e)
        } catch (e: IOException) {
            Resource.Error(e)
        }
    }
}