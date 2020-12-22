package com.example.fundooapp.notification.localnotification

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.fundooapp.R.drawable.ic_baseline_event_note_24

class NotificationHelper(base: Context?) : ContextWrapper(base) {
    private var mManager: NotificationManager? = null

    @TargetApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        val channel =
            NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_DEFAULT)
        manager?.createNotificationChannel(channel)
    }

    val manager: NotificationManager?
        get() {
            if (mManager == null) {
                mManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            }
            return mManager
        }

    fun getChannelNotification(tittle: String, text: String): NotificationCompat.Builder {
        return NotificationCompat.Builder(applicationContext, channelID)
            .setContentTitle(tittle)
            .setContentText(text)
            .setSmallIcon(ic_baseline_event_note_24)
    }

    companion object {
        const val channelID = "reminderID"
        const val channelName = "Fundoo Reminder"
    }

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel()
        }
    }
}