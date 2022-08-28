package com.femi.billreminder.ui.bill.detail

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.femi.billreminder.R
import com.femi.billreminder.database.BillDatabase
import com.femi.billreminder.database.entity.Bill
import com.femi.billreminder.databinding.FragmentUpdateBillBinding
import com.femi.billreminder.repository.BillRepository
import com.femi.billreminder.ui.base.ViewModelFactory
import com.femi.billreminder.ui.main.MainActivity
import com.femi.billreminder.utils.RoomConverters
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.coroutines.runBlocking
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.*

class UpdateBillSheet(val bill: Bill) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentUpdateBillBinding
    private lateinit var billDetailViewModel: BillDetailViewModel
    private lateinit var chosenDate: Date
    private lateinit var activity: Activity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentUpdateBillBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity = requireActivity()
        setupViewModel()

        bill.let {
            val sdf = SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault())
            val date = sdf.format(RoomConverters.dateToLong(bill.date))

            binding.edtTitle.setText(bill.title)
            binding.edtDescription.setText(bill.description)
            binding.edtAmount.setText(bill.amount.toDouble().toString())
            binding.edtDate.setText(date)
            chosenDate = bill.date
        }

        binding.edtDate.setOnClickListener {
            showDPD()
        }

        binding.btnSave.setOnClickListener {
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
                        bill.id,
                        title,
                        description,
                        amount.toDouble().toBigDecimal(),
                        chosenDate,
                        bill.paid
                    )

                    updateBill(bill)

                    Toast.makeText(activity, "Bill updated", Toast.LENGTH_SHORT).show()

                    val intent = Intent(context, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    activity.finish()
                }
            }
        }

    }

    private fun showDPD() {
        val today = MaterialDatePicker.todayInUtcMilliseconds()
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        calendar.timeInMillis = today
        val startDate = calendar.timeInMillis

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
        datePicker.show(requireActivity().supportFragmentManager, "DATE_PICKER")

        datePicker.addOnPositiveButtonClickListener {

            chosenDate = RoomConverters.longToDate(it)!!
            val sdf = SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault())
            val date = sdf.format(it)

            binding.edtDate.setText(date)

        }
    }

    private fun setupViewModel() {
        val database: BillDatabase = BillDatabase.invoke(activity)
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

}