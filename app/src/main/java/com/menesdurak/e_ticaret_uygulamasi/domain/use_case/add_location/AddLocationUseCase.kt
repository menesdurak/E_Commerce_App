package com.menesdurak.e_ticaret_uygulamasi.domain.use_case.add_location

import com.menesdurak.e_ticaret_uygulamasi.common.Resource
import com.menesdurak.e_ticaret_uygulamasi.data.local.entity.Location
import com.menesdurak.e_ticaret_uygulamasi.domain.repository.LocalRepository
import java.io.IOException
import javax.inject.Inject

class AddLocationUseCase @Inject constructor(
    private val localRepository: LocalRepository
) {

    suspend operator fun invoke(location: Location) {
        try {
            Resource.Success(localRepository.addLocation(location))
        } catch (e: IOException) {
            Resource.Error(e)
        }
    }
}