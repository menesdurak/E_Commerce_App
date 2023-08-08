package com.menesdurak.e_ticaret_uygulamasi.data.mapper

import com.menesdurak.e_ticaret_uygulamasi.common.mapper.ListMapper
import com.menesdurak.e_ticaret_uygulamasi.data.remote.dto.BoughtProduct
import com.menesdurak.e_ticaret_uygulamasi.data.local.entity.CartProduct
import java.text.SimpleDateFormat
import java.util.Calendar

class CartProductListToBoughtProductListMapper(
    private val address: String,
    private val creditCardNumber: String,
    private val orderNumber: String
) : ListMapper<CartProduct, BoughtProduct> {
    override fun map(input: List<CartProduct>): List<BoughtProduct> {
        val time = Calendar.getInstance().time
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm")
        val current = formatter.format(time)
        return input.map {
            BoughtProduct(
                category = it.category,
                description = it.description,
                id = it.id,
                image = it.image,
                price = it.price,
                title = it.title,
                amount = it.amount,
                isDelivered = false,
                address = address,
                creditCardNumber = creditCardNumber,
                orderDate = current,
                orderNumber = orderNumber
            )
        }
    }
}