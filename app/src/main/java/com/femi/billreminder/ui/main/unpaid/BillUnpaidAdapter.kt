package com.femi.billreminder.ui.main.unpaid

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.femi.billreminder.R
import com.femi.billreminder.database.entity.Bill
import com.femi.billreminder.databinding.ItemBillUnpaidBinding
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textview.MaterialTextView
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class BillUnpaidAdapter(
    private val checkBill: (Bill) -> Unit,
    private val showDetail: (Bill) -> Unit,
) : ListAdapter<Bill, BillUnpaidAdapter.BillsViewHolder>(COMPARATOR) {

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<Bill>() {
            override fun areItemsTheSame(oldItem: Bill, newItem: Bill): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Bill, newItem: Bill): Boolean {
                return ((oldItem.id == oldItem.id) &&
                        (oldItem.title == newItem.title) &&
                        (oldItem.description == oldItem.description) &&
                        (oldItem.amount == oldItem.amount) &&
                        (oldItem.date == oldItem.date) &&
                        (oldItem.paid == newItem.paid))
            }
        }
    }

    inner class BillsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val contentBinding = ItemBillUnpaidBinding.bind(itemView)
        val cvContainer: MaterialCardView = contentBinding.cvContainer
        val tvTitle: MaterialTextView = contentBinding.tvTitle
        val tvDescription: MaterialTextView = contentBinding.tvDescription
        val tvAmount: MaterialTextView = contentBinding.tvAmount
        val tvDate: MaterialTextView = contentBinding.tvDate
        val btnPaid: MaterialButton = contentBinding.btnPaid
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillsViewHolder {
        return BillsViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_bill_unpaid, parent, false)
        )
    }

    override fun onBindViewHolder(holder: BillsViewHolder, position: Int) {
        getItem(position).let { bill ->
            holder.apply {

                val localeID = Locale.getDefault()
                val formatCurrency = NumberFormat.getCurrencyInstance(localeID)
                val amount = formatCurrency.format(bill.amount.toDouble())
                val sdf = SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault())
                val date = sdf.format(bill.date)

                tvTitle.text = bill.title
                tvDescription.text = bill.description
                tvAmount.text = amount
                tvDate.text = date

                cvContainer.setOnClickListener {
                    showDetail(bill)
                }

                btnPaid.setOnClickListener {
                    checkBill(bill)
                }

            }
        }
    }
}