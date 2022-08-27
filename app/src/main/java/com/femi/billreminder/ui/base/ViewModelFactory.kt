package com.femi.billreminder.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.femi.billreminder.repository.BillRepository
import com.femi.billreminder.ui.bill.add.AddBillViewModel
import com.femi.billreminder.ui.bill.detail.BillDetailViewModel
import com.femi.billreminder.ui.main.all.AllBillsViewModel
import com.femi.billreminder.ui.main.paid.BillPaidViewModel
import com.femi.billreminder.ui.main.unpaid.BillUnpaidViewModel

class ViewModelFactory(private val repository: BaseRepository): ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when{
            modelClass.isAssignableFrom(AllBillsViewModel::class.java) -> AllBillsViewModel(repository as BillRepository) as T
            modelClass.isAssignableFrom(AddBillViewModel::class.java) -> AddBillViewModel(repository as BillRepository) as T
            modelClass.isAssignableFrom(BillUnpaidViewModel::class.java) -> BillUnpaidViewModel(repository as BillRepository) as T
            modelClass.isAssignableFrom(BillPaidViewModel::class.java) -> BillPaidViewModel(repository as BillRepository) as T
            modelClass.isAssignableFrom(BillDetailViewModel::class.java) -> BillDetailViewModel(repository as BillRepository) as T
            else -> throw IllegalArgumentException("ViewModelClass Not Found")
        }
    }
}