package com.example.fundooapp.notification.localnotification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences

class ReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val sharedPreference = context?.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        val title = intent?.getStringExtra("tittle")!!
        val text = intent.getStringExtra("text")!!
        val notificationHelper = NotificationHelper(context)
        val channelNotification =
            notificationHelper.getChannelNotification(title, text)
        var notificationId = getNotificationId(sharedPreference)
        notificationId?.let {
            notificationId += 1
            notificationHelper.manager?.notify(notificationId, channelNotification.build())
            setNotificationId(sharedPreference, notificationId)
        }
    }

    private fun setNotificationId(sharedPreference: SharedPreferences?, notificationId: Int) {
        val editor = sharedPreference?.edit()
        editor?.putInt(NOTIFICATION_ID, notificationId)
        editor?.apply()
    }

    private fun getNotificationId(sharedPreference: SharedPreferences?): Int? {
        return sharedPreference?.getInt(NOTIFICATION_ID, 0)
    }

    companion object {
        private const val SHARED_PREFS = "com.example.fundooapp.notificationId"
        private const val NOTIFICATION_ID = "id"
    }
}