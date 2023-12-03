package com.example.greenthumb.workmanager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat


class CustomBroadcast : BroadcastReceiver() {

    private val channelId = "my_channel_id"
    override fun onReceive(context: Context?, intent: Intent?) {
        createNotificationChannel(context)
        showNotification(context)
    }



    private fun showNotification(context: Context?) {
        val builder = context?.let {
            NotificationCompat.Builder(it, channelId)
                .setSmallIcon(com.google.android.material.R.drawable.navigation_empty_icon)
                .setContentTitle("Notification Title")
                .setContentText("Notification Content")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        }

        with(context?.let { NotificationManagerCompat.from(it) }) {
            builder?.let { this?.notify(0, it.build()) }
        }
    }
    private fun createNotificationChannel(context: Context?) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "My Channel"
            val descriptionText = "My Channel Description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }

            val notificationManager =
                context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}