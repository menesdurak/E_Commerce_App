package com.menesdurak.e_ticaret_uygulamasi.presentation.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.menesdurak.e_ticaret_uygulamasi.R
import com.menesdurak.e_ticaret_uygulamasi.databinding.ViewPagerItemBinding

class HomeViewPagerAdapter(private val imageList: List<Int>) :
    RecyclerView.Adapter<HomeViewPagerAdapter.ImageHolder>() {

    inner class ImageHolder(private val binding: ViewPagerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(image: Int) {
            Glide
                .with(binding.root.context)
                .load(imageList[adapterPosition])
                .placeholder(R.drawable.loading_200x200)
                .into(binding.ivViewPager)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        val bind =
            ViewPagerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageHolder(bind)
    }

    override fun getItemCount(): Int = imageList.size

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        holder.bind(imageList[position])
    }
}