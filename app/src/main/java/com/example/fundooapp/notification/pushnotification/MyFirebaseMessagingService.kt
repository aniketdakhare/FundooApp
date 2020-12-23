package com.example.fundooapp.notification.pushnotification

import com.example.fundooapp.notification.localnotification.NotificationHelper
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        val notificationHelper = NotificationHelper(this)
        val channelNotification = notificationHelper.getChannelNotification(
            remoteMessage.notification?.title.toString(),
            remoteMessage.notification?.body.toString())
        notificationHelper.manager?.notify(0, channelNotification.build())
    }
}