package com.menesdurak.e_ticaret_uygulamasi.presentation.user.orders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.menesdurak.e_ticaret_uygulamasi.R
import com.menesdurak.e_ticaret_uygulamasi.common.addCurrencySign
import com.menesdurak.e_ticaret_uygulamasi.data.remote.dto.BoughtProduct
import com.menesdurak.e_ticaret_uygulamasi.databinding.ItemOrderBinding
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date

class OrdersAdapter : RecyclerView.Adapter<OrdersAdapter.OrderHolder>() {

    private val items = mutableListOf<BoughtProduct>()

    inner class OrderHolder(private val binding: ItemOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(order: BoughtProduct) {
            binding.tvDateAndMonth.text = convertToDate(order.orderDate!!)
            binding.tvDayAndTime.text = convertToTime(order.orderDate!!)
            binding.tvPrice.text = (order.price!!.toDouble() * order.amount!!).addCurrencySign()
            binding.tvOrderNumber.text = order.orderNumber

            Glide
                .with(binding.root.context)
                .load(order.image)
                .placeholder(R.drawable.loading_200x200)
                .into(binding.ivProduct)

            if (order.isDelivered!!) {
                binding.ib.setBackgroundColor(binding.root.resources.getColor(R.color.green, null))
                binding.tvDeliveredStatus.text = binding.root.context.getString(R.string.delivered)
                binding.tvDeliveredStatus.setTextColor(
                    binding.root.resources.getColor(
                        R.color.green,
                        null
                    )
                )
            } else {
                binding.ib.setBackgroundColor(binding.root.resources.getColor(R.color.red, null))
                binding.tvDeliveredStatus.text =
                    binding.root.context.getString(R.string.not_delivered)
                binding.tvDeliveredStatus.setTextColor(
                    binding.root.resources.getColor(
                        R.color.red,
                        null
                    )
                )
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHolder {
        val bind =
            ItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderHolder(bind)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: OrderHolder, position: Int) {
        holder.bind(items[items.size - position - 1])
    }

    fun updateList(newList: List<BoughtProduct>) {
        items.clear()
        items.addAll(newList)
        notifyDataSetChanged()
    }

    fun convertToDate(time: String): String? {
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm")
        var convertedDate: Date? = null
        var formattedDate: String? = null
        try {
            convertedDate = formatter.parse(time)
            formattedDate = SimpleDateFormat("dd MMM yy").format(convertedDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return formattedDate
    }

    fun convertToTime(time: String): String? {
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm")
        var convertedTime: Date? = null
        var formattedTime: String? = null
        try {
            convertedTime = formatter.parse(time)
            formattedTime = SimpleDateFormat("HH:mm").format(convertedTime)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return formattedTime
    }
}