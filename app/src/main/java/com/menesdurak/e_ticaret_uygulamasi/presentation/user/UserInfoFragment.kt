package com.menesdurak.e_ticaret_uygulamasi.presentation.user

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.menesdurak.e_ticaret_uygulamasi.data.remote.dto.UserInfo
import com.menesdurak.e_ticaret_uygulamasi.databinding.FragmentUserInfoBinding
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class UserInfoFragment : Fragment() {
    private var _binding: FragmentUserInfoBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    private lateinit var storageRef: StorageReference
    private lateinit var storageRef2: StorageReference

    private lateinit var imagesRef: StorageReference

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

        // Create a storage reference from our app
        storageRef = Firebase.storage.reference
        storageRef2 = FirebaseStorage.getInstance().reference.child("images/${auth.currentUser!!.uid}.jpg")
        imagesRef = storageRef.child("images/${auth.currentUser!!.uid}.jpg")

        val localFile = File.createTempFile("tempImage", "jpg")
        storageRef2.getFile(localFile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            binding.ivUserImage.setImageBitmap(bitmap)
        }

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

        val changeImage =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == Activity.RESULT_OK) {
                    val data = it.data
                    val imgUri = data?.data
                    imagesRef.putFile(imgUri!!)
                    binding.ivUserImage.setImageURI(imgUri)
                }
            }

        binding.ivChangeImage.setOnClickListener {
            val pickImg = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            changeImage.launch(pickImg)
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