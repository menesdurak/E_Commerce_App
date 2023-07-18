package com.menesdurak.e_ticaret_uygulamasi.presentation.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.menesdurak.e_ticaret_uygulamasi.common.Resource
import com.menesdurak.e_ticaret_uygulamasi.data.local.entity.FavoriteProduct
import com.menesdurak.e_ticaret_uygulamasi.domain.use_case.add_favorite.AddFavoriteProductUseCase
import com.menesdurak.e_ticaret_uygulamasi.domain.use_case.delete_favorite.DeleteFavoriteProductUseCase
import com.menesdurak.e_ticaret_uygulamasi.domain.use_case.get_all_favorite_products.GetAllFavoriteProductsUseCase
import com.menesdurak.e_ticaret_uygulamasi.domain.use_case.get_all_favorite_products_id.GetAllFavoriteProductsIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val getAllFavoriteProductsUseCase: GetAllFavoriteProductsUseCase,
    private val deleteFavoriteProductUseCase: DeleteFavoriteProductUseCase
) : ViewModel() {

    private val _favoriteProductsList =
        MutableLiveData<Resource<List<FavoriteProduct>>>(Resource.Loading)
    val favoriteProductsList: LiveData<Resource<List<FavoriteProduct>>> = _favoriteProductsList

    fun getAllFavoriteProducts() {
        viewModelScope.launch {
            _favoriteProductsList.value = Resource.Loading
            _favoriteProductsList.value = getAllFavoriteProductsUseCase()!!
        }
    }

    fun deleteFavoriteProduct(favoriteProductId: Int) {
        viewModelScope.launch {
            deleteFavoriteProductUseCase(favoriteProductId)
        }
    }

}