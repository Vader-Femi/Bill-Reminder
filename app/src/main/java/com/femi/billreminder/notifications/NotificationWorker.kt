package com.femi.billreminder.notifications

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.femi.billreminder.database.entity.Bill

class NotificationWorker(context: Context, params: WorkerParameters) :
    Worker(context, params) {
    override fun doWork(): Result {
        val billTitle = inputData.getString("billTitle")
        val billDate = inputData.getString("billDate")
        return if (billTitle != null && billDate != null) {
            createNotification(billTitle, billDate)
            Result.success()
        } else Result.failure()
    }

    private fun createNotification(billTitle: String, billDate: String) {
        val service = ReminderNotificationService(applicationContext)
//        service.showNotification(billTitle, billDate)
    }
}