package com.femi.billreminder.utils

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.femi.billreminder.database.entity.Bill
import com.femi.billreminder.notifications.ReminderReceiver

fun <A : Activity> Activity.getBillIntent(destination: Class<A>, bill: Bill): Intent {
    val intent = Intent(this, destination)
    val bundle = Bundle()
    bundle.putSerializable(EXTRA_BILL, bill)
    intent.putExtras(bundle)
    return intent
}

fun Activity.getReminderIntent(bill: Bill): Intent {
    val intent = Intent(this, ReminderReceiver::class.java)
    val bundle = Bundle()
    bundle.putSerializable(EXTRA_BILL, bill)
    intent.putExtras(bundle)
    return intent
}