package com.menesdurak.e_ticaret_uygulamasi.presentation.product_detail

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.menesdurak.e_ticaret_uygulamasi.R
import com.menesdurak.e_ticaret_uygulamasi.common.Resource
import com.menesdurak.e_ticaret_uygulamasi.common.addCurrencySign
import com.menesdurak.e_ticaret_uygulamasi.data.mapper.ProductToFavoriteProductMapper
import com.menesdurak.e_ticaret_uygulamasi.data.mapper.ProductToProductUiMapper
import com.menesdurak.e_ticaret_uygulamasi.data.mapper.ProductUiToCartProductMapper
import com.menesdurak.e_ticaret_uygulamasi.data.remote.dto.Product
import com.menesdurak.e_ticaret_uygulamasi.databinding.FragmentProductDetailBinding
import com.menesdurak.e_ticaret_uygulamasi.presentation.cart.CartViewModel
import com.menesdurak.e_ticaret_uygulamasi.presentation.categories.CategoriesViewModel
import com.menesdurak.e_ticaret_uygulamasi.presentation.favorites.FavoritesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductDetailFragment : Fragment() {
    private var _binding: FragmentProductDetailBinding? = null
    private val binding get() = _binding!!

    private val categoriesViewModel: CategoriesViewModel by viewModels()

    private val cartViewModel: CartViewModel by viewModels()

    private val favoritesViewModel: FavoritesViewModel by viewModels()

    private var productId = -1

    private lateinit var product: Product

    private var isFavorite = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
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

        categoriesViewModel.getAllFavoriteProductsId()

        categoriesViewModel.product.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    product = it.data
                    binding.progressBar.visibility = View.GONE
                    binding.linlayIndicator.visibility = View.VISIBLE
                    binding.linlayBottomContainer.visibility = View.VISIBLE
                    binding.ratingBar.visibility = View.VISIBLE
                    binding.tvProductName.text = it.data.title
                    binding.ratingBar.rating = it.data.rating.rate.toFloat()
                    binding.tvRatingCount.text =
                        it.data.rating.count.toString() + " " + getString(R.string.review)
                    binding.tvRatingRate.text = it.data.rating.rate.toString()
                    binding.tvPrice.text = it.data.price.toDouble().addCurrencySign()
                    binding.tvProductDetails.text = it.data.description

                    val imageList = listOf(it.data.image, it.data.image, it.data.image)
                    val viewPagerAdapter = ImageViewPagerAdapter(imageList)
                    binding.viewPager.adapter = viewPagerAdapter

                    binding.viewPager.registerOnPageChangeCallback(object :
                        ViewPager2.OnPageChangeCallback() {
                        override fun onPageScrolled(
                            position: Int,
                            positionOffset: Float,
                            positionOffsetPixels: Int,
                        ) {
                            changeIndicatorColor()
                            super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                        }

                        override fun onPageScrollStateChanged(state: Int) {
                            super.onPageScrollStateChanged(state)
                            changeIndicatorColor()
                        }

                    })
                }

                is Resource.Error -> {
                    Log.e("error", "Error in Product Detail Fragment")
                }

                Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.linlayIndicator.visibility = View.GONE
                    binding.linlayBottomContainer.visibility = View.GONE
                    binding.ratingBar.visibility = View.GONE
                }
            }
        }

        categoriesViewModel.favoriteProductsIdList.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    if (productId in it.data) {
                        isFavorite = true
                        binding.ivFavorite.setImageResource(R.drawable.ic_favorite)
                    } else {
                        isFavorite = false
                        binding.ivFavorite.setImageResource(R.drawable.ic_favorite_empty)
                    }
                }

                is Resource.Error -> {
                    Log.e("error", "Error in Product Detail Fragment")
                }

                Resource.Loading -> {

                }
            }
        }

        binding.btnBuy.setOnClickListener {
            cartViewModel.addCartProduct(
                ProductUiToCartProductMapper().map(
                    ProductToProductUiMapper().map(
                        product
                    )
                )
            )

            binding.btnBuy.setBackgroundColor(binding.root.resources.getColor(R.color.sub2, null))
            binding.btnBuy.text = getString(R.string.in_cart)
            val timer = object : CountDownTimer(1000, 50) {
                override fun onTick(millisUntilFinished: Long) {

                }

                override fun onFinish() {
                    binding.btnBuy.setBackgroundColor(binding.root.resources.getColor(R.color.main, null))
                    binding.btnBuy.text = getString(R.string.buy)
                }

            }.start()
        }

        binding.ivFavorite.setOnClickListener {
            if (isFavorite) {
                categoriesViewModel.deleteFavoriteProduct(productId)
                binding.ivFavorite.setImageResource(R.drawable.ic_favorite_empty)
            } else {
                categoriesViewModel.addFavoriteProduct(ProductToFavoriteProductMapper().map(product))
                binding.ivFavorite.setImageResource(R.drawable.ic_favorite)
            }
            isFavorite = !isFavorite
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun changeIndicatorColor() {
        when (binding.viewPager.currentItem) {
            0 -> {
                binding.firstIndicator.backgroundTintList =
                    AppCompatResources.getColorStateList(requireContext(), R.color.sub2)
                binding.secondIndicator.backgroundTintList =
                    AppCompatResources.getColorStateList(requireContext(), R.color.gray)
                binding.thirdIndicator.backgroundTintList =
                    AppCompatResources.getColorStateList(requireContext(), R.color.gray)
            }

            1 -> {
                binding.firstIndicator.backgroundTintList =
                    AppCompatResources.getColorStateList(requireContext(), R.color.gray)
                binding.secondIndicator.backgroundTintList =
                    AppCompatResources.getColorStateList(requireContext(), R.color.sub2)
                binding.thirdIndicator.backgroundTintList =
                    AppCompatResources.getColorStateList(requireContext(), R.color.gray)
            }

            2 -> {
                binding.firstIndicator.backgroundTintList =
                    AppCompatResources.getColorStateList(requireContext(), R.color.gray)
                binding.secondIndicator.backgroundTintList =
                    AppCompatResources.getColorStateList(requireContext(), R.color.gray)
                binding.thirdIndicator.backgroundTintList =
                    AppCompatResources.getColorStateList(requireContext(), R.color.sub2)
            }
        }
    }
}