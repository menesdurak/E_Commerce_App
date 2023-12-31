package com.menesdurak.e_ticaret_uygulamasi.presentation.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.menesdurak.e_ticaret_uygulamasi.R
import com.menesdurak.e_ticaret_uygulamasi.common.addCurrencySign
import com.menesdurak.e_ticaret_uygulamasi.common.round
import com.menesdurak.e_ticaret_uygulamasi.data.local.entity.CartProduct
import com.menesdurak.e_ticaret_uygulamasi.databinding.ItemCartProductBinding

class CartProductAdapter(
    private val onCheckboxClick: (Int, CartProduct) -> Unit,
    private val onDecreaseClick: (Int, CartProduct) -> Unit,
    private val onIncreaseClick: (Int, CartProduct) -> Unit,
    private val onProductClick: (CartProduct) -> Unit
) : RecyclerView.Adapter<CartProductAdapter.CartProductHolder>() {

    private val itemList = mutableListOf<CartProduct>()

    inner class CartProductHolder(private val binding: ItemCartProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: CartProduct) {
            binding.tvProductName.text = product.title
            binding.tvPrice.text = product.price.toDouble().addCurrencySign()
            binding.tvAmount.text = product.amount.toString()
            val randomNumber = (1..3).random()
            binding.tvCargoInfo.text = String.format(
                binding.root.resources.getString(R.string.you_will_get_you_cargo_in_days),
                randomNumber
            )

            binding.checkbox.isChecked = product.isChecked

            if (product.amount == 1) {
                binding.ivDecrease.setImageResource(R.drawable.ic_delete_outlined_main)
            } else {
                binding.ivDecrease.setImageResource(R.drawable.ic_minus)
            }

            Glide
                .with(binding.root.context)
                .load(itemList[adapterPosition].image)
                .placeholder(R.drawable.loading_200x200)
                .into(binding.ivProduct)

            binding.root.setOnClickListener {
                onProductClick.invoke(product)
            }

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

    fun removeItem(item: CartProduct) {
        itemList.remove(item)
        notifyDataSetChanged()
    }

    fun removeGivenItems(list: List<CartProduct>) {
        itemList.removeAll(list)
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

    fun updateCheckedStatusAllChecked() {
        for (index in 0 until itemList.size) {
            itemList[index].isChecked = true
        }
        notifyDataSetChanged()
    }

    fun updateCheckedStatusAllNotChecked() {
        for (index in 0 until itemList.size) {
            itemList[index].isChecked = false
        }
        notifyDataSetChanged()
    }

    fun calculateTotalPrice() : Double {
        var totalPrice = 0.0
        for (index in 0 until itemList.size) {
            totalPrice += itemList[index].amount * itemList[index].price.toDouble() round 2
        }
        return totalPrice
    }

}