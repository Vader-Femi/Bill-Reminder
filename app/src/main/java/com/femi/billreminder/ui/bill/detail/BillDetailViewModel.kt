package com.femi.billreminder.ui.bill.detail

import androidx.lifecycle.viewModelScope
import com.femi.billreminder.database.entity.Bill
import com.femi.billreminder.repository.BillRepository
import com.femi.billreminder.ui.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BillDetailViewModel(
    private val billRepository: BillRepository
) : BaseViewModel(billRepository) {

    fun deleteBill(bill: Bill) {
        viewModelScope.launch {
            delete(bill)
        }
    }

    private suspend fun delete(bill: Bill) {
        withContext(Dispatchers.IO) {
            billRepository.delete(bill)
        }
    }
}