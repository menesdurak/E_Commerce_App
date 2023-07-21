package com.menesdurak.e_ticaret_uygulamasi.presentation.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.menesdurak.e_ticaret_uygulamasi.common.Resource
import com.menesdurak.e_ticaret_uygulamasi.data.local.entity.CartProduct
import com.menesdurak.e_ticaret_uygulamasi.domain.use_case.add_cart_product.AddCartProductUseCase
import com.menesdurak.e_ticaret_uygulamasi.domain.use_case.delete_all_cart_products.DeleteAllCartProductsUseCase
import com.menesdurak.e_ticaret_uygulamasi.domain.use_case.delete_all_checked_cart_products.DeleteAllCheckedCartProductsUseCase
import com.menesdurak.e_ticaret_uygulamasi.domain.use_case.delete_cart_product.DeleteCartProductUseCase
import com.menesdurak.e_ticaret_uygulamasi.domain.use_case.get_all_cart_products.GetAllCartProductsUseCase
import com.menesdurak.e_ticaret_uygulamasi.domain.use_case.get_all_checked_cart_products.GetAllCheckedCartProductsUseCase
import com.menesdurak.e_ticaret_uygulamasi.domain.use_case.update_cart_product_amount.UpdateCartProductAmountUseCase
import com.menesdurak.e_ticaret_uygulamasi.domain.use_case.update_cart_product_checked_status.UpdateCartProductCheckedStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val addCartProductUseCase: AddCartProductUseCase,
    private val deleteCartProductUseCase: DeleteCartProductUseCase,
    private val getAllCartProductsUseCase: GetAllCartProductsUseCase,
    private val deleteAllCartProductsUseCase: DeleteAllCartProductsUseCase,
    private val updateCartProductCheckedStatusUseCase: UpdateCartProductCheckedStatusUseCase,
    private val updateCartProductAmountUseCase: UpdateCartProductAmountUseCase,
    private val getAllCheckedCartProductsUseCase: GetAllCheckedCartProductsUseCase,
    private val deleteAllCheckedCartProductsUseCase: DeleteAllCheckedCartProductsUseCase
) : ViewModel() {

    private val _cartProductsList = MutableLiveData<Resource<List<CartProduct>>>(Resource.Loading)
    val cartProductList: LiveData<Resource<List<CartProduct>>> = _cartProductsList

    private val _checkedAllCartProductsList = MutableLiveData<Resource<List<CartProduct>>>(Resource.Loading)
    val checkedAllCartProductsList: LiveData<Resource<List<CartProduct>>> = _checkedAllCartProductsList

    fun getAllCartProducts() {
        viewModelScope.launch {
            _cartProductsList.value = Resource.Loading
            _cartProductsList.value = getAllCartProductsUseCase()!!
        }
    }

    fun addCartProduct(cartProduct: CartProduct) {
        viewModelScope.launch {
            addCartProductUseCase(cartProduct)
        }
    }

    fun deleteCartProduct(cartProductId: Int) {
        viewModelScope.launch {
            deleteCartProductUseCase(cartProductId)
        }
    }

    fun deleteAllCartProducts() {
        viewModelScope.launch {
            deleteAllCartProductsUseCase()
        }
    }

    fun updateCartProductCheckedStatus(isCheckedStatus: Boolean, cartProductId: Int) {
        viewModelScope.launch {
            updateCartProductCheckedStatusUseCase(isCheckedStatus, cartProductId)
        }
    }

    fun updateCartProductAmount(newAmount: Int, cartProductId: Int) {
        viewModelScope.launch {
            updateCartProductAmountUseCase(newAmount, cartProductId)
        }
    }

    fun getAllCheckedCartProducts() {
        viewModelScope.launch {
            _checkedAllCartProductsList.value = Resource.Loading
            _checkedAllCartProductsList.value = getAllCheckedCartProductsUseCase()!!
        }
    }

    fun deleteAllCheckedCartProducts() {
        viewModelScope.launch {
            deleteAllCheckedCartProductsUseCase()
        }
    }
}