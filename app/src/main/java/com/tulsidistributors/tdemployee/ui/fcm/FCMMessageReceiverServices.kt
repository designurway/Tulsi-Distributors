package com.tulsidistributors.tdemployee.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.tulsidistributors.tdemployee.R
import com.tulsidistributors.tdemployee.ui.home.HomePageActivity

class FCMMessageReceiverServices:FirebaseMessagingService() {

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
        Log.d("TAGnOtification", p0.notification?.title.toString())
        createNotificationChannel()

        val notifyIntent = Intent(this, HomePageActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        notifyIntent.putExtra("Title", p0.notification?.title.toString())
        notifyIntent.putExtra("Body",p0.notification?.body.toString())
        val notifyPendingIntent = PendingIntent.getActivity(
            this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )
        var builder = NotificationCompat.Builder(applicationContext, "102")
        builder.setSmallIcon(R.drawable.facebook_icon)
        builder.setContentTitle( p0.notification?.title.toString())
        builder.setContentText(p0.notification?.body.toString())
        builder.setAutoCancel(true)
        builder.setContentIntent(notifyPendingIntent)
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT)


        with(NotificationManagerCompat.from(applicationContext)) {
            // notificationId is a unique int for each notification that you must define
            notify(2, builder.build())
        }
    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        Log.d("TAGnOtification", "onNewToken: called")
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "route"
            val descriptionText = "this is notification channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("102", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =applicationContext.getSystemService(Context.NOTIFICATION_SERVICE)
                    as NotificationManager
            notificationManager.createNotificationChannel(channel)

        }
    }

}