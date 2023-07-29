package com.menesdurak.e_ticaret_uygulamasi.domain.use_case.update_all_cart_products_checked_status

import com.menesdurak.e_ticaret_uygulamasi.common.Resource
import com.menesdurak.e_ticaret_uygulamasi.domain.repository.LocalRepository
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class UpdateAllCartProductsToCheckedUseCase @Inject constructor(
    private val localRepository: LocalRepository
) {

    suspend operator fun invoke() {
        try {
            Resource.Success(localRepository.updateAllCartProductsToChecked())
        } catch (e: HttpException) {
            Resource.Error(e)
        } catch (e: IOException) {
            Resource.Error(e)
        }
    }
}