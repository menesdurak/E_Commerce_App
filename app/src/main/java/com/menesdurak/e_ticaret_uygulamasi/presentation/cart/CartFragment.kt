package com.menesdurak.e_ticaret_uygulamasi.presentation.cart

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.menesdurak.e_ticaret_uygulamasi.common.Resource
import com.menesdurak.e_ticaret_uygulamasi.data.local.entity.CartProduct
import com.menesdurak.e_ticaret_uygulamasi.databinding.FragmentCartBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartFragment : Fragment() {
    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!

    private val cartViewModel: CartViewModel by viewModels()

    private val cartProductAdapter: CartProductAdapter by lazy {
        CartProductAdapter(
            ::onCheckboxClicked,
            ::onDecreaseClicked,
            ::onIncreaseClicked
        )
    }

    private var totalPrice = 0.0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cartViewModel.getAllCartProducts()

        binding.rvCart.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = cartProductAdapter
        }

        cartViewModel.cartProductList.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    cartProductAdapter.updateList(it.data)
                }

                is Resource.Error -> {
                    Log.e("error", "Error in Cart Fragment")
                }

                Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        }

        binding.ivDelete.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Delete All Products")
            builder.setMessage("Do you want to delete all of your products from cart?")
                .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which ->
                    cartViewModel.deleteAllCartProducts()
                    cartProductAdapter.updateList(emptyList())
                })
                .setNegativeButton("No", DialogInterface.OnClickListener { dialog, which -> })
            builder.create()
            builder.show()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onCheckboxClicked(position: Int, cartProduct: CartProduct) {
        cartViewModel.updateCartProductCheckedStatus(!cartProduct.isChecked, cartProduct.id)
        cartProductAdapter.updateCheckedStatusOfProduct(position, cartProduct)

        if (cartProduct.isChecked) {
            totalPrice += cartProduct.price.toDouble() * cartProduct.amount
        } else {
            totalPrice -= cartProduct.price.toDouble() * cartProduct.amount
        }

        if (totalPrice > 0.0) {
            binding.linlayPriceContainer.visibility = View.VISIBLE
        } else {
            binding.linlayPriceContainer.visibility = View.GONE
        }

        binding.tvTotalPrice.text = totalPrice.toString()
    }

    private fun onDecreaseClicked(position: Int, cartProduct: CartProduct) {
        cartViewModel.updateCartProductAmount((cartProduct.amount - 1), cartProduct.id)
        cartProductAdapter.decreaseAmount(position, cartProduct)
    }

    private fun onIncreaseClicked(position: Int, cartProduct: CartProduct) {
        cartViewModel.updateCartProductAmount((cartProduct.amount + 1), cartProduct.id)
        cartProductAdapter.increaseAmount(position, cartProduct)
    }
}