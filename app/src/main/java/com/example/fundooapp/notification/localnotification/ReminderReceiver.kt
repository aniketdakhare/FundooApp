package com.example.fundooapp.notification.localnotification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ReminderReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val title:String = intent?.getStringExtra("tittle")!!
        val text:String = intent.getStringExtra("text")!!
        val notificationHelper = NotificationHelper(context)
        val channelNotification = notificationHelper.getChannelNotification(title, text)
        notificationHelper.manager?.notify(1, channelNotification.build())
    }
}