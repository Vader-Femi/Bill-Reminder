package com.femi.billreminder.ui.bill.add

import androidx.lifecycle.viewModelScope
import com.femi.billreminder.ui.base.BaseViewModel
import com.femi.billreminder.database.entity.Bill
import com.femi.billreminder.repository.BillRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddBillViewModel(
    private val billRepository: BillRepository
) : BaseViewModel(billRepository) {

    fun insertBill(bill: Bill) {
        viewModelScope.launch {
            insert(bill)
        }
    }

    private suspend fun insert(bill: Bill) {
        withContext(Dispatchers.IO) {
            billRepository.insert(bill)
        }
    }
}