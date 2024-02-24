package com.example.elogapp.util.bg

import android.app.*
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.elogapp.R
import com.example.elogapp.activity.MainActivity
import com.example.elogapp.util.AppUtils


class MyBgService : Service() {
    private val TAG = "MyBgService"
    private val CHANNEL_ID = "NOTIFICATION_CHANNEL"

    override fun onCreate() {
        super.onCreate()

        isServiceRunning = true
        AppUtils.logger("MyBgService Created")
        createNotificationChannel()

    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        AppUtils.logger("onStartCommand 1")
        AppUtils.logger("onStartCommand called in My Service")
        val notificationIntent = Intent(this, MainActivity::class.java)
        AppUtils.logger("onStartCommand 2")

        val pendingIntent: PendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(
                this,
                0,
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            )
        } else {
            PendingIntent.getActivity(
                this,
                0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
        AppUtils.logger("onStartCommand 3")
        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("MyBgService is Running in My Service")
            .setContentText("Listening for Screen Off/On events")
            .setSmallIcon(R.drawable.ic_pickup_icon)
            .setContentIntent(pendingIntent)
            .setColor(resources.getColor(R.color.colorPrimary))
            .build()
        AppUtils.logger("onStartCommand 4")
        AppUtils.logger("StartFOreGround Service in My Service")
        startForeground(1, notification)
        AppUtils.logger("onStartCommand 5")
        return START_STICKY
    }

    private fun createNotificationChannel() {
        try {
            AppUtils.logger("MyBgService Notification CreateNotificationChannel create")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val appName = getString(R.string.app_name)
                val serviceChannel = NotificationChannel(
                    CHANNEL_ID,
                    appName,
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                val manager = getSystemService(
                    NotificationManager::class.java
                )
                manager.createNotificationChannel(serviceChannel)
            }

            AppUtils.logger("MyBgService Notification CreateNotificationChannel created")
        }catch (e: Exception){
            e.printStackTrace()

        }
    }

    override fun onDestroy() {
        //Log.d(TAG, "onDestroy called")
        AppUtils.logger("onDestroy called in MyBgService")
        isServiceRunning = false
        stopForeground(true)

        // call MyReceiver which will restart this service via a worker
        val broadcastIntent = Intent(this, MyReceiver::class.java)
        sendBroadcast(broadcastIntent)
        super.onDestroy()
    }

    companion object {
        var isServiceRunning: Boolean = false
    }

//    init {
//        Log.d(TAG, "constructor called MyBgService")
//        isServiceRunning = false
//    }
}