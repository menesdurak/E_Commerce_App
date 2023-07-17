package com.menesdurak.e_ticaret_uygulamasi.presentation.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.menesdurak.e_ticaret_uygulamasi.common.Resource
import com.menesdurak.e_ticaret_uygulamasi.domain.use_case.GetAllCategoriesUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class CategoriesViewModel @Inject constructor(
    private val getAllCategoriesUseCase: GetAllCategoriesUseCase
): ViewModel() {

    private val _categoriesList = MutableLiveData<Resource<List<String>>>(Resource.Loading)
    val categoriesList : LiveData<Resource<List<String>>> = _categoriesList

    fun getCategories() {
        viewModelScope.launch {
            _categoriesList.value = Resource.Loading
            _categoriesList.value = getAllCategoriesUseCase()!!
        }
    }
}