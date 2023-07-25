package com.menesdurak.e_ticaret_uygulamasi.presentation

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.menesdurak.e_ticaret_uygulamasi.R
import com.menesdurak.e_ticaret_uygulamasi.common.Resource
import com.menesdurak.e_ticaret_uygulamasi.common.addCurrencySign
import com.menesdurak.e_ticaret_uygulamasi.databinding.FragmentProductDetailBinding
import com.menesdurak.e_ticaret_uygulamasi.presentation.categories.CategoriesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductDetailFragment : Fragment() {
    private var _binding: FragmentProductDetailBinding? = null
    private val binding get() = _binding!!

    private val categoriesViewModel: CategoriesViewModel by viewModels()

    private var productId = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductDetailBinding.inflate(inflater, container, false)
        val view = binding.root

        //Receiving clicked character ID and location ID
        val args: ProductDetailFragmentArgs by navArgs()
        productId = args.productId

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        categoriesViewModel.getSingleProduct(productId)

        categoriesViewModel.product.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    binding.tvProductName.text = it.data.title
                    binding.ratingBar.rating = it.data.rating.rate.toFloat()
                    binding.tvRatingCount.text = it.data.rating.count.toString()
                    binding.tvPrice.text = it.data.price.toDouble().addCurrencySign()

                    Glide
                        .with(requireContext())
                        .load(it.data.image)
                        .placeholder(R.drawable.loading_200x200)
                        .into(binding.ivProduct)
                }

                is Resource.Error -> {
                    Log.e("error", "Error in Product Detail Fragment")
                }

                Resource.Loading -> {

                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}