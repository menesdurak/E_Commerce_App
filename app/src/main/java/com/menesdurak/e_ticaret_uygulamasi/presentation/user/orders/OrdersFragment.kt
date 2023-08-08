package com.menesdurak.e_ticaret_uygulamasi.presentation.user.orders

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.menesdurak.e_ticaret_uygulamasi.data.remote.dto.BoughtProduct
import com.menesdurak.e_ticaret_uygulamasi.databinding.FragmentOrdersBinding
import com.menesdurak.e_ticaret_uygulamasi.presentation.user.my_credit_cards.UserCreditCardAdapter

class OrdersFragment : Fragment() {
    private var _binding: FragmentOrdersBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    private lateinit var databaseReference: DatabaseReference

    private val orderList = mutableListOf<BoughtProduct>()

    private val orderAdapter: OrdersAdapter by lazy { OrdersAdapter() }

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
        _binding = FragmentOrdersBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        databaseReference = Firebase.database.reference

        val ordersReference = databaseReference.child(auth.currentUser?.uid!!).child("orders")

        with(binding.recyclerView) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = orderAdapter
        }

        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (postSnapshot in snapshot.children) {
                    val orderItem = convertDataSnapshotToBoughtProductFirebase(postSnapshot)
                    orderList.add(orderItem!!)
                }
                orderAdapter.updateList(orderList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("12345", "loadPost:onCancelled", databaseError.toException())
            }
        }

        ordersReference.get().addOnSuccessListener {
            for (first in it.children) {
                ordersReference.child(first.key!!).addValueEventListener(postListener)
            }
        }

    }

    private fun convertDataSnapshotToBoughtProductFirebase(dataSnapshot: DataSnapshot): BoughtProduct? {
        val address = dataSnapshot.child("address").getValue(String::class.java)
        val amount = dataSnapshot.child("amount").getValue(Int::class.java)
        val category = dataSnapshot.child("category").getValue(String::class.java)
        val creditCardNumber = dataSnapshot.child("creditCardNumber").getValue(String::class.java)
        val isDelivered = dataSnapshot.child("delivered").getValue(Boolean::class.java)
        val description = dataSnapshot.child("description").getValue(String::class.java)
        val id = dataSnapshot.child("id").getValue(Int::class.java)
        val image = dataSnapshot.child("image").getValue(String::class.java)
        val orderDate = dataSnapshot.child("orderDate").getValue(String::class.java)
        val orderNumber = dataSnapshot.child("orderNumber").getValue(String::class.java)
        val price = dataSnapshot.child("price").getValue(String::class.java)
        val title = dataSnapshot.child("title").getValue(String::class.java)
        return BoughtProduct(
            address = address,
            amount = amount,
            category = category,
            creditCardNumber = creditCardNumber,
            isDelivered = isDelivered,
            description = description,
            id = id,
            image = image,
            orderDate = orderDate,
            orderNumber = orderNumber,
            price = price,
            title = title
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}