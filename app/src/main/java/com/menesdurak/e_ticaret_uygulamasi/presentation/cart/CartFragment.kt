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
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.menesdurak.e_ticaret_uygulamasi.R
import com.menesdurak.e_ticaret_uygulamasi.common.Resource
import com.menesdurak.e_ticaret_uygulamasi.common.addCurrencySign
import com.menesdurak.e_ticaret_uygulamasi.common.round
import com.menesdurak.e_ticaret_uygulamasi.data.local.entity.CartProduct
import com.menesdurak.e_ticaret_uygulamasi.data.mapper.CartProductListToBoughtProductListMapper
import com.menesdurak.e_ticaret_uygulamasi.databinding.FragmentCartBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartFragment : Fragment() {
    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    private lateinit var databaseReference: DatabaseReference

    private val cartViewModel: CartViewModel by viewModels()

    private val cartProductAdapter: CartProductAdapter by lazy {
        CartProductAdapter(
            ::onCheckboxClicked,
            ::onDecreaseClicked,
            ::onIncreaseClicked
        )
    }

    private var totalPrice: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase Auth
        auth = Firebase.auth

        totalPrice = 0.0
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        val view = binding.root

        databaseReference = Firebase.database.reference

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cartViewModel.getAllCartProducts()

        totalPrice = 0.0

        binding.rvCart.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = cartProductAdapter
        }

        cartViewModel.cartProductList.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    cartProductAdapter.updateList(it.data)

                    it.data.forEach {cartProduct ->
                        if (cartProduct.isChecked) {
                            totalPrice += cartProduct.price.toDouble() * cartProduct.amount
                            binding.tvTotalPrice.text = (totalPrice round 2).toString().toDouble().addCurrencySign()
                            binding.linlayPriceContainer.visibility = View.VISIBLE
                        }
                    }
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
                    totalPrice = 0.0
                    binding.linlayPriceContainer.visibility = View.GONE
                })
                .setNegativeButton("No", DialogInterface.OnClickListener { dialog, which -> })
            builder.create()
            builder.show()
        }

        binding.btnBuy.setOnClickListener {
            onCheckoutClicked()
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

        binding.tvTotalPrice.text = (totalPrice round 2).toString().toDouble().addCurrencySign()
    }

    private fun onDecreaseClicked(position: Int, cartProduct: CartProduct) {

        if (cartProduct.amount != 1) {
            cartViewModel.updateCartProductAmount((cartProduct.amount - 1), cartProduct.id)
            cartProductAdapter.decreaseAmount(position, cartProduct)
            if (totalPrice > 0.0 && cartProduct.isChecked) {
                totalPrice -= cartProduct.price.toDouble()
                binding.tvTotalPrice.text = (totalPrice round 2).toString().toDouble().addCurrencySign()
            }
        } else {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Delete")
            builder.setMessage("Do you want to delete this item from your cart?")
                .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which ->
                    cartViewModel.deleteCartProduct(cartProduct.id)
                    cartProductAdapter.removeItem(cartProduct)
                    totalPrice -= cartProduct.price.toDouble()
                    binding.tvTotalPrice.text = (totalPrice round 2).toString().toDouble().addCurrencySign()
                    if (totalPrice <= 0.0) {
                        binding.linlayPriceContainer.visibility = View.GONE
                    }
                })
                .setNegativeButton("No", DialogInterface.OnClickListener { dialog, which -> })
            builder.create()
            builder.show()
        }
    }

    private fun onIncreaseClicked(position: Int, cartProduct: CartProduct) {
        cartViewModel.updateCartProductAmount((cartProduct.amount + 1), cartProduct.id)
        cartProductAdapter.increaseAmount(position, cartProduct)

        if (cartProduct.isChecked) {
            totalPrice += cartProduct.price.toDouble()
            binding.tvTotalPrice.text = (totalPrice round 2).toString().toDouble().addCurrencySign()
        }

    }

    private fun onCheckoutClicked() {
        if (auth.currentUser == null) {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("You need to login")
            builder.setMessage("You need to login to buy your products in your cart. Do you want to go to login page?")
                .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which ->
                    val action = CartFragmentDirections.actionCartFragmentToUserLogInFragment()
                    val bottomNavMenu = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavMenu)
                    bottomNavMenu.menu.getItem(4).isChecked = true
                    findNavController().navigate(action)
                })
                .setNegativeButton("No", DialogInterface.OnClickListener { dialog, which -> })
            builder.create()
            builder.show()
        } else {
            val action = CartFragmentDirections.actionCartFragmentToPaymentFragment()
            findNavController().navigate(action)
        }
    }
}