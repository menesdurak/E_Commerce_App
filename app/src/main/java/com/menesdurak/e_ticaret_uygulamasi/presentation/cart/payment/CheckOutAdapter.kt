package com.menesdurak.e_ticaret_uygulamasi.presentation.cart.payment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.menesdurak.e_ticaret_uygulamasi.R
import com.menesdurak.e_ticaret_uygulamasi.data.local.entity.CartProduct
import com.menesdurak.e_ticaret_uygulamasi.databinding.ItemCheckoutProductBinding

class CheckOutAdapter : RecyclerView.Adapter<CheckOutAdapter.CheckOutProductHolder>() {

    private val itemList = mutableListOf<CartProduct>()

    inner class CheckOutProductHolder(private val binding: ItemCheckoutProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: CartProduct) {
            binding.tvName.text = product.title
            binding.tvTotalPrice.text = (product.price.toDouble() * product.amount).toString()
            binding.tvAmount.text = product.amount.toString()

            Glide
                .with(binding.root.context)
                .load(itemList[adapterPosition].image)
                .placeholder(R.drawable.loading_200x200)
                .into(binding.ivImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckOutProductHolder {
        val bind =
            ItemCheckoutProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CheckOutProductHolder(bind)
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: CheckOutProductHolder, position: Int) {
        holder.bind(itemList[position])
    }

    fun updateList(newList: List<CartProduct>) {
        itemList.clear()
        itemList.addAll(newList)
        notifyDataSetChanged()
    }
}