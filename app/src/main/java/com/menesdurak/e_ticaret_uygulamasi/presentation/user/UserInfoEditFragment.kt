package com.menesdurak.e_ticaret_uygulamasi.presentation.user

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
import com.menesdurak.e_ticaret_uygulamasi.R
import com.menesdurak.e_ticaret_uygulamasi.data.remote.dto.UserInfo
import com.menesdurak.e_ticaret_uygulamasi.databinding.FragmentUserInfoEditBinding

class UserInfoEditFragment : Fragment() {
    private var _binding: FragmentUserInfoEditBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    private lateinit var databaseReference: DatabaseReference

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
        _binding = FragmentUserInfoEditBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        databaseReference = Firebase.database.reference

        databaseReference.child(auth.currentUser?.uid!!).child("userInfo").get()
            .addOnSuccessListener {
                val userInfo = UserInfo("", "", "", "")
                userInfo.name = it.getValue<UserInfo>()!!.name
                userInfo.surName = it.getValue<UserInfo>()!!.surName
                userInfo.address = it.getValue<UserInfo>()!!.address
                userInfo.phone = it.getValue<UserInfo>()!!.phone

                binding.etUserName.setText(userInfo.name)
                binding.etUserSurname.setText(userInfo.surName)
                binding.etUserAddress.setText(userInfo.address)
                binding.etUserPhone.setText(userInfo.phone)

            }

        binding.btnSave.setOnClickListener {
            val name = binding.etUserName.text.toString()
            val surName = binding.etUserSurname.text.toString()
            val address = binding.etUserAddress.text.toString()
            val phone = binding.etUserPhone.text.toString()

            val userInfo = UserInfo(name, surName, address, phone)

            databaseReference.child(auth.currentUser?.uid!!).child("userInfo").setValue(userInfo)
            val action = UserInfoEditFragmentDirections.actionUserInfoEditFragmentToUserFragment()
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}