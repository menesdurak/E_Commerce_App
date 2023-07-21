package com.menesdurak.e_ticaret_uygulamasi.presentation.favorites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.menesdurak.e_ticaret_uygulamasi.R
import com.menesdurak.e_ticaret_uygulamasi.data.local.entity.FavoriteProduct
import com.menesdurak.e_ticaret_uygulamasi.databinding.ItemFavoriteProductBinding

class FavoriteProductAdapter(
    private val onProductClick: (FavoriteProduct) -> Unit,
    private val onFavoriteClick: (Int, Int) -> Unit,
    private val onBuyClick: (Int, FavoriteProduct) -> Unit
) :
    RecyclerView.Adapter<FavoriteProductAdapter.FavoriteProductHolder>() {

    private val itemList = mutableListOf<FavoriteProduct>()

    inner class FavoriteProductHolder(private val binding: ItemFavoriteProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: FavoriteProduct) {
            binding.tvProductName.text = product.title
            binding.tvProductPrice.text = product.price
            binding.tvFavoriteDate.text = product.whenFavorite

            Glide
                .with(binding.root.context)
                .load(itemList[adapterPosition].image)
                .placeholder(R.drawable.loading_200x200)
                .into(binding.ivProduct)

            binding.root.setOnClickListener {
                onProductClick.invoke(product)
            }

            binding.ivFavorite.setOnClickListener {
                onFavoriteClick.invoke(adapterPosition, product.id)
            }

            binding.btnBuy.setOnClickListener {
                onBuyClick.invoke(adapterPosition, product)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteProductHolder {
        val bind =
            ItemFavoriteProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteProductHolder(bind)
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: FavoriteProductHolder, position: Int) {
        holder.bind(itemList[position])
    }

    fun updateList(newList: List<FavoriteProduct>) {
        itemList.clear()
        itemList.addAll(newList)
        notifyDataSetChanged()
    }

    fun deleteItem(position: Int, productId: Int) {
        itemList.removeAt(position)
        notifyItemRemoved(position)
    }
}