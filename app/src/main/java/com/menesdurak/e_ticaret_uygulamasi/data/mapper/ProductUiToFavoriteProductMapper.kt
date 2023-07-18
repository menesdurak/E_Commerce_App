package com.menesdurak.e_ticaret_uygulamasi.data.mapper

import com.menesdurak.e_ticaret_uygulamasi.common.mapper.Mapper
import com.menesdurak.e_ticaret_uygulamasi.data.local.entity.FavoriteProduct
import com.menesdurak.e_ticaret_uygulamasi.data.remote.dto.Product
import com.menesdurak.e_ticaret_uygulamasi.data.remote.dto.ProductUi
import java.text.SimpleDateFormat
import java.util.Calendar

class ProductUiToFavoriteProductMapper: Mapper<ProductUi, FavoriteProduct> {
    override fun map(input: ProductUi): FavoriteProduct {
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