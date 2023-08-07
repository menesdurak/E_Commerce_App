package com.menesdurak.e_ticaret_uygulamasi.domain.use_case.delete_all_locations

import com.menesdurak.e_ticaret_uygulamasi.common.Resource
import com.menesdurak.e_ticaret_uygulamasi.domain.repository.LocalRepository
import java.io.IOException
import javax.inject.Inject

class DeleteAllLocationsUseCase @Inject constructor(
    private val localRepository: LocalRepository
) {

    suspend operator fun invoke() {
        try {
            Resource.Success(localRepository.deleteAllLocations())
        } catch (e: IOException) {
            Resource.Error(e)
        }
    }
}