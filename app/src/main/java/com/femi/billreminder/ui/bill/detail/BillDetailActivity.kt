package com.femi.billreminder.ui.bill.detail

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.femi.billreminder.R
import com.femi.billreminder.database.BillDatabase
import com.femi.billreminder.database.entity.Bill
import com.femi.billreminder.databinding.ActivityBillDetailBinding
import com.femi.billreminder.repository.BillRepository
import com.femi.billreminder.ui.base.ViewModelFactory
import com.femi.billreminder.ui.main.MainActivity
import com.femi.billreminder.utils.EXTRA_BILL
import com.femi.billreminder.utils.RoomConverters
import com.femi.billreminder.utils.cancelAlarm
import com.femi.billreminder.utils.deleteAlarm
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
        val bill: Bill = bundle?.getSerializable(EXTRA_BILL) as Bill

        val localeID = Locale.getDefault()

        val formatCurrency = NumberFormat.getCurrencyInstance(localeID)
        bill.let {
            val amount = formatCurrency.format(bill.amount.toDouble())
            val sdf = SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault())
            val date = sdf.format(RoomConverters.dateToLong(bill.date))

            binding.tvTitle.text = bill.title
            binding.tvDescription.text = bill.description
            binding.tvAmount.text = amount
            binding.tvDate.text = date
        }

        bill.let {
            binding.fab.setOnClickListener {

                this.cancelAlarm(bill)
                lifecycleScope.deleteAlarm(bill, this)

                deleteBill(bill)

                Toast.makeText(this, getString(R.string.bill_deleted), Toast.LENGTH_SHORT).show()

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

    private fun deleteBill(bill: Bill) {
        billDetailViewModel.deleteBill(bill)
    }

    override fun onSupportNavigateUp(): Boolean {
        if (isTaskRoot){
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }else{
            finish()
        }
        return super.onSupportNavigateUp()
    }
}
