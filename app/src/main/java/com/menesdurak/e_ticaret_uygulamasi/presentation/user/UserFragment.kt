package com.menesdurak.e_ticaret_uygulamasi.presentation.user

import android.app.AlertDialog
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.ktx.storage
import com.menesdurak.e_ticaret_uygulamasi.R
import com.menesdurak.e_ticaret_uygulamasi.common.AppLanguageProvider
import com.menesdurak.e_ticaret_uygulamasi.common.Resource
import com.menesdurak.e_ticaret_uygulamasi.databinding.FragmentUserBinding
import com.menesdurak.e_ticaret_uygulamasi.presentation.cart.CartViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.util.Locale

@AndroidEntryPoint
class UserFragment : Fragment() {
    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!

    private val cartViewModel: CartViewModel by viewModels()

    private lateinit var auth: FirebaseAuth

    private lateinit var storageRef: StorageReference
    private lateinit var storageRef2: StorageReference

    private lateinit var imagesRef: StorageReference

    private lateinit var bottomNavView: BottomNavigationView

    private var imageStorageTask : StorageTask<FileDownloadTask.TaskSnapshot>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentUserBinding.inflate(inflater, container, false)
        val view = binding.root

        bottomNavView = requireActivity().findViewById(R.id.bottomNavMenu)
        bottomNavView.menu.getItem(4).isChecked = true

        // Initialize Firebase Auth
        auth = Firebase.auth
        // Create a storage reference from our app
        storageRef = Firebase.storage.reference
        storageRef2 = FirebaseStorage.getInstance().reference.child("images/${auth.currentUser!!.uid}.jpg")
        imagesRef = storageRef.child("images/${auth.currentUser!!.uid}.jpg")

        val localFile = File.createTempFile("tempImage", "jpg")
        imageStorageTask = storageRef2.getFile(localFile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            binding.ivUserImage.setImageBitmap(bitmap)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        binding.tvLanguage.setOnClickListener {
            val currentLanguage = Locale.getDefault().language

            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Language")
            val customAlertLayout =
                layoutInflater.inflate(R.layout.alert_dialog_language_picker, null)

            when (currentLanguage) {
                "en" -> {
                    customAlertLayout.findViewById<ImageButton>(R.id.ivCheckEnglish).visibility =
                        View.VISIBLE
                    customAlertLayout.findViewById<ImageButton>(R.id.ivCheckTurkish).visibility =
                        View.GONE
                }

                "tr" -> {
                    customAlertLayout.findViewById<ImageButton>(R.id.ivCheckTurkish).visibility =
                        View.VISIBLE
                    customAlertLayout.findViewById<ImageButton>(R.id.ivCheckEnglish).visibility =
                        View.GONE
                }

                else -> {
                    customAlertLayout.findViewById<ImageButton>(R.id.ivCheckEnglish).visibility =
                        View.VISIBLE
                    customAlertLayout.findViewById<ImageButton>(R.id.ivCheckTurkish).visibility =
                        View.GONE
                }

            }

            builder.setView(customAlertLayout)
            builder.create()
            builder.show()
            customAlertLayout.findViewById<TextView>(R.id.tvTurkish).setOnClickListener {
                AppLanguageProvider(requireContext()).setLocale("tr-TR")
            }
            customAlertLayout.findViewById<TextView>(R.id.tvEnglish).setOnClickListener {
                AppLanguageProvider(requireContext()).setLocale("en-EN")
            }
        }

        binding.tvUserInfo.setOnClickListener {
            val action = UserFragmentDirections.actionUserFragmentToUserInfoFragment()
            findNavController().navigate(action)
        }

        binding.tvMyOrders.setOnClickListener {
            val action = UserFragmentDirections.actionUserFragmentToOrdersFragment()
            findNavController().navigate(action)
        }

        binding.tvMyCreditCards.setOnClickListener {
            val action = UserFragmentDirections.actionUserFragmentToUserCreditCardFragment()
            findNavController().navigate(action)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        imageStorageTask?.cancel()
        imageStorageTask = null
    }

}