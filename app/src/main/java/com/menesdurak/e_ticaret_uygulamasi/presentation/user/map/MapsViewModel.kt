package com.menesdurak.e_ticaret_uygulamasi.presentation.user.map

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.menesdurak.e_ticaret_uygulamasi.common.Resource
import com.menesdurak.e_ticaret_uygulamasi.data.local.entity.Location
import com.menesdurak.e_ticaret_uygulamasi.domain.use_case.add_location.AddLocationUseCase
import com.menesdurak.e_ticaret_uygulamasi.domain.use_case.delete_all_locations.DeleteAllLocationsUseCase
import com.menesdurak.e_ticaret_uygulamasi.domain.use_case.delete_location.DeleteLocationUseCase
import com.menesdurak.e_ticaret_uygulamasi.domain.use_case.get_all_locations.GetAllLocationsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(
    private val addLocationUseCase: AddLocationUseCase,
    private val deleteLocationUseCase: DeleteLocationUseCase,
    private val deleteAllLocationsUseCase: DeleteAllLocationsUseCase,
    private val getAllLocationsUseCase: GetAllLocationsUseCase
) : ViewModel() {

    private val _locations = MutableLiveData<Resource<List<Location>>>(Resource.Loading)
    val locations: MutableLiveData<Resource<List<Location>>> = _locations

    fun getAllLocations() {
        viewModelScope.launch {
            _locations.value = Resource.Loading
            _locations.value = getAllLocationsUseCase()!!
        }
    }

    fun addLocation(location: Location) {
        viewModelScope.launch {
            addLocationUseCase(location)
        }
    }

    fun deleteLocation(locationId: Int) {
        viewModelScope.launch {
            deleteLocationUseCase(locationId)
        }
    }

    fun deleteAllLocations() {
        viewModelScope.launch {
            deleteAllLocationsUseCase()
        }
    }
}