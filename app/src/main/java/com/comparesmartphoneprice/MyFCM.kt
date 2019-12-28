package com.comparesmartphoneprice

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFCM : FirebaseMessagingService() {

    //FCM을 위한 토큰 생성
    override fun onNewToken(p0: String) {
        super.onNewToken(p0)

        Log.d("onNewToken()", p0)
    }

    //FCM 수신 시 실행
    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
        Log.d("onMessageReceived()", p0.toString())

        if (p0 != null && p0.data.size > 0) {
            whenRecived(p0)
        }
    }

    fun whenRecived(remoteMessage: RemoteMessage) {
        var msg = remoteMessage.data.get("data")
        val vibPattern: LongArray = longArrayOf(100, 200, 100, 200)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel: String = "Channel"
            val channelName: String = "ChannelName"

            val channelMsg = NotificationChannel(
                channel,
                channelName,
                android.app.NotificationManager.IMPORTANCE_DEFAULT
            )
            channelMsg.description = "Description about channel"
            channelMsg.enableLights(true)
            channelMsg.enableVibration(true)
            channelMsg.setShowBadge(false)
            channelMsg.vibrationPattern = vibPattern
            val notiChannel: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notiChannel.createNotificationChannel(channelMsg)

            //TODO 앱 아이콘 변경
            val notiBuilder: NotificationCompat.Builder = NotificationCompat.Builder(this, channel)
                .setSmallIcon(R.drawable.hippo)
                .setContentTitle("Content Title")
                .setContentText(msg)
                .setChannelId(channel)
                .setAutoCancel(true)

            notiChannel.notify(9999, notiBuilder.build())
        } else {
            val notiBuilder: NotificationCompat.Builder = NotificationCompat.Builder(this, "")
                .setSmallIcon(R.drawable.hippo)
                .setContentTitle("Content Title")
                .setContentText(msg)
                .setAutoCancel(true)

            val notiChannel: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notiChannel.notify(9999, notiBuilder.build())
        }
    }
}

