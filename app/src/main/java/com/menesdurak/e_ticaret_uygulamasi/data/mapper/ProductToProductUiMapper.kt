package com.menesdurak.e_ticaret_uygulamasi.data.mapper

import com.menesdurak.e_ticaret_uygulamasi.common.mapper.ListMapper
import com.menesdurak.e_ticaret_uygulamasi.common.mapper.Mapper
import com.menesdurak.e_ticaret_uygulamasi.data.remote.dto.Product
import com.menesdurak.e_ticaret_uygulamasi.data.remote.dto.ProductUi

class ProductToProductUiMapper : ListMapper<Product, ProductUi> {
    override fun map(input: List<Product>): List<ProductUi> {
        return input.map {
            ProductUi(
                category = it.category,
                description = it.description,
                id = it.id,
                image = it.image,
                price = it.price,
                title = it.title
            )
        }
    }

}