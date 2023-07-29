package com.menesdurak.e_ticaret_uygulamasi.presentation.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.menesdurak.e_ticaret_uygulamasi.R
import com.menesdurak.e_ticaret_uygulamasi.common.addCurrencySign
import com.menesdurak.e_ticaret_uygulamasi.data.remote.dto.ProductUi
import com.menesdurak.e_ticaret_uygulamasi.databinding.ItemCategoryProductBinding
import com.menesdurak.e_ticaret_uygulamasi.databinding.ItemForYouProductBinding

class ForYouAdapter(
    private val onProductClick: (ProductUi) -> Unit,
    private val onFavoriteClick: (Int, ProductUi) -> Unit,
    private val onAddToCartClick: (Int, ProductUi) -> Unit,
) : RecyclerView.Adapter<ForYouAdapter.ProductHolder>() {

    private val itemList = mutableListOf<ProductUi>()

    private val favoriteProductsIdList = mutableListOf<Int>()

    inner class ProductHolder(private val binding: ItemForYouProductBinding) :
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

            binding.root.setOnClickListener {
                onProductClick.invoke(product)
            }

            binding.ivFavorite.setOnClickListener {
                onFavoriteClick.invoke(adapterPosition, product)
            }

            binding.btnBuy.setOnClickListener {
                onAddToCartClick.invoke(adapterPosition, product)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductHolder {
        val bind =
            ItemForYouProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductHolder(bind)
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: ProductHolder, position: Int) {
        holder.bind(itemList[position])
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