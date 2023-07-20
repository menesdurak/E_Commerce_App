package com.menesdurak.e_ticaret_uygulamasi.data.mapper

import com.menesdurak.e_ticaret_uygulamasi.common.mapper.Mapper
import com.menesdurak.e_ticaret_uygulamasi.data.local.entity.CartProduct
import com.menesdurak.e_ticaret_uygulamasi.data.remote.dto.ProductUi

class ProductUiToCartProductMapper: Mapper<ProductUi, CartProduct> {
    override fun map(input: ProductUi): CartProduct {
        return CartProduct(
            category = input.category,
            description = input.description,
            id = input.id,
            image = input.image,
            price = input.price,
            title = input.title,
            amount = 1
        )
    }
}