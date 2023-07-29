package com.menesdurak.e_ticaret_uygulamasi.presentation.orders

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.menesdurak.e_ticaret_uygulamasi.R
import com.menesdurak.e_ticaret_uygulamasi.data.remote.dto.BoughtProductFirebase
import com.menesdurak.e_ticaret_uygulamasi.databinding.FragmentOrdersBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

class OrdersFragment : Fragment() {
    private var _binding: FragmentOrdersBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    private lateinit var databaseReference: DatabaseReference

    private val orderList = mutableListOf<BoughtProductFirebase>()

    private lateinit var boughtProduct: BoughtProductFirebase

    private var counter = 1

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

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                val orderDataSnapshot = dataSnapshot.getValue<BoughtProductFirebase>()
                boughtProduct = BoughtProductFirebase()
                boughtProduct.category = orderDataSnapshot!!.category
                boughtProduct.id = orderDataSnapshot.id
                boughtProduct.image = orderDataSnapshot.image
                boughtProduct.price = orderDataSnapshot.price
                boughtProduct.title = orderDataSnapshot.title
                boughtProduct.amount = orderDataSnapshot.amount
                boughtProduct.isDelivered = orderDataSnapshot.isDelivered
                boughtProduct.address = orderDataSnapshot.address
                boughtProduct.orderDate = orderDataSnapshot.orderDate
                orderList.add(boughtProduct)

                when (counter) {
                    1 -> {
                        binding.tvTitle1.text = boughtProduct.title
                        binding.tvOrderDate1.text = boughtProduct.orderDate
                        binding.tvOrderNumber1.text = dataSnapshot.key
                        if (boughtProduct.isDelivered!!) {
                            binding.ib1.setBackgroundColor(resources.getColor(R.color.green, null))
                            binding.tvDeliveredStatus1.text = getString(R.string.delivered)
                            binding.tvDeliveredStatus1.setTextColor(
                                resources.getColor(
                                    R.color.green,
                                    null
                                )
                            )
                        } else {
                            binding.ib1.setBackgroundColor(resources.getColor(R.color.red, null))
                            binding.tvDeliveredStatus1.text = getString(R.string.not_delivered)
                            binding.tvDeliveredStatus1.setTextColor(
                                resources.getColor(
                                    R.color.red,
                                    null
                                )
                            )
                        }
                        counter++
                    }

                    2 -> {
                        binding.horizontalLineFirst.visibility = View.VISIBLE
                        binding.tvTitle2.text = boughtProduct.title
                        binding.tvOrderDate2.text = boughtProduct.orderDate
                        binding.tvOrderNumber2.text = dataSnapshot.key
                        if (boughtProduct.isDelivered!!) {
                            binding.ib2.setBackgroundColor(resources.getColor(R.color.green, null))
                            binding.tvDeliveredStatus2.text = getString(R.string.delivered)
                            binding.tvDeliveredStatus2.setTextColor(
                                resources.getColor(
                                    R.color.green,
                                    null
                                )
                            )
                        } else {
                            binding.ib2.setBackgroundColor(resources.getColor(R.color.red, null))
                            binding.tvDeliveredStatus2.text = getString(R.string.not_delivered)
                            binding.tvDeliveredStatus2.setTextColor(
                                resources.getColor(
                                    R.color.red,
                                    null
                                )
                            )
                        }
                        counter++
                    }

                    3 -> {
                        binding.horizontalLineSecond.visibility = View.VISIBLE
                        binding.tvTitle3.text = boughtProduct.title
                        binding.tvOrderDate3.text = boughtProduct.orderDate
                        binding.tvOrderNumber3.text = dataSnapshot.key
                        if (boughtProduct.isDelivered!!) {
                            binding.ib3.setBackgroundColor(resources.getColor(R.color.green, null))
                            binding.tvDeliveredStatus3.text = getString(R.string.delivered)
                            binding.tvDeliveredStatus3.setTextColor(
                                resources.getColor(
                                    R.color.green,
                                    null
                                )
                            )
                        } else {
                            binding.ib3.setBackgroundColor(resources.getColor(R.color.red, null))
                            binding.tvDeliveredStatus3.text = getString(R.string.not_delivered)
                            binding.tvDeliveredStatus3.setTextColor(
                                resources.getColor(
                                    R.color.red,
                                    null
                                )
                            )
                        }
                        counter++
                    }

                    4 -> {
                        binding.horizontalLineThird.visibility = View.VISIBLE
                        binding.tvTitle4.text = boughtProduct.title
                        binding.tvOrderDate4.text = boughtProduct.orderDate
                        binding.tvOrderNumber4.text = dataSnapshot.key
                        if (boughtProduct.isDelivered!!) {
                            binding.ib4.setBackgroundColor(resources.getColor(R.color.green, null))
                            binding.tvDeliveredStatus4.text = getString(R.string.delivered)
                            binding.tvDeliveredStatus4.setTextColor(
                                resources.getColor(
                                    R.color.green,
                                    null
                                )
                            )
                        } else {
                            binding.ib4.setBackgroundColor(resources.getColor(R.color.red, null))
                            binding.tvDeliveredStatus4.text = getString(R.string.not_delivered)
                            binding.tvDeliveredStatus4.setTextColor(
                                resources.getColor(
                                    R.color.red,
                                    null
                                )
                            )
                        }
                        counter++
                    }
                    5 -> {
                        binding.horizontalLineFourth.visibility = View.VISIBLE
                        binding.tvTitle5.text = boughtProduct.title
                        binding.tvOrderDate5.text = boughtProduct.orderDate
                        binding.tvOrderNumber5.text = dataSnapshot.key
                        if (boughtProduct.isDelivered!!) {
                            binding.ib5.setBackgroundColor(resources.getColor(R.color.green, null))
                            binding.tvDeliveredStatus5.text = getString(R.string.delivered)
                            binding.tvDeliveredStatus5.setTextColor(
                                resources.getColor(
                                    R.color.green,
                                    null
                                )
                            )
                        } else {
                            binding.ib5.setBackgroundColor(resources.getColor(R.color.red, null))
                            binding.tvDeliveredStatus5.text = getString(R.string.not_delivered)
                            binding.tvDeliveredStatus5.setTextColor(
                                resources.getColor(
                                    R.color.red,
                                    null
                                )
                            )
                        }
                        counter = 1
                    }

                }

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("12345", "loadPost:onCancelled", databaseError.toException())
            }
        }

        ordersReference.get().addOnSuccessListener {
            for (first in it.children) {
                ordersReference.child(first.key!!).get().addOnSuccessListener {
                    for (second in it.children) {
                        ordersReference.child(first.key!!).child(second.key!!)
                            .addValueEventListener(postListener)
                    }
                }
            }
        }

