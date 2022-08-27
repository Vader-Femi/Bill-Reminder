package com.femi.billreminder.ui.bill.add

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.femi.billreminder.R
import com.femi.billreminder.database.BillDatabase
import com.femi.billreminder.database.entity.Bill
import com.femi.billreminder.databinding.ActivityAddBillBinding
import com.femi.billreminder.repository.BillRepository
import com.femi.billreminder.ui.base.ViewModelFactory
import com.femi.billreminder.utils.RoomConverters
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.*


class AddBillActivity : AppCompatActivity() {

    private lateinit var addBillViewModel: AddBillViewModel
    private lateinit var binding: ActivityAddBillBinding
    private lateinit var chosenDate: Date

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBillBinding.inflate(layoutInflater)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val database: BillDatabase = BillDatabase.invoke(this)
        val billRepository = BillRepository(database)
        val viewModelFactory = ViewModelFactory(billRepository)
        addBillViewModel =
            ViewModelProvider(
                this,
                viewModelFactory
            )[AddBillViewModel::class.java]

//        binding.edtTitle.manageImeOption()
//        binding.edtContent.manageImeOption()
//        binding.edtAmount.manageImeOption()

        binding.edtDate.setOnClickListener {
            showDPD()
        }

        binding.fab.setOnClickListener {
            val title = binding.edtTitle.text.toString()
            val content = binding.edtContent.text.toString()
            val amount = binding.edtAmount.text.toString()
            val date = binding.edtDate.text.toString()

            when {
                title.isEmpty() -> binding.edtTitle.error = getString(R.string.validation_filled)
                content.isEmpty() -> binding.edtContent.error =
                    getString(R.string.validation_filled)
                amount.isEmpty() -> binding.edtAmount.error = getString(R.string.validation_filled)
                date.isEmpty() -> binding.edtDate.error = getString(R.string.validation_filled)
                else -> {
                    val bill = Bill(
                        0,
                        title,
                        content,
                        amount.toBigDecimal(),
                        amount.toBigDecimal(),
                        chosenDate,
                        "false"
                    )

                    insertBill(bill)

                    Toast.makeText(this, "Add Bill successfully", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }

        setContentView(binding.root)
    }

    private fun showDPD() {

        val today = MaterialDatePicker.todayInUtcMilliseconds()
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        calendar.timeInMillis = today
        val startDate = calendar.timeInMillis
        calendar[Calendar.MONDAY] = calendar.get(Calendar.MONTH)

        calendar.timeInMillis = today
        calendar[Calendar.YEAR] = 2050
        val endDate = calendar.timeInMillis

        val constraints: CalendarConstraints = CalendarConstraints.Builder()
            .setOpenAt(startDate)
            .setStart(startDate)
            .setEnd(endDate)
            .build()

        val datePicker: MaterialDatePicker<Long> = MaterialDatePicker
            .Builder
            .datePicker()
            .setCalendarConstraints(constraints)
            .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
            .setTitleText("Select a date")
            .build()
        datePicker.show(supportFragmentManager, "DATE_PICKER")

        datePicker.addOnPositiveButtonClickListener {

            chosenDate = RoomConverters.longToDate(it)!!
            val sdf = SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault())
            val date = sdf.format(it)

            binding.edtDate.setText(date)

        }
    }

    private fun TextInputEditText.manageImeOption() {

        this.setOnEditorActionListener { _, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    when (this.id) {
                        binding.edtTitle.id -> binding.edtContent.requestFocus()
                        binding.edtContent.id -> binding.edtAmount.requestFocus()
                        else -> {
                            val imm =
                                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                            imm.hideSoftInputFromWindow(windowToken, 0)
                        }
                    }
                }
            }
            false
        }
    }

    private fun insertBill(bill: Bill) {
        addBillViewModel.insertBill(bill)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}
