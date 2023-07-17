package com.menesdurak.e_ticaret_uygulamasi.domain.use_case

import com.menesdurak.e_ticaret_uygulamasi.common.Resource
import com.menesdurak.e_ticaret_uygulamasi.domain.repository.RemoteRepository
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetAllCategoriesUseCase @Inject constructor(
    private val remoteRepository: RemoteRepository
) {

    suspend operator fun invoke(): Resource<List<String>> {
        return try {
            Resource.Success(remoteRepository.getAllCategories())
        } catch (e: HttpException) {
            Resource.Error(e)
        } catch (e: IOException) {
            Resource.Error(e)
        }
    }
}