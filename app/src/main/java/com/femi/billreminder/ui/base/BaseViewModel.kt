package com.femi.billreminder.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.femi.billreminder.database.entity.Bill
import com.femi.billreminder.repository.BillRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

open class BaseViewModel(
    private val billRepository: BillRepository
): ViewModel() {

    fun updateBill(bill: Bill) {
        viewModelScope.launch {
            update(bill)
        }
    }

    private suspend fun update(bill: Bill) {
        withContext(Dispatchers.IO) {
            billRepository.update(bill)
        }
    }

}