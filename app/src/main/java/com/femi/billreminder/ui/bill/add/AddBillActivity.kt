package com.femi.billreminder.ui.bill.add

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.femi.billreminder.R
import com.femi.billreminder.database.BillDatabase
import com.femi.billreminder.database.entity.Bill
import com.femi.billreminder.databinding.ActivityAddBillBinding
import com.femi.billreminder.notifications.NotificationWorker
import com.femi.billreminder.repository.BillRepository
import com.femi.billreminder.ui.base.ViewModelFactory
import com.femi.billreminder.utils.RoomConverters
import com.femi.billreminder.utils.setAlarm
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

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
            val description = binding.edtDescription.text.toString()
            val amount = binding.edtAmount.text.toString()
            val date = binding.edtDate.text.toString()

            when {
                title.isEmpty() -> binding.edtTitle.error = getString(R.string.validation_filled)
                description.isEmpty() -> binding.edtDescription.error =
                    getString(R.string.validation_filled)
                amount.isEmpty() -> binding.edtAmount.error = getString(R.string.validation_filled)
                date.isEmpty() -> binding.edtDate.error = getString(R.string.validation_filled)
                else -> {
                    val bill = Bill(
                        0,
                        title,
                        description,
                        amount.toBigDecimal(),
                        chosenDate,
                        "false"
                    )

                    showReminderDpd(bill)

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
            .setTitleText(getString(R.string.select_the_bill_due_date))
            .build()
        datePicker.show(supportFragmentManager, "DATE_PICKER")

        datePicker.addOnPositiveButtonClickListener {

            if (it >= startDate) {
                chosenDate = RoomConverters.longToDate(it)!!
                val sdf = SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault())
                val date = sdf.format(it)

                binding.edtDate.error = null
                binding.edtDate.setText(date)
            } else
                Toast.makeText(this, getString(R.string.select_a_future_date), Toast.LENGTH_SHORT)
                    .show()

        }
    }

    private fun showReminderDpd(bill: Bill) {

        val today = MaterialDatePicker.todayInUtcMilliseconds()
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        calendar.timeInMillis = today
        val startDate = calendar.timeInMillis
        val endDate = chosenDate.time

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
            .setTitleText(getString(R.string.when_do_you_want_to_be_reminded))
            .build()
        datePicker.show(supportFragmentManager, "DATE_PICKER")

        datePicker.addOnPositiveButtonClickListener {

            if (it >= startDate && it <= chosenDate.time) {

//                val diff = it - startDate
//                val daysDelay = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)
//
//                createNotificationWorkRequest(bill, daysDelay)

                this.setAlarm(bill, it)

                insertBill(bill)

                Toast.makeText(this, "${bill.title} added successfully", Toast.LENGTH_SHORT).show()
                finish()

            } else
                Toast.makeText(this,
                    getString(R.string.select_a_valid_reminder_date),
                    Toast.LENGTH_SHORT)
                    .show()

        }

    }

    private fun createNotificationWorkRequest(bill: Bill, delayInDays: Long) {
        val workManager = WorkManager.getInstance(this)
        val sdf = SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault())
        val date = sdf.format(bill.date)

        val data = workDataOf(
            Pair("billTitle", bill.title),
            Pair("billDate", date)
        )

        val workRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay(delayInDays, TimeUnit.DAYS)
            .setInputData(data)
            .build()
        workManager.enqueueUniqueWork(
            bill.title,
            ExistingWorkPolicy.REPLACE,
            workRequest
        )
    }

    private fun TextInputEditText.manageImeOption() {

        this.setOnEditorActionListener { _, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    when (this.id) {
                        binding.edtTitle.id -> binding.edtDescription.requestFocus()
                        binding.edtDescription.id -> binding.edtAmount.requestFocus()
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
