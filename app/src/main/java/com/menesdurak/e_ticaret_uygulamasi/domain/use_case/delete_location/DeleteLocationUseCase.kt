package com.menesdurak.e_ticaret_uygulamasi.domain.use_case.delete_location

import com.menesdurak.e_ticaret_uygulamasi.common.Resource
import com.menesdurak.e_ticaret_uygulamasi.domain.repository.LocalRepository
import java.io.IOException
import javax.inject.Inject

class DeleteLocationUseCase @Inject constructor(
    private val localRepository: LocalRepository
) {

    suspend operator fun invoke(locationId: Int) {
        try {
            Resource.Success(localRepository.deleteLocationWithId(locationId))
        } catch (e: IOException) {
            Resource.Error(e)
        }
    }
}