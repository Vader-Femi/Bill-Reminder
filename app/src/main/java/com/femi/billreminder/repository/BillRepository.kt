package com.femi.billreminder.repository

import androidx.lifecycle.LiveData
import com.femi.billreminder.ui.base.BaseRepository
import com.femi.billreminder.database.BillDatabase
import com.femi.billreminder.database.entity.Bill

class BillRepository(
    private val billDatabase: BillDatabase,
): BaseRepository() {

    fun getBills(): LiveData<List<Bill>> =
        billDatabase.billDao().getBills()

    fun getUnpaidBills(): LiveData<List<Bill>> =
        billDatabase.billDao().getUnpaidBills()

    fun getPaidBills(): LiveData<List<Bill>> =
        billDatabase.billDao().getPaidBills()

    fun update(bill: Bill) {
        billDatabase.billDao().update(bill)
    }

    fun insert(bill: Bill) {
        billDatabase.billDao().insert(bill)
    }

    fun delete(bill: Bill) {
        billDatabase.billDao().deleteBill(bill)
    }

}