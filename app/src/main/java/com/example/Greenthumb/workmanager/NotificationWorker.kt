package com.example.greenthumb.workmanager

import android.content.Context
import androidx.work.*
import java.util.concurrent.TimeUnit

class NotificationWorker(val context: Context, params: WorkerParameters) : Worker(context,params){

    private val CHANNEL_ID = "notification_channel"
    private val notificationId = 1


    override fun doWork(): Result {
        showNotification()
        //scheduleNextNotification()
        return Result.success()
    }

    private fun showNotification() {
        val notificationHelper = NotificationHelper(applicationContext)
        notificationHelper.showNotification()
    }

    private fun scheduleNextNotification() {
        val nextWorkRequest = getNextWorkRequest()
        WorkManager.getInstance(applicationContext).enqueue(nextWorkRequest)
    }

    private fun getNextWorkRequest(): OneTimeWorkRequest {
        val delay = 2 * 60 * 1000 // 2 minutes
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .build()


        return OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay(delay.toLong(), TimeUnit.MILLISECONDS)
            .setConstraints(constraints)
            .build()
    }


}