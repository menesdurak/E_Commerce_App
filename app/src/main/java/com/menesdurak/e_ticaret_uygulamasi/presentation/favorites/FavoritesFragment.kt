package com.menesdurak.e_ticaret_uygulamasi.presentation.favorites

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.menesdurak.e_ticaret_uygulamasi.R
import com.menesdurak.e_ticaret_uygulamasi.common.Resource
import com.menesdurak.e_ticaret_uygulamasi.data.local.entity.FavoriteProduct
import com.menesdurak.e_ticaret_uygulamasi.databinding.FragmentFavoritesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment : Fragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private val favoritesViewModel: FavoritesViewModel by viewModels()
    private val favoriteProductAdapter: FavoriteProductAdapter by lazy {
        FavoriteProductAdapter(
            ::onProductClick,
            ::onFavoriteClick
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favoritesViewModel.getAllFavoriteProducts()

        binding.rvFavoriteProducts.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvFavoriteProducts.adapter = favoriteProductAdapter

        favoritesViewModel.favoriteProductsList.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    favoriteProductAdapter.updateList(it.data)
                }

                is Resource.Error -> {
                    Log.e("error", "Error in Favorites Fragment")
                }

                Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onProductClick(product: FavoriteProduct) {
        Toast.makeText(requireContext(), product.title, Toast.LENGTH_SHORT).show()
    }

    private fun onFavoriteClick(position: Int, productId: Int) {
        favoriteProductAdapter.deleteItem(position, productId)
        favoritesViewModel.deleteFavoriteProduct(productId)
    }
}