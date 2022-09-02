package com.femi.billreminder.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.femi.billreminder.database.AlarmsDatabase
import com.femi.billreminder.utils.EXTRA_BILL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class BootCompleteBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if ("android.intent.action.BOOT_COMPLETED" == intent.action) {

            val database: AlarmsDatabase = AlarmsDatabase.invoke(context)

            runBlocking {
                launch (Dispatchers.Default){
                    for (bill in database.alarmsDao().getAlarms()) {
                        context.let {
                            val alarmManager = context.getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager

                            val notificationIntent = Intent(context, ReminderReceiver::class.java)
                            val bundle = Bundle()
                            bundle.putSerializable(EXTRA_BILL, bill)
                            notificationIntent.putExtras(bundle)

                            val pendingIntent = PendingIntent.getBroadcast(context,
                                0,
                                notificationIntent,
                                PendingIntent.FLAG_IMMUTABLE)

                            alarmManager.cancel(pendingIntent)
                        }
                    }

                }
            }

        }
    }
}