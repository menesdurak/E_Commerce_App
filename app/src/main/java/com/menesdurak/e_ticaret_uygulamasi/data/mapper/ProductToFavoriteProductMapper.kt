package com.menesdurak.e_ticaret_uygulamasi.data.mapper

import com.menesdurak.e_ticaret_uygulamasi.common.mapper.Mapper
import com.menesdurak.e_ticaret_uygulamasi.data.local.entity.FavoriteProduct
import com.menesdurak.e_ticaret_uygulamasi.data.remote.dto.Product
import java.text.SimpleDateFormat
import java.util.Calendar

class ProductToFavoriteProductMapper: Mapper<Product, FavoriteProduct> {
    override fun map(input: Product): FavoriteProduct {
        val time = Calendar.getInstance().time
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm")
        val current = formatter.format(time)
        return FavoriteProduct(
            category = input.category,
            description = input.description,
            id = input.id,
            image = input.image,
            price = input.price,
            title = input.title,
            whenFavorite = current
        )
    }
}