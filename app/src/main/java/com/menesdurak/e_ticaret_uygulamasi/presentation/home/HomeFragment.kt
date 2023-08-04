package com.menesdurak.e_ticaret_uygulamasi.presentation.home

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.menesdurak.e_ticaret_uygulamasi.R
import com.menesdurak.e_ticaret_uygulamasi.common.Resource
import com.menesdurak.e_ticaret_uygulamasi.data.mapper.ProductListToProductUiListMapper
import com.menesdurak.e_ticaret_uygulamasi.data.mapper.ProductUiToCartProductMapper
import com.menesdurak.e_ticaret_uygulamasi.data.mapper.ProductUiToFavoriteProductMapper
import com.menesdurak.e_ticaret_uygulamasi.data.remote.dto.ProductUi
import com.menesdurak.e_ticaret_uygulamasi.databinding.FragmentHomeBinding
import com.menesdurak.e_ticaret_uygulamasi.presentation.cart.CartViewModel
import com.menesdurak.e_ticaret_uygulamasi.presentation.categories.CategoriesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val cartViewModel: CartViewModel by viewModels()
    private val categoriesViewModel: CategoriesViewModel by viewModels()
    private val forYouAdapter: ForYouAdapter by lazy {
        ForYouAdapter(
            ::onProductClick,
            ::onFavoriteClick,
            ::onAddToCartClick
        )
    }
    private lateinit var bottomNavView: BottomNavigationView

    private var timer: CountDownTimer? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        bottomNavView = requireActivity().findViewById(R.id.bottomNavMenu)
        bottomNavView.menu.getItem(0).isChecked = true
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cartViewModel.getAllCheckedCartProducts()

        categoriesViewModel.getAllFavoriteProductsId()

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

        categoriesViewModel.favoriteProductsIdList.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    forYouAdapter.updateFavoriteProductsIdList(it.data)
                }

                is Resource.Error -> {
                    Log.e("error", "Error in Categories Fragment")
                }

                Resource.Loading -> {

                }
            }
        }

        binding.recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerView.adapter = forYouAdapter

        categoriesViewModel.getProductsFromCategory("electronics")

        categoriesViewModel.productsList.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    forYouAdapter.updateList(ProductListToProductUiListMapper().map(it.data))
                }

                is Resource.Error -> {
                    Log.e("error", "Error in Home Fragment")
                }

                Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        }

        val imageList = listOf(
            R.drawable.home5,
            R.drawable.home1,
            R.drawable.home2,
            R.drawable.home3,
            R.drawable.home4,
            R.drawable.home5,
            R.drawable.home1
        )
        val viewPagerAdapter = HomeViewPagerAdapter(imageList)
        binding.viewPager.adapter = viewPagerAdapter

        binding.viewPager.setCurrentItem(1, false)

        binding.tvViewPagerPage.text = "1 / 5"

        val recyclerView = binding.viewPager.getChildAt(0) as RecyclerView
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        val itemCount = binding.viewPager.adapter?.itemCount ?: 0
        // attach scroll listener
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(
                recyclerView: RecyclerView, dx: Int, dy: Int,
            ) {
                super.onScrolled(recyclerView, dx, dy)
                when (binding.viewPager.currentItem) {
                    6 -> {
                        binding.tvViewPagerPage.text =
                            "1 / ${itemCount - 2}"
                    }
                    0 -> {
                        binding.tvViewPagerPage.text =
                            "5 / ${itemCount - 2}"
                    }
                    else -> {
                        binding.tvViewPagerPage.text =
                            "${binding.viewPager.currentItem} / ${itemCount - 2}"
                    }
                }
                val firstItemVisible = layoutManager.findFirstVisibleItemPosition()
                val lastItemVisible = layoutManager.findLastVisibleItemPosition()
                if (firstItemVisible == (itemCount - 1) && dx > 0) {
                    recyclerView.scrollToPosition(1)
                } else if (lastItemVisible == 0 && dx < 0) {
                    recyclerView.scrollToPosition(itemCount - 2)
                }
            }
        })

        binding.ibLeft.setOnClickListener {
            binding.viewPager.currentItem = binding.viewPager.currentItem - 1
        }

        binding.ibRight.setOnClickListener {
            binding.viewPager.currentItem = binding.viewPager.currentItem + 1
        }

    }

    private fun onProductClick(product: ProductUi) {
        val action =
            HomeFragmentDirections.actionHomeFragmentToProductDetailFragment(product.id)
        findNavController().navigate(action)
    }

    private fun onAddToCartClick(position: Int, product: ProductUi) {
        cartViewModel.addCartProduct(ProductUiToCartProductMapper().map(product))
        val button = binding.recyclerView.layoutManager?.findViewByPosition(position)
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

    private fun onFavoriteClick(position: Int, product: ProductUi) {
        val favoriteProduct = ProductUiToFavoriteProductMapper().map(product)
        if (product.isFavorite) {
            categoriesViewModel.deleteFavoriteProduct(favoriteProduct.id)
            forYouAdapter.updateFavoriteStatusOfProduct(position, favoriteProduct.id)
        } else {
            categoriesViewModel.addFavoriteProduct(favoriteProduct)
            forYouAdapter.updateFavoriteStatusOfProduct(position, favoriteProduct.id)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        timer?.cancel()
        timer = null
    }
}