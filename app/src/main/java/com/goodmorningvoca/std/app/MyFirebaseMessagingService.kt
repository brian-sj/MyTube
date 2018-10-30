package com.goodmorningvoca.std.app

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.support.v4.app.NotificationCompat

import android.util.Log
import com.goodmorningvoca.std.app.model.GlobalVariable

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.goodmorningvoca.std.app.R

class MyFirebaseMessagingService  : FirebaseMessagingService() {

    private val TAG = "FCM"

    /*
    override fun onNewToken(token: String?) {
        Log.e(TAG, "new Token: $token")
        println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$")
    }
    */

    /**
     * this method will be triggered every time there is new FCM Message.
     */
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.from)
        Log.e(TAG , remoteMessage.data.toString())

        val score = remoteMessage.data

        GlobalVariable.unlead++
        val intent = Intent("android.intent.action.BADGE_COUNT_UPDATE")
        intent.putExtra("badge_count", GlobalVariable.unlead ) //뱃지 카운터
        intent.putExtra("badge_count_package_name", packageName  )//패키지 명
        intent.putExtra("badge_count_class_name", "com.goodmorningvoca.std.app.SplashActivity" )//런처 Activity명
        sendBroadcast(intent)

        //intent.putExtra( Data.FCM_SCORE_KEY , score  )

        if(remoteMessage.notification != null) {
            Log.e(TAG, "Notification Message Body: ${remoteMessage.notification?.body}")
            sendNotification(remoteMessage.notification?.body)
        }
    }

    private fun sendNotification(body: String?) {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("Notification", body)
        }
        Log.e(TAG, "SEND Notification $body " )

        var pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        val notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        var notificationBuilder = NotificationCompat.Builder(this,"Notification")
                .setSmallIcon(R.drawable.ic_stat_name)

                .setContentTitle("Push Notification FCM")
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(notificationSound)
                .setContentIntent(pendingIntent)

        var notificationManager: NotificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, notificationBuilder.build())
    }

}
