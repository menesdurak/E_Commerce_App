package com.menesdurak.e_ticaret_uygulamasi.domain.use_case.get_all_products

import com.menesdurak.e_ticaret_uygulamasi.common.Resource
import com.menesdurak.e_ticaret_uygulamasi.data.local.entity.Location
import com.menesdurak.e_ticaret_uygulamasi.data.remote.dto.Product
import com.menesdurak.e_ticaret_uygulamasi.domain.repository.LocalRepository
import com.menesdurak.e_ticaret_uygulamasi.domain.repository.RemoteRepository
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetAllProductsUseCase @Inject constructor(
    private val remoteRepository: RemoteRepository
) {

    suspend operator fun invoke(): Resource<List<Product>> {
        return try {
            Resource.Success(remoteRepository.getAllProducts())
        } catch (e: HttpException) {
            Resource.Error(e)
        } catch (e: IOException) {
            Resource.Error(e)
        }
    }
}