package com.menesdurak.e_ticaret_uygulamasi.common

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.menesdurak.e_ticaret_uygulamasi.R


class MyFirebaseMessagingService : FirebaseMessagingService() {

    val CHANNEL_ID = "2";

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.notification != null) {
            //Create and Display Notificaiton
            showNotification(
                remoteMessage.notification!!.title!!,
                remoteMessage.notification!!.body!!
            )
        }
        if (remoteMessage.data.isNotEmpty()) {
            val myData: Map<String, String> = remoteMessage.data
            Log.d("MYDATA", myData["key1"] ?: "null geldi")
            Log.d("MYDATA", myData["key2"] ?: "null geldi")
        }

    }

    private fun showNotification(title: String, text: String) {

        //Create Notification Channel
        createNotificationChannel()
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(
            this, CHANNEL_ID
        )
            .setSmallIcon(R.drawable.ic_home)
            .setContentTitle(title)
            .setContentText(text)
            .setAutoCancel(true)
            .setColor(resources.getColor(R.color.main))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        val notificationManagerCompat = NotificationManagerCompat.from(this)
        //Notification ID is unique for each notification you create
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        notificationManagerCompat.notify(2, builder.build())
    }

    private fun createNotificationChannel() {
        //Create Notification channel only on API Level 26+
        //NotificationChannel is a new Class and not in a support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "My Channel Name2"
            val description = "My Channel description2"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            channel.description = description
            //Register the channel with the system.
            //You cannot change importance or other notification behaviors after this
            val manager = getSystemService(
                NotificationManager::class.java
            )
            manager.createNotificationChannel(channel)
        }
    }

}
