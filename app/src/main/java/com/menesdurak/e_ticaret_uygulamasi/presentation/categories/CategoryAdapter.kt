package com.menesdurak.e_ticaret_uygulamasi.presentation.categories

import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.menesdurak.e_ticaret_uygulamasi.R
import com.menesdurak.e_ticaret_uygulamasi.data.remote.dto.CategoryUi
import com.menesdurak.e_ticaret_uygulamasi.databinding.ItemCategoryBinding

class CategoryAdapter(private val onCategoryClicked: (Int, CategoryUi) -> Unit) :
    RecyclerView.Adapter<CategoryAdapter.CategoryHolder>() {

    private val itemList = mutableListOf<CategoryUi>()

    private var oldCheckedCategoryPosition = 0

    inner class CategoryHolder(private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(category: CategoryUi) {
            binding.tvCategoryTitle.text = category.name

            if (!category.isChecked) {
                binding.root.background = ContextCompat.getDrawable(
                    binding.root.context,
                    R.drawable.black_border_rounded
                )
                binding.tvCategoryTitle.setTypeface(null, Typeface.NORMAL)
            } else {
                binding.root.background = ContextCompat.getDrawable(
                    binding.root.context,
                    R.drawable.sub2_border_rounded
                )
                binding.tvCategoryTitle.setTypeface(null, Typeface.BOLD)
            }

            when (category.name) {
                "electronics" -> {
                    binding.ivCategory.setImageDrawable(
                        binding.root.resources.getDrawable(R.drawable.electronics, null)
                    )
                }

                "jewelery" -> {
                    binding.ivCategory.setImageDrawable(
                        binding.root.resources.getDrawable(R.drawable.jewelery, null)
                    )
                }

                "men's clothing" -> {
                    binding.ivCategory.setImageDrawable(
                        binding.root.resources.getDrawable(R.drawable.men_s_clothing, null)
                    )
                }

                "women's clothing" -> {
                    binding.ivCategory.setImageDrawable(
                        binding.root.resources.getDrawable(R.drawable.women_s_clothing, null)
                    )
                }

                "all" -> {
                    binding.ivCategory.setImageDrawable(
                        binding.root.resources.getDrawable(R.drawable.all, null)
                    )
                }
            }

            binding.root.setOnClickListener {
                onCategoryClicked.invoke(adapterPosition, category)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
        val bind = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryHolder(bind)
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
        holder.bind(itemList[position])
    }

    fun updateList(newList: List<CategoryUi>) {
        itemList.clear()
        itemList.addAll(newList)
        notifyDataSetChanged()
    }

    fun updateCheckedStatus(position: Int) {
        itemList[oldCheckedCategoryPosition].isChecked = false
        itemList[position].isChecked = !itemList[position].isChecked
        notifyItemChanged(oldCheckedCategoryPosition)
        notifyItemChanged(position)
        oldCheckedCategoryPosition = position
    }
}