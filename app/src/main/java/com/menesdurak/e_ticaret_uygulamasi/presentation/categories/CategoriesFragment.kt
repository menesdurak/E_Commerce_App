package com.menesdurak.e_ticaret_uygulamasi.presentation.categories

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.menesdurak.e_ticaret_uygulamasi.R
import com.menesdurak.e_ticaret_uygulamasi.common.Resource
import com.menesdurak.e_ticaret_uygulamasi.common.addOnScrollHiddenView
import com.menesdurak.e_ticaret_uygulamasi.data.mapper.ProductListToProductUiListMapper
import com.menesdurak.e_ticaret_uygulamasi.data.mapper.ProductUiToCartProductMapper
import com.menesdurak.e_ticaret_uygulamasi.data.mapper.ProductUiToFavoriteProductMapper
import com.menesdurak.e_ticaret_uygulamasi.data.remote.dto.ProductUi
import com.menesdurak.e_ticaret_uygulamasi.databinding.FragmentCategoriesBinding
import com.menesdurak.e_ticaret_uygulamasi.presentation.cart.CartViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoriesFragment : Fragment() {
    private var _binding: FragmentCategoriesBinding? = null
    private val binding get() = _binding!!

    private val categoriesViewModel: CategoriesViewModel by viewModels()
    private val cartViewModel: CartViewModel by viewModels()
    private val categoryAdapter: CategoryAdapter by lazy { CategoryAdapter(::onCategoryClick) }
    private val categoryProductAdapter: CategoryProductAdapter by lazy {
        CategoryProductAdapter(
            ::onProductClick,
            ::onFavoriteClick,
            ::onAddToCartClick
        )
    }
    private lateinit var bottomNavView: BottomNavigationView

    private var categoryName = "electronics"

    private var timer: CountDownTimer? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        val view = binding.root

        bottomNavView = requireActivity().findViewById(R.id.bottomNavMenu)
        bottomNavView.menu.getItem(1).isChecked = true

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        categoriesViewModel.getAllCategories()
        categoriesViewModel.getAllFavoriteProductsId()
        categoriesViewModel.getProductsFromCategory(categoryName)

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

        binding.rvCategories.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvCategories.adapter = categoryAdapter

        categoriesViewModel.categoriesList.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    categoryAdapter.updateList(it.data)
                }

                is Resource.Error -> {
                    Log.e("error", "Error in Categories Fragment")
                }

                Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        }

        binding.rvProducts.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvProducts.adapter = categoryProductAdapter

        categoriesViewModel.productsList.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    categoryProductAdapter.updateList(ProductListToProductUiListMapper().map(it.data))

                    binding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
                        override fun onQueryTextSubmit(query: String?): Boolean {
                            return false
                        }

                        override fun onQueryTextChange(newText: String?): Boolean {
                            filterProducts(newText ?: "", ProductListToProductUiListMapper().map(it.data), categoryProductAdapter)
                            return true
                        }

                    })
                }

                is Resource.Error -> {
                    Log.e("error", "Error in Categories Fragment")
                }

                Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        }

        categoriesViewModel.favoriteProductsIdList.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    categoryProductAdapter.updateFavoriteProductsIdList(it.data)
                }

                is Resource.Error -> {
                    Log.e("error", "Error in Categories Fragment")
                }

                Resource.Loading -> {

                }
            }
        }

        //Extension function for hiding categories when scrolling down
        binding.rvProducts.addOnScrollHiddenView(binding.rvCategories)

    }

    private fun onCategoryClick(categoryName: String) {
        categoriesViewModel.getProductsFromCategory(categoryName)
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun onProductClick(product: ProductUi) {
        val action =
            CategoriesFragmentDirections.actionCategoriesFragmentToProductDetailFragment(product.id)
        findNavController().navigate(action)
    }

    private fun onAddToCartClick(position: Int, product: ProductUi) {
        cartViewModel.addCartProduct(ProductUiToCartProductMapper().map(product))
        val button = binding.rvProducts.layoutManager?.findViewByPosition(position)
            ?.findViewById<Button>(R.id.btnBuy)
        button?.setBackgroundColor(binding.root.resources.getColor(R.color.sub2, null))
        button?.text = getString(R.string.in_cart)
        timer = object : CountDownTimer(2000, 50) {
            override fun onTick(millisUntilFinished: Long) {

            }

            override fun onFinish() {
                button?.setBackgroundColor(binding.root.resources.getColor(R.color.main, null))
                button?.text = getString(R.string.buy)
            }

        }.start()
    }

    private fun onFavoriteClick(position: Int, product: ProductUi) {
        val favoriteProduct = ProductUiToFavoriteProductMapper().map(product)
        if (product.isFavorite) {
            categoriesViewModel.deleteFavoriteProduct(favoriteProduct.id)
            categoryProductAdapter.updateFavoriteStatusOfProduct(position, favoriteProduct.id)
        } else {
            categoriesViewModel.addFavoriteProduct(favoriteProduct)
            categoryProductAdapter.updateFavoriteStatusOfProduct(position, favoriteProduct.id)
        }
    }

    private fun filterProducts(query: String, productList: List<ProductUi>, adapter: CategoryProductAdapter) {
        val filteredList = productList.filter {
            it.title.contains(query, ignoreCase = true)
        }
        adapter.updateList(filteredList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        timer?.cancel()
        timer = null
    }
}