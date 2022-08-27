package com.femi.billreminder.ui.main.unpaid

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.femi.billreminder.R
import com.femi.billreminder.database.BillDatabase
import com.femi.billreminder.database.entity.Bill
import com.femi.billreminder.databinding.FragmentBillUnpaidBinding
import com.femi.billreminder.repository.BillRepository
import com.femi.billreminder.ui.base.ViewModelFactory
import com.femi.billreminder.ui.bill.detail.BillDetailActivity
import com.femi.billreminder.utils.BILL_ID
import com.google.android.material.snackbar.Snackbar


class BillUnpaidFragment : Fragment() {

    private lateinit var unpaidAdapter: BillUnpaidAdapter
    private lateinit var binding: FragmentBillUnpaidBinding

    private lateinit var billViewModel: BillUnpaidViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentBillUnpaidBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


//        if (activity != null && context != null) {

        context?.let { context ->

            setupViewModel(context)

            initAdapter()
            binding.rvBills.setHasFixedSize(true)
            binding.rvBills.layoutManager = LinearLayoutManager(context)
            binding.rvBills.adapter = unpaidAdapter

            billViewModel.getBills().observe(viewLifecycleOwner) {
                renderBillData(it)
            }
        }
//        }

    }

    private fun setupViewModel(context: Context) {
        val database: BillDatabase = BillDatabase.invoke(context)
        val billRepository = BillRepository(database)
        val viewModelFactory = ViewModelFactory(billRepository)
        billViewModel =
            ViewModelProvider(
                this,
                viewModelFactory
            )[BillUnpaidViewModel::class.java]
    }

    private fun initAdapter() {
        unpaidAdapter =
            BillUnpaidAdapter({ bill ->
                val newBill = bill.copy(paid = "true")
                billViewModel.updateBill(newBill)

                view?.let {
                    Snackbar.make(it, "${bill.title} has been paid", Snackbar.LENGTH_LONG).apply {
                        setAction(getString(R.string.snackbar_undo)) {
                            billViewModel.updateBill(bill)
                        }
                        show()
                    }
                }

            },
                { bill ->
                    val intent = Intent(context, BillDetailActivity::class.java)
                    val bundle = Bundle()
                    bundle.putSerializable(BILL_ID, bill)
                    intent.putExtras(bundle)
                    startActivity(intent)
                })
    }

    private fun renderBillData(bills: List<Bill>) {
        if (bills.isEmpty()) {
            binding.tvNoData.visibility = View.VISIBLE
            binding.rvBills.visibility = View.GONE
        } else {
            binding.tvNoData.visibility = View.GONE
            binding.rvBills.visibility = View.VISIBLE
        }
        setBillData(bills)
    }

    private fun setBillData(bills: List<Bill>) {
        unpaidAdapter.submitList(bills)
    }

    companion object {
        fun newInstance(): Fragment {
            return BillUnpaidFragment()
        }
    }

}
