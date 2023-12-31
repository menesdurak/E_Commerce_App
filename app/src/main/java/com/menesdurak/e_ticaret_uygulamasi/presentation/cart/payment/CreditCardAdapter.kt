package com.menesdurak.e_ticaret_uygulamasi.presentation.cart.payment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.menesdurak.e_ticaret_uygulamasi.R
import com.menesdurak.e_ticaret_uygulamasi.common.hideCreditCardNumber
import com.menesdurak.e_ticaret_uygulamasi.data.local.entity.CreditCardInfo
import com.menesdurak.e_ticaret_uygulamasi.databinding.ItemCreditCardBinding

class CreditCardAdapter(
    private val onCardClicked: (Int, CreditCardInfo) -> Unit,
    private val onCardLongClicked: (Int, CreditCardInfo) -> Unit,
    private val onRadiButtonClicked: (Int, CreditCardInfo) -> Unit
) : RecyclerView.Adapter<CreditCardAdapter.CreditCardHolder>() {

    private val itemList = mutableListOf<CreditCardInfo>()

    var activeItemPosition = -1

    private var counter = 0

    inner class CreditCardHolder(private val binding: ItemCreditCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(creditCard: CreditCardInfo) {
            binding.tvCardNumber.text = creditCard.number.hideCreditCardNumber()
            binding.tvCardHolder.text = creditCard.holderName

            when (counter) {
                0 -> {
                    Glide
                        .with(binding.root.context)
                        .load(R.drawable.credit_card_red)
                        .placeholder(R.drawable.loading_200x200)
                        .into(binding.ivCreditCard)
                    counter++
                }
                1 -> {
                    Glide
                        .with(binding.root.context)
                        .load(R.drawable.credit_card_blue)
                        .placeholder(R.drawable.loading_200x200)
                        .into(binding.ivCreditCard)
                    counter++
                }
                2 -> {
                    Glide
                        .with(binding.root.context)
                        .load(R.drawable.credit_card_purple)
                        .placeholder(R.drawable.loading_200x200)
                        .into(binding.ivCreditCard)
                    counter = 0
                }
            }

            binding.root.setOnClickListener {
                onCardClicked.invoke(adapterPosition, creditCard)
            }

            binding.root.setOnLongClickListener {
                onCardLongClicked.invoke(adapterPosition, creditCard)
                true
            }

            binding.radioButton.setOnClickListener {
                onRadiButtonClicked.invoke(adapterPosition, creditCard)
            }

            if (creditCard.isActive) {
                binding.root.setBackgroundColor(binding.root.resources.getColor(R.color.backgroundSoft, null))
                binding.radioButton.isChecked = true
            } else {
                binding.root.setBackgroundColor(binding.root.resources.getColor(R.color.white, null))
                binding.radioButton.isChecked = false
            }

        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): CreditCardHolder {
        val bind =
            ItemCreditCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CreditCardHolder(bind)
    }

    override fun onBindViewHolder(holder: CreditCardHolder, position: Int) {
        holder.bind(itemList[position])
    }

    override fun getItemCount(): Int  = itemList.size

    fun updateList(newList: List<CreditCardInfo>) {
        itemList.clear()
        itemList.addAll(newList)
        notifyDataSetChanged()
    }

    fun addNewCard(creditCard: CreditCardInfo) {
        itemList.add(creditCard)
        notifyDataSetChanged()
    }

    fun deleteCard(position: Int, creditCard: CreditCardInfo) {
        itemList.removeAt(position)
        notifyDataSetChanged()
    }

    fun activeItem(position: Int) : String {
        if (activeItemPosition == position)
            return itemList[position].number
        if (activeItemPosition != -1) {
            itemList[activeItemPosition].isActive = false
            notifyItemChanged(activeItemPosition)
        }
        activeItemPosition = position
        itemList[activeItemPosition].isActive = true
        notifyItemChanged(position)
        return itemList[position].number
    }
}