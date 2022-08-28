package com.femi.billreminder.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.femi.billreminder.database.entity.Bill
import com.femi.billreminder.utils.EXTRA_BILL

class ReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val bundle = intent.extras
        val bill = bundle?.getSerializable(EXTRA_BILL) as? Bill

        if (bill != null) {
            ReminderNotificationService(context).showNotification(bill)
        }
    }
}