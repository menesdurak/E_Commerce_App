package com.menesdurak.e_ticaret_uygulamasi.presentation.user

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.menesdurak.e_ticaret_uygulamasi.data.remote.dto.UserInfo
import com.menesdurak.e_ticaret_uygulamasi.databinding.FragmentUserInfoBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserInfoFragment : Fragment() {
    private var _binding: FragmentUserInfoBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    private lateinit var databaseReference: DatabaseReference

    private lateinit var userInfo: UserInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase Auth
        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentUserInfoBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.progressBar.visibility = View.VISIBLE

        databaseReference = Firebase.database.reference

        databaseReference.child(auth.currentUser!!.uid).child("userInfo").get()
            .addOnSuccessListener {
                userInfo = UserInfo("", "", "", "")
                userInfo.name = it.getValue<UserInfo>()!!.name ?: ""
                userInfo.surName = it.getValue<UserInfo>()!!.surName ?: ""
                userInfo.address = it.getValue<UserInfo>()!!.address ?: ""
                userInfo.phone = it.getValue<UserInfo>()!!.phone ?: ""

                binding.progressBar.visibility = View.GONE

                binding.tvUserNameInfo.text = userInfo.name
                binding.tvUserSurNameInfo.text = userInfo.surName
                binding.tvUserAddressInfo.text = userInfo.address
                binding.tvUserPhoneInfo.text = userInfo.phone

            }

        binding.ivLogOutUser.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Sign Out")
            builder.setMessage("Do you want to sign out?")
                .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which ->
                    signOutUser()
                    val action = UserInfoFragmentDirections.actionUserInfoFragmentToUserLogInFragment()
                    findNavController().navigate(action)
                })
                .setNegativeButton("No", DialogInterface.OnClickListener { dialog, which -> })
            builder.create()
            builder.show()

        }

        binding.ivEditUserInfo.setOnClickListener {
            val action = UserInfoFragmentDirections.actionUserInfoFragmentToUserInfoEditFragment()
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun signOutUser() {
        Firebase.auth.signOut()
    }
}