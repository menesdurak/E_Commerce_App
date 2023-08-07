package com.menesdurak.e_ticaret_uygulamasi.domain.use_case.get_all_locations

import com.menesdurak.e_ticaret_uygulamasi.common.Resource
import com.menesdurak.e_ticaret_uygulamasi.data.local.entity.Location
import com.menesdurak.e_ticaret_uygulamasi.domain.repository.LocalRepository
import java.io.IOException
import javax.inject.Inject

class GetAllLocationsUseCase @Inject constructor(
    private val localRepository: LocalRepository
) {

    suspend operator fun invoke() : Resource<List<Location>> {
        return try {
            Resource.Success(localRepository.getAllLocations())
        } catch (e: IOException) {
            Resource.Error(e)
        }
    }
}