//        ordersReference.get().addOnSuccessListener {
//            for (first in it.children) {
//                ordersReference.child(first.key!!).get().addOnSuccessListener {
//                    for (second in it.children) {
//                        ordersReference.child(first.key!!).child(second.key!!).get()
//                            .addOnSuccessListener { orderDataSnapshot ->
//                                boughtProduct = BoughtProductFirebase()
//                                boughtProduct.category =
//                                    orderDataSnapshot.getValue<BoughtProductFirebase>()!!.category
//                                boughtProduct.id =
//                                    orderDataSnapshot.getValue<BoughtProductFirebase>()!!.id
//                                boughtProduct.image =
//                                    orderDataSnapshot.getValue<BoughtProductFirebase>()!!.image
//                                boughtProduct.price =
//                                    orderDataSnapshot.getValue<BoughtProductFirebase>()!!.price
//                                boughtProduct.title =
//                                    orderDataSnapshot.getValue<BoughtProductFirebase>()!!.title
//                                boughtProduct.amount =
//                                    orderDataSnapshot.getValue<BoughtProductFirebase>()!!.amount
//                                boughtProduct.isDelivered =
//                                    orderDataSnapshot.getValue<BoughtProductFirebase>()!!.isDelivered
//                                boughtProduct.address =
//                                    orderDataSnapshot.getValue<BoughtProductFirebase>()!!.address
//                                boughtProduct.orderDate =
//                                    orderDataSnapshot.getValue<BoughtProductFirebase>()!!.orderDate
//                            }
//                    }
//                }
//            }
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}