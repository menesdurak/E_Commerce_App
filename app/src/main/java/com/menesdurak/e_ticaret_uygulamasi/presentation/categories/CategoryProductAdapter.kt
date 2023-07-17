package com.menesdurak.e_ticaret_uygulamasi.presentation.categories

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.menesdurak.e_ticaret_uygulamasi.R
import com.menesdurak.e_ticaret_uygulamasi.data.remote.dto.Product
import com.menesdurak.e_ticaret_uygulamasi.databinding.ItemCategoryProductBinding

class CategoryProductAdapter(
    private val onProductClick: (Product) -> Unit,
    private val onFavoriteClick: (Product) -> Unit
    ) :
    RecyclerView.Adapter<CategoryProductAdapter.CategoryProductHolder>() {

    private val itemList = mutableListOf<Product>()

    inner class CategoryProductHolder(private val binding: ItemCategoryProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            binding.tvProductName.text = product.title
            binding.tvProductPrice.text = product.price

            Glide
                .with(binding.root.context)
                .load(itemList[adapterPosition].image)
                .placeholder(R.drawable.loading_200x200)
                .into(binding.ivProduct)

            binding.root.setOnClickListener {
                onProductClick.invoke(product)
            }

            binding.ivFavorite.setOnClickListener {
                onFavoriteClick.invoke(product)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryProductHolder {
        val bind = ItemCategoryProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryProductHolder(bind)
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: CategoryProductHolder, position: Int) {
        holder.bind(itemList[position])
    }

    fun updateList(newList: List<Product>) {
        itemList.clear()
        itemList.addAll(newList)
        notifyDataSetChanged()
    }
}