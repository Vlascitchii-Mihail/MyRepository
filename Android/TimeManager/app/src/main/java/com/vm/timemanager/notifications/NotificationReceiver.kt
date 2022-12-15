package com.vm.timemanager.notifications

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.vm.timemanager.R

const val NOTIFICATION_ID = 1
const val CHANNEL_ID = "reminders"
const val TITLE_EXTRA = "titleExtra"
const val MESSAGE_EXTRA = "messageExtra"


class NotificationReceiver : BroadcastReceiver()
{
    override fun onReceive(context: Context, intent: Intent?)
    {
        val notification = NotificationCompat.Builder(context.applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(intent?.getStringExtra(TITLE_EXTRA))
            .setContentText(intent?.getStringExtra(MESSAGE_EXTRA))
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(NOTIFICATION_ID, notification)
    }
}