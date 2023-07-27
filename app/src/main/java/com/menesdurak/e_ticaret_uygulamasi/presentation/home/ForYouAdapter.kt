package com.menesdurak.e_ticaret_uygulamasi.presentation.home

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.menesdurak.e_ticaret_uygulamasi.R
import com.menesdurak.e_ticaret_uygulamasi.common.addCurrencySign
import com.menesdurak.e_ticaret_uygulamasi.data.remote.dto.ProductUi
import com.menesdurak.e_ticaret_uygulamasi.databinding.ItemCategoryProductBinding

class ForYouAdapter : RecyclerView.Adapter<ForYouAdapter.ProductHolder>() {

    private val itemList = mutableListOf<ProductUi>()

    private val favoriteProductsIdList = mutableListOf<Int>()

    inner class ProductHolder(private val binding: ItemCategoryProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: ProductUi) {
            binding.tvProductName.text = product.title
            binding.tvProductPrice.text = product.price.toDouble().addCurrencySign()
            binding.ratingBar.rating = product.rating.rate.toFloat()
            binding.tvRatingCount.text = product.rating.count.toString()

            if (product.id !in favoriteProductsIdList) {
                binding.ivFavorite.setImageResource(R.drawable.ic_favorite_empty)
                product.isFavorite = false
            } else {
                binding.ivFavorite.setImageResource(R.drawable.ic_favorite)
                product.isFavorite = true
            }

            Glide
                .with(binding.root.context)
                .load(itemList[adapterPosition].image)
                .placeholder(R.drawable.loading_200x200)
                .into(binding.ivProduct)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductHolder {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: ProductHolder, position: Int) {
        TODO("Not yet implemented")
    }

    fun updateList(newList: List<ProductUi>) {
        itemList.clear()
        itemList.addAll(newList)
        notifyDataSetChanged()
    }

    fun updateFavoriteProductsIdList(newList: List<Int>) {
        favoriteProductsIdList.clear()
        favoriteProductsIdList.addAll(newList)
        notifyDataSetChanged()
    }

    fun updateFavoriteStatusOfProduct(position: Int, id: Int) {
        if (id !in favoriteProductsIdList) {
            favoriteProductsIdList.add(id)
        } else {
            favoriteProductsIdList.remove(id)
        }
        itemList[position].isFavorite = !itemList[position].isFavorite
        notifyItemChanged(position)
    }
}