package com.femi.billreminder.ui.main.all

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.femi.billreminder.R
import com.femi.billreminder.database.entity.Bill
import com.femi.billreminder.databinding.ItemBillsBinding
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textview.MaterialTextView
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class AllBillsAdapter(
    private val context: Context?,
    private val showDetail: (Bill) -> Unit,
) : ListAdapter<Bill, AllBillsAdapter.BillsViewHolder>(COMPARATOR) {

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<Bill>() {
            override fun areItemsTheSame(oldItem: Bill, newItem: Bill): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Bill, newItem: Bill): Boolean {
                return ((oldItem.id == oldItem.id) &&
                        (oldItem.title == newItem.title) &&
                        (oldItem.content == oldItem.content) &&
                        (oldItem.amount == oldItem.amount) &&
                        (oldItem.remaining == oldItem.remaining) &&
                        (oldItem.date == oldItem.date) &&
                        (oldItem.paid == newItem.paid))
            }
        }
    }

    inner class BillsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val contentBinding = ItemBillsBinding.bind(itemView)
        val cvContainer: MaterialCardView = contentBinding.cvContainer
        val tvTitle: MaterialTextView = contentBinding.tvTitle
        val tvContent: MaterialTextView = contentBinding.tvContent
        val tvAmount: MaterialTextView = contentBinding.tvAmount
        val tvDate: MaterialTextView = contentBinding.tvDate
        val tvPaid: MaterialTextView = contentBinding.tvPaid
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllBillsAdapter.BillsViewHolder {
        return BillsViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_bills, parent, false)
        )
    }


    override fun onBindViewHolder(holder: AllBillsAdapter.BillsViewHolder, position: Int) {
        getItem(position).let { bill ->
            holder.apply {

                val localeID = Locale.getDefault()
                val formatCurrency = NumberFormat.getCurrencyInstance(localeID)
                val amount = formatCurrency.format(bill.amount.toDouble())
                val sdf = SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault())
                val date = sdf.format(bill.date)

                tvTitle.text = bill.title
                tvContent.text = bill.content
                tvAmount.text = amount
                tvDate.text = date

                val paid = bill.paid.toBoolean()
                context?.let { context
                    if (paid) {
                        tvPaid.setTextColor(
                            ContextCompat.getColor(
                                context,
                                R.color.colorGreen
                            )
                        )
                    } else {
                        tvPaid.setTextColor(
                            ContextCompat.getColor(
                                context,
                                R.color.colorYellow
                            )
                        )
                    }
                }

                tvPaid.text =
                    if (paid) context?.getString(R.string.paid) else context?.getString(R.string.unpaid)

                cvContainer.setOnClickListener {
                    showDetail(bill)
                }

            }
        }
    }

}