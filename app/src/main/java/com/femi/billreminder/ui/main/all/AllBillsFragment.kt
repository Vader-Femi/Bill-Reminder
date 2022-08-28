package com.femi.billreminder.ui.main.all

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.femi.billreminder.database.BillDatabase
import com.femi.billreminder.database.entity.Bill
import com.femi.billreminder.databinding.FragmentAllBillsBinding
import com.femi.billreminder.repository.BillRepository
import com.femi.billreminder.ui.base.ViewModelFactory
import com.femi.billreminder.ui.bill.detail.BillDetailActivity
import com.femi.billreminder.utils.getBillIntent


class AllBillsFragment : Fragment() {

    private lateinit var adapter: AllBillsAdapter
    private lateinit var binding: FragmentAllBillsBinding

    private lateinit var allBillsViewModel: AllBillsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentAllBillsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (activity != null && context != null) {
            setupViewModel(requireContext())

            initAdapter()
            binding.rvBills.setHasFixedSize(true)
            binding.rvBills.layoutManager = LinearLayoutManager(context)
            binding.rvBills.adapter = adapter

            allBillsViewModel.getBills().observe(viewLifecycleOwner) {
                renderBillData(it)
            }

        }
    }

    private fun setupViewModel(context: Context) {
        val database: BillDatabase = BillDatabase.invoke(context)
        val billRepository = BillRepository(database)
        val viewModelFactory = ViewModelFactory(billRepository)
        allBillsViewModel =
            ViewModelProvider(
                this,
                viewModelFactory
            )[AllBillsViewModel::class.java]
    }

    private fun initAdapter() {
        adapter = AllBillsAdapter(requireContext()) { bill ->
            startActivity(activity?.getBillIntent(BillDetailActivity::class.java, bill))
        }
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
        adapter.submitList(bills)
    }

    companion object {
        fun newInstance(): Fragment {
            return AllBillsFragment()
        }
    }
}
