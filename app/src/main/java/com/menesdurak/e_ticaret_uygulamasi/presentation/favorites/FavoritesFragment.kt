package com.menesdurak.e_ticaret_uygulamasi.presentation.favorites

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.menesdurak.e_ticaret_uygulamasi.R
import com.menesdurak.e_ticaret_uygulamasi.common.Resource
import com.menesdurak.e_ticaret_uygulamasi.data.local.entity.FavoriteProduct
import com.menesdurak.e_ticaret_uygulamasi.data.mapper.FavoriteProductToCartProductMapper
import com.menesdurak.e_ticaret_uygulamasi.databinding.FragmentFavoritesBinding
import com.menesdurak.e_ticaret_uygulamasi.presentation.cart.CartViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment : Fragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private val favoritesViewModel: FavoritesViewModel by viewModels()
    private val cartViewModel: CartViewModel by viewModels()
    private val favoriteProductAdapter: FavoriteProductAdapter by lazy {
        FavoriteProductAdapter(
            ::onProductClick,
            ::onFavoriteClick,
            ::onBuyClick
        )
    }

    private var timer: CountDownTimer? = null

    private lateinit var bottomNavView: BottomNavigationView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        val view = binding.root

        bottomNavView = requireActivity().findViewById(R.id.bottomNavMenu)
        bottomNavView.menu.getItem(3).isChecked = true

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favoritesViewModel.getAllFavoriteProducts()

        cartViewModel.getAllCheckedCartProducts()

        cartViewModel.checkedAllCartProductsList.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    val badge =
                        requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavMenu)
                            .getOrCreateBadge(R.id.cart)
                    badge.number = it.data.size
                    badge.isVisible = badge.number > 0
                }

                is Resource.Error -> {
                    Log.e("error", "Error in Home Fragment")
                }

                Resource.Loading -> {

                }
            }
        }

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
        timer?.cancel()
        timer = null
    }

    private fun onProductClick(product: FavoriteProduct) {
        val action =
            FavoritesFragmentDirections.actionFavoritesFragmentToProductDetailFragment(product.id)
        findNavController().navigate(action)
    }

    private fun onFavoriteClick(position: Int, productId: Int) {
        favoriteProductAdapter.deleteItem(position, productId)
        favoritesViewModel.deleteFavoriteProduct(productId)
    }

    private fun onBuyClick(position: Int, product: FavoriteProduct) {
        cartViewModel.addCartProduct(FavoriteProductToCartProductMapper().map(product))
        val button = binding.rvFavoriteProducts.layoutManager?.findViewByPosition(position)
            ?.findViewById<Button>(R.id.btnBuy)
        button?.setBackgroundColor(binding.root.resources.getColor(R.color.sub2, null))
        button?.text = getString(R.string.in_cart)
        timer = object : CountDownTimer(1000, 50) {
            override fun onTick(millisUntilFinished: Long) {

            }

            override fun onFinish() {
                button?.setBackgroundColor(binding.root.resources.getColor(R.color.main, null))
                button?.text = getString(R.string.buy)
            }

        }.start()
    }
}