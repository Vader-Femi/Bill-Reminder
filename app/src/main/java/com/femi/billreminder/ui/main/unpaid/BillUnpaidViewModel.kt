package com.femi.billreminder.ui.main.unpaid

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.femi.billreminder.database.entity.Bill
import com.femi.billreminder.repository.BillRepository
import com.femi.billreminder.ui.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BillUnpaidViewModel (
    private val billRepository: BillRepository
) : BaseViewModel(billRepository) {

    fun getBills(): LiveData<List<Bill>> = billRepository.getUnpaidBills()

}