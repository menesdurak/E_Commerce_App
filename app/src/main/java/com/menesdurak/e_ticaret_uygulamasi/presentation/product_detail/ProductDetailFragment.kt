package com.menesdurak.e_ticaret_uygulamasi.presentation.product_detail

import android.graphics.Paint
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.menesdurak.e_ticaret_uygulamasi.R
import com.menesdurak.e_ticaret_uygulamasi.common.Resource
import com.menesdurak.e_ticaret_uygulamasi.common.addCurrencySign
import com.menesdurak.e_ticaret_uygulamasi.common.round
import com.menesdurak.e_ticaret_uygulamasi.data.mapper.ProductToFavoriteProductMapper
import com.menesdurak.e_ticaret_uygulamasi.data.mapper.ProductToProductUiMapper
import com.menesdurak.e_ticaret_uygulamasi.data.mapper.ProductUiToCartProductMapper
import com.menesdurak.e_ticaret_uygulamasi.data.remote.dto.Product
import com.menesdurak.e_ticaret_uygulamasi.databinding.FragmentProductDetailBinding
import com.menesdurak.e_ticaret_uygulamasi.presentation.cart.CartViewModel
import com.menesdurak.e_ticaret_uygulamasi.presentation.categories.CategoriesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductDetailFragment : Fragment() {
    private var _binding: FragmentProductDetailBinding? = null
    private val binding get() = _binding!!

    private val categoriesViewModel: CategoriesViewModel by viewModels()

    private val cartViewModel: CartViewModel by viewModels()

    private var productId = -1

    private var discountRate = 0.0f

    private var isDiscounted = false

    private lateinit var product: Product

    private var isFavorite = false

    private var timer: CountDownTimer? = null

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
        discountRate = args.discountRate
        isDiscounted = args.isDiscounted

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
                    binding.tvProductName.text = it.data.title
                    with(binding.ratingBar) {
                        visibility = View.VISIBLE
                        rating = it.data.rating.rate.toFloat()
                    }
                    binding.tvRatingCount.text =
                        it.data.rating.count.toString() + " " + getString(R.string.review)
                    binding.tvRatingRate.text = it.data.rating.rate.toString()
                    if (isDiscounted) {
                        with(binding.tvPrice) {
                            text = (it.data.price.toDouble() round 2).addCurrencySign()
                            setTextColor(resources.getColor(R.color.red, null))
                            paintFlags = this.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                        }
                        with(binding.tvPriceDiscounted) {
                            visibility = View.VISIBLE
                            text = ((it.data.price.toDouble() * discountRate) round 2) .addCurrencySign()
                            setTextColor(resources.getColor(R.color.green, null))
                        }
                    } else {
                        with(binding.tvPrice) {
                            text = (it.data.price.toDouble() round 2).addCurrencySign()
                            textSize = 36f
                        }
                    }

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
                ProductUiToCartProductMapper(discountRate).map(
                    ProductToProductUiMapper().map(
                        product
                    )
                )
            )

            binding.btnBuy.setBackgroundColor(binding.root.resources.getColor(R.color.sub2, null))
            binding.btnBuy.text = getString(R.string.in_cart)
            timer = object : CountDownTimer(1000, 50) {
                override fun onTick(millisUntilFinished: Long) {

                }

                override fun onFinish() {
                    binding.btnBuy.setBackgroundColor(
                        binding.root.resources.getColor(
                            R.color.main,
                            null
                        )
                    )
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
        timer?.cancel()
        timer = null
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