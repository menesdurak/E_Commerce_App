package com.menesdurak.e_ticaret_uygulamasi.presentation.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.menesdurak.e_ticaret_uygulamasi.common.Resource
import com.menesdurak.e_ticaret_uygulamasi.data.local.entity.FavoriteProduct
import com.menesdurak.e_ticaret_uygulamasi.data.remote.dto.Product
import com.menesdurak.e_ticaret_uygulamasi.domain.use_case.add_favorite.AddFavoriteProductUseCase
import com.menesdurak.e_ticaret_uygulamasi.domain.use_case.delete_favorite.DeleteFavoriteProductUseCase
import com.menesdurak.e_ticaret_uygulamasi.domain.use_case.get_all_categories.GetAllCategoriesUseCase
import com.menesdurak.e_ticaret_uygulamasi.domain.use_case.get_all_favorites.GetAllFavoriteProductsUseCase
import com.menesdurak.e_ticaret_uygulamasi.domain.use_case.get_products_from_category.GetProductsFromCategoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val getAllCategoriesUseCase: GetAllCategoriesUseCase,
    private val getProductsFromCategoryUseCase: GetProductsFromCategoryUseCase,
    private val addFavoriteProductUseCase: AddFavoriteProductUseCase,
    private val deleteFavoriteProductUseCase: DeleteFavoriteProductUseCase
) : ViewModel() {

    private val _categoriesList = MutableLiveData<Resource<List<String>>>(Resource.Loading)
    val categoriesList: LiveData<Resource<List<String>>> = _categoriesList

    private val _productsList = MutableLiveData<Resource<List<Product>>>(Resource.Loading)
    val productsList: LiveData<Resource<List<Product>>> = _productsList

    fun getAllCategories() {
        viewModelScope.launch {
            _categoriesList.value = Resource.Loading
            _categoriesList.value = getAllCategoriesUseCase()!!
        }
    }

    fun getProductsFromCategory(categoryName: String) {
        viewModelScope.launch {
            _productsList.value = Resource.Loading
            _productsList.value = getProductsFromCategoryUseCase(categoryName)!!
        }
    }

    fun addFavoriteProduct(favoriteProduct: FavoriteProduct) {
        viewModelScope.launch {
            addFavoriteProductUseCase(favoriteProduct)
        }
    }

    fun deleteFavoriteProduct(favoriteProductId: Int) {
        viewModelScope.launch {
            deleteFavoriteProductUseCase(favoriteProductId)
        }
    }

}