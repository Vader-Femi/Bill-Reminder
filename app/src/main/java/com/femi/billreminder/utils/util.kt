package com.femi.billreminder.utils

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import com.femi.billreminder.database.AlarmsDatabase
import com.femi.billreminder.database.entity.Alarms
import com.femi.billreminder.database.entity.Bill
import com.femi.billreminder.notifications.ReminderReceiver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun <A : Activity> Activity.getBillIntent(destination: Class<A>, bill: Bill): Intent {
    val intent = Intent(this, destination)
    val bundle = Bundle()
    bundle.putSerializable(EXTRA_BILL, bill)
    intent.putExtras(bundle)
    return intent
}

fun Activity.setAlarm(bill: Bill, alarmTime: Long) {
    val alarmManager = getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager

    val intent = Intent(this, ReminderReceiver::class.java)
    val bundle = Bundle()
    bundle.putSerializable(EXTRA_BILL, bill)
    intent.putExtras(bundle)

    val pendingIntent = PendingIntent.getBroadcast(this,
        0,
        intent,
        PendingIntent.FLAG_IMMUTABLE)

    alarmManager.setAndAllowWhileIdle(
        AlarmManager.RTC,
        alarmTime,
        pendingIntent
    )

}

fun Activity.cancelAlarm(bill: Bill) {
    val alarmManager = getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager

    val intent = Intent(this, ReminderReceiver::class.java)
    val bundle = Bundle()
    bundle.putSerializable(EXTRA_BILL, bill)
    intent.putExtras(bundle)

    val pendingIntent = PendingIntent.getBroadcast(this,
        0,
        intent,
        PendingIntent.FLAG_IMMUTABLE)

    alarmManager.cancel(pendingIntent)

}

fun LifecycleCoroutineScope.addAlarm(bill: Bill, context: Context?, time: Long) {
    runBlocking {
        launch(Dispatchers.Default) {
            context?.let { context ->
                AlarmsDatabase.invoke(context).alarmsDao().insert(Alarms(0, bill, time))
            }
        }
    }

}

fun LifecycleCoroutineScope.deleteAlarm(bill: Bill, context: Context?) {
    runBlocking {
        launch(Dispatchers.Default) {
            context?.let { context ->
                val alarmsDatabase = AlarmsDatabase.invoke(context).alarmsDao()
                alarmsDatabase.getAlarms().forEach { alarm ->
                    if (alarm.bill == bill)
                        alarmsDatabase.deleteAlarm(alarm)
                }
            }
        }
    }

}