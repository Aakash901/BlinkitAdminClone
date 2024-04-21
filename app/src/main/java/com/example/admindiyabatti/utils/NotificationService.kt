package com.example.admindiyabatti.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.admindiyabatti.R
import com.example.admindiyabatti.activity.AdminMainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlin.random.Random

class NotificationService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val channnelId = "Admin Diya Batti"
        val channel = NotificationChannel(
            channnelId,
            "Diya Batti",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Diya Batti message"
            enableLights(true)
        }

        val  pendingIntent = PendingIntent.getActivity(this,0, Intent(this,AdminMainActivity::class.java),PendingIntent.FLAG_IMMUTABLE)
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(this, channnelId)
            .setContentTitle(message.data["title"])
            .setContentText(message.data["body"])
            .setSmallIcon(R.drawable.admins_app_icon)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        manager.notify(Random.nextInt(), notification)
    }
}