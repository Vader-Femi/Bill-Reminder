package com.femi.billreminder.ui.bill.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.femi.billreminder.database.BillDatabase
import com.femi.billreminder.database.entity.Bill
import com.femi.billreminder.databinding.ActivityBillDetailBinding
import com.femi.billreminder.repository.BillRepository
import com.femi.billreminder.ui.base.ViewModelFactory
import com.femi.billreminder.utils.BILL_ID
import com.femi.billreminder.utils.RoomConverters
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class BillDetailActivity : AppCompatActivity() {

    private lateinit var billDetailViewModel: BillDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityBillDetailBinding.inflate(layoutInflater)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupViewModel()

        val bundle = intent.extras
        val bill: Bill = bundle?.getSerializable(BILL_ID) as Bill

        val localeID = Locale.getDefault()

        val formatCurrency = NumberFormat.getCurrencyInstance(localeID)
        bill.let {
            val amount = formatCurrency.format(bill.amount.toDouble())
            val sdf = SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault())
            val date = sdf.format(RoomConverters.dateToLong(bill.date))

            binding.tvTitle.text = bill.title
            binding.tvContent.text = bill.content
            binding.tvAmount.text = amount
            binding.tvDate.text = date
        }

        bill.let {
            binding.fab.setOnClickListener {

                deleteBill(bill)

                finish()
            }
        }

        binding.cvTitle.setOnClickListener {
            if (!bill.paid.toBoolean())
                UpdateBillSheet(bill).show(supportFragmentManager, "Update")
        }

        binding.cvAmount.setOnClickListener {
            if (!bill.paid.toBoolean())
                UpdateBillSheet(bill).show(supportFragmentManager, "Update")
        }

        setContentView(binding.root)
    }

    private fun setupViewModel() {
        val database: BillDatabase = BillDatabase.invoke(this)
        val billRepository = BillRepository(database)
        val viewModelFactory = ViewModelFactory(billRepository)
        billDetailViewModel =
            ViewModelProvider(
                this,
                viewModelFactory
            )[BillDetailViewModel::class.java]
    }

    private fun updateBill(bill: Bill) {
        billDetailViewModel.updateBill(bill)
    }

    private fun deleteBill(bill: Bill) {
        billDetailViewModel.deleteBill(bill)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}
