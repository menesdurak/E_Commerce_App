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
            ::onIncreaseClicked,
            ::onProductClicked
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

        val badge = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavMenu)
            .getOrCreateBadge(R.id.cart)
        badge.isVisible = false

        cartViewModel.cartProductList.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    cartProductAdapter.updateList(it.data)

                    it.data.forEach { cartProduct ->
                        if (cartProduct.isChecked) {
                            totalPrice += cartProduct.price.toDouble() * cartProduct.amount
                            binding.tvTotalPrice.text =
                                (totalPrice round 2).toString().toDouble().addCurrencySign()
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
            builder.setTitle(getString(R.string.delete_all_products))
            builder.setMessage(getString(R.string.do_you_want_to_delete_all_of_your_products_from_cart))
                .setPositiveButton(getString(R.string.yes), DialogInterface.OnClickListener { dialog, which ->
                    cartViewModel.deleteAllCartProducts()
                    cartProductAdapter.updateList(emptyList())
                    totalPrice = 0.0
                    binding.linlayPriceContainer.visibility = View.GONE
                })
                .setNegativeButton(getString(R.string.no), DialogInterface.OnClickListener { dialog, which -> })
            builder.create()
            builder.show()
        }

        binding.btnBuy.setOnClickListener {
            onCheckoutClicked()
        }

        binding.checkboxAll.setOnClickListener {
            if (binding.checkboxAll.isChecked) {
                cartViewModel.updateAllCartProductsToChecked()
                cartProductAdapter.updateCheckedStatusAllChecked()
                totalPrice = cartProductAdapter.calculateTotalPrice()
                binding.tvTotalPrice.text = totalPrice.addCurrencySign()
                binding.linlayPriceContainer.visibility = View.VISIBLE
            } else {
                cartViewModel.updateAllCartProductsToNotChecked()
                cartProductAdapter.updateCheckedStatusAllNotChecked()
                totalPrice = 0.0
                binding.tvTotalPrice.text = totalPrice.addCurrencySign()
                binding.linlayPriceContainer.visibility = View.GONE
            }
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
                binding.tvTotalPrice.text =
                    (totalPrice round 2).toString().toDouble().addCurrencySign()
            }
        } else {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle(getString(R.string.delete))
            builder.setMessage(getString(R.string.do_you_want_to_delete_this_item_from_your_cart))
                .setPositiveButton(getString(R.string.yes), DialogInterface.OnClickListener { dialog, which ->
                    cartViewModel.deleteCartProduct(cartProduct.id)
                    cartProductAdapter.removeItem(cartProduct)
                    totalPrice -= cartProduct.price.toDouble()
                    binding.tvTotalPrice.text =
                        (totalPrice round 2).toString().toDouble().addCurrencySign()
                    if (totalPrice <= 0.0) {
                        binding.linlayPriceContainer.visibility = View.GONE
                    }
                })
                .setNegativeButton(R.string.no, DialogInterface.OnClickListener { dialog, which -> })
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
            builder.setTitle(getString(R.string.you_need_to_login))
            builder.setMessage(getString(R.string.you_need_to_login_to_buy_your_products_in_your_cart_do_you_want_to_go_to_login_page))
                .setPositiveButton(R.string.yes, DialogInterface.OnClickListener { dialog, which ->
                    val action = CartFragmentDirections.actionCartFragmentToUserLogInFragment()
                    val bottomNavMenu =
                        requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavMenu)
                    bottomNavMenu.menu.getItem(4).isChecked = true
                    findNavController().navigate(action)
                })
                .setNegativeButton(R.string.no, DialogInterface.OnClickListener { dialog, which -> })
            builder.create()
            builder.show()
        } else {
            val action = CartFragmentDirections.actionCartFragmentToPaymentFragment()
            findNavController().navigate(action)
        }
    }

    private fun onProductClicked(cartProduct: CartProduct) {
        val action =
            CartFragmentDirections.actionCartFragmentToProductDetailFragment(cartProduct.id)
        findNavController().navigate(action)
    }
}