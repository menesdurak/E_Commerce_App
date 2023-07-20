package com.menesdurak.e_ticaret_uygulamasi.presentation.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.menesdurak.e_ticaret_uygulamasi.R
import com.menesdurak.e_ticaret_uygulamasi.data.local.entity.CartProduct
import com.menesdurak.e_ticaret_uygulamasi.databinding.ItemCartProductBinding

class CartProductAdapter(
    private val onCheckboxClick: (Int, CartProduct) -> Unit,
    private val onDecreaseClick: (Int, CartProduct) -> Unit,
    private val onIncreaseClick: (Int, CartProduct) -> Unit,
) : RecyclerView.Adapter<CartProductAdapter.CartProductHolder>() {

    private val itemList = mutableListOf<CartProduct>()

    inner class CartProductHolder(private val binding: ItemCartProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: CartProduct) {
            binding.tvProductName.text = product.title
            binding.tvPrice.text = product.price
            binding.tvAmount.text = product.amount.toString()
            binding.tvCargoInfo.text = "You will get you cargo in ${(1..3).random()} days"

            binding.checkbox.isChecked = product.isChecked

            Glide
                .with(binding.root.context)
                .load(itemList[adapterPosition].image)
                .placeholder(R.drawable.loading_200x200)
                .into(binding.ivProduct)

            binding.checkbox.setOnClickListener {
                onCheckboxClick.invoke(adapterPosition, product)
            }

            binding.ivDecrease.setOnClickListener {
                onDecreaseClick.invoke(adapterPosition, product)
            }

            binding.ivIncrease.setOnClickListener {
                onIncreaseClick.invoke(adapterPosition, product)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartProductHolder {
        val bind =
            ItemCartProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartProductHolder(bind)
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: CartProductHolder, position: Int) {
        holder.bind(itemList[position])
    }

    fun updateList(newList: List<CartProduct>) {
        itemList.clear()
        itemList.addAll(newList)
        notifyDataSetChanged()
    }

    fun updateCheckedStatusOfProduct(position: Int, product: CartProduct) {
        itemList[position].isChecked = !itemList[position].isChecked
        notifyItemChanged(position)
    }

    fun decreaseAmount(position: Int, product: CartProduct) {
        if (product.amount > 1) {
            itemList[position].amount = product.amount - 1
            notifyItemChanged(position)
        }
    }

    fun increaseAmount(position: Int, product: CartProduct) {
        itemList[position].amount = product.amount + 1
        notifyItemChanged(position)
    }

}