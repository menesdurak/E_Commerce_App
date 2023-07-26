package com.menesdurak.e_ticaret_uygulamasi.data.mapper

import com.menesdurak.e_ticaret_uygulamasi.common.mapper.Mapper
import com.menesdurak.e_ticaret_uygulamasi.data.remote.dto.Product
import com.menesdurak.e_ticaret_uygulamasi.data.remote.dto.ProductUi

class ProductToProductUiMapper : Mapper<Product, ProductUi> {
    override fun map(input: Product): ProductUi {
        return ProductUi(
            category = input.category,
            description = input.description,
            id = input.id,
            image = input.image,
            price = input.price,
            title = input.title,
            rating = input.rating
        )
    }
}