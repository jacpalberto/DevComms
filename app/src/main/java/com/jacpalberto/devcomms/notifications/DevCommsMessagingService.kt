package com.jacpalberto.devcomms.notifications

import android.content.Context.NOTIFICATION_SERVICE
import android.app.NotificationManager
import com.jacpalberto.devcomms.R.mipmap.ic_launcher
import android.media.RingtoneManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import com.jacpalberto.devcomms.events.MainActivity
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.messaging.FirebaseMessagingService
import com.jacpalberto.devcomms.R


/**
 * Created by Alberto Carrillo on 9/6/18.
 */
//TODO: filtrar notificaciones
class DevCommsMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage?) {
        sendMyNotification(message!!.notification!!.body)
    }

    private fun sendMyNotification(message: String?) {
        val intent = MainActivity.newIntent(this).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }

        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notificationBuilder = NotificationCompat.Builder(this, getString(R.string.app_name))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(soundUri)
                .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.notify(0, notificationBuilder.build())
    }
}