package com.femi.billreminder.notifications

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.app.NotificationCompat
import com.femi.billreminder.R
import com.femi.billreminder.database.entity.Bill
import com.femi.billreminder.ui.bill.detail.BillDetailActivity
import com.femi.billreminder.utils.EXTRA_BILL

class ReminderNotificationService(
    private val context: Context,
) {

    companion object {
        const val REMINDER_CHANNEL_ID = "reminder_channel"
    }

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun showNotification(bill: Bill) {
        val activityIntent = Intent(context, BillDetailActivity::class.java)
        val bundle = Bundle()
        bundle.putSerializable(EXTRA_BILL, bill)
        activityIntent.putExtras(bundle)

        val activityPendingIntent = PendingIntent.getActivity(context,
            1,
            activityIntent,
            PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(context, REMINDER_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(context.getString(R.string.bill_reminder_notification_title))
            .setContentText("${context.getString(R.string.bill_reminder_notification_text)} - ${bill.title} ${
                context.getString(R.string.on)
            } ${bill.date}")
            .setContentIntent(activityPendingIntent)
            .build()

        notificationManager.notify(
            1, notification
        )
    }

}