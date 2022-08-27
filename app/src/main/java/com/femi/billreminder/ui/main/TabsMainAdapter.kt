package com.femi.billreminder.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.femi.billreminder.ui.main.all.AllBillsFragment
import com.femi.billreminder.ui.main.paid.BillPaidFragment
import com.femi.billreminder.ui.main.unpaid.BillUnpaidFragment

class TabsMainAdapter(
    fm: FragmentManager,
    private var mNumOfTabs: Int,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fm, lifecycle) {

    override fun getItemCount(): Int = mNumOfTabs

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> BillUnpaidFragment.newInstance()
            1 -> BillPaidFragment.newInstance()
            else -> AllBillsFragment.newInstance()
        }
    }
}