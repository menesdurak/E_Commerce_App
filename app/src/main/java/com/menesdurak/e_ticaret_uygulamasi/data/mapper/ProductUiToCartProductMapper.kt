package com.menesdurak.e_ticaret_uygulamasi.data.mapper

import com.menesdurak.e_ticaret_uygulamasi.common.mapper.Mapper
import com.menesdurak.e_ticaret_uygulamasi.common.round
import com.menesdurak.e_ticaret_uygulamasi.data.local.entity.CartProduct
import com.menesdurak.e_ticaret_uygulamasi.data.remote.dto.ProductUi

class ProductUiToCartProductMapper(private val discountRate: Float) :
    Mapper<ProductUi, CartProduct> {
    override fun map(input: ProductUi): CartProduct {
        return CartProduct(
            category = input.category,
            description = input.description,
            id = input.id,
            image = input.image,
            price = ((input.price.toDouble() * discountRate) round 2).toString(),
            title = input.title,
            amount = 1
        )
    }
}