package com.femi.billreminder.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.femi.billreminder.R
import com.femi.billreminder.databinding.ActivityMainBinding
import com.femi.billreminder.ui.bill.add.AddBillActivity
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)

//        binding.tabLayout.tabGravity = TabLayout.GRAVITY_FILL
//
//        binding.tabLayout.addTab(binding.tabLayout.newTab()
//            .setText(getString(R.string.menu_bills_unpaid)))
//        binding.tabLayout.addTab(binding.tabLayout.newTab()
//            .setText(getString(R.string.menu_bills_paid)))
//        binding.tabLayout.addTab(binding.tabLayout.newTab()
//            .setText(getString(R.string.menu_all_bills)))
//
//        val tabsAdapter =
//            TabsMainAdapter(supportFragmentManager, binding.tabLayout.tabCount, lifecycle)
//        binding.viewPager2.adapter = tabsAdapter
//
//        val myPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
//            override fun onPageSelected(position: Int) {
//                binding.tabLayout.getTabAt(position)?.select()
//            }
//        }
//        binding.viewPager2.registerOnPageChangeCallback(myPageChangeCallback)
//
//        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
//            override fun onTabSelected(tab: TabLayout.Tab) {
//                binding.viewPager2.currentItem = tab.position
//            }
//
//            override fun onTabUnselected(tab: TabLayout.Tab) {
//            }
//
//            override fun onTabReselected(tab: TabLayout.Tab) {
//            }
//        })

        val bottomTabsAdapter =
            TabsMainAdapter(supportFragmentManager, 3, lifecycle)
        binding.viewPager2.adapter = bottomTabsAdapter

        val bottomPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.bottomBar.menu.getItem(position).isChecked = true
            }
        }
        binding.viewPager2.registerOnPageChangeCallback(bottomPageChangeCallback)

        binding.bottomBar.setOnItemSelectedListener  {
            when (it.itemId) {
                R.id.menu_unpaid -> {
                    binding.viewPager2.currentItem = 0
                }
                R.id.menu_paid -> {
                    binding.viewPager2.currentItem = 1
                }
                R.id.menu_all_bills -> {
                    binding.viewPager2.currentItem = 2
                }
            }
            true
        }

        binding.fab.setOnClickListener {
            val intent = Intent(this, AddBillActivity::class.java)
            startActivity(intent)
        }

        setContentView(binding.root)
    }
}