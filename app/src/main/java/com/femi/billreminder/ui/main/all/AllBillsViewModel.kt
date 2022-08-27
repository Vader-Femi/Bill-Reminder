package com.femi.billreminder.ui.main.all

import androidx.lifecycle.LiveData
import com.femi.billreminder.database.entity.Bill
import com.femi.billreminder.repository.BillRepository
import com.femi.billreminder.ui.base.BaseViewModel

class AllBillsViewModel (
    private val billRepository: BillRepository
) : BaseViewModel(billRepository) {

    fun getBills(): LiveData<List<Bill>> = billRepository.getBills()
}