package com.vm.timemanager

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import com.vm.timemanager.notifications.CHANNEL_ID

const val NOTIFICATION_CHANEL_ID = "task_reminder_channel"

class ApplicationTimeManager : Application() {
    override fun onCreate() {
        super.onCreate()

        //creating notification channel
//        val name = getString(R.string.notification_channel_name)
//        val importance = NotificationManager.IMPORTANCE_DEFAULT
//        val chanel = NotificationChannel(NOTIFICATION_CHANEL_ID, name, importance)
//        val notificationManager: NotificationManager =
//            getSystemService(NotificationManager::class.java)
//        notificationManager.createNotificationChannel(chanel)
//        notificationManager.createNotificationChannel(chanel)

        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        val name = "Time Manager"
        val description = "Time Manager's Notification Channel"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance)
        channel.description = description

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}