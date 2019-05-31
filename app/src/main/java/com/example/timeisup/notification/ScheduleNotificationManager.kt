package com.example.timeisup.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.example.timeisup.R
import com.example.timeisup.schedule.ScheduleListActivity
import com.example.timeisup.schedule.ScheduleListContract

/**
 * Created by hclee on 2019-05-31.
 */

object ScheduleNotificationManager {
    private val TAG: String = ScheduleNotificationManager::class.java.simpleName

    private var mNotificationManager: NotificationManager? = null
    private var mNotificationBuilder: NotificationCompat.Builder? = null
    private var mNotificationId: Int = 0
    private var mIsSelfTriggered: Boolean = false

    fun makeNotification(view: ScheduleListContract.View) {
        Log.d(TAG, "makeNotification()")
        Log.d(TAG, "mIsSelfTriggered: $mIsSelfTriggered")

        val context: Context = (view as ScheduleListActivity).applicationContext

        if(!mIsSelfTriggered) {
            Log.d(TAG, "not triggered myself")

            mNotificationManager = mNotificationManager ?:
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            mNotificationBuilder = mNotificationBuilder ?:
                    NotificationCompat.Builder(context, "default")
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setContentTitle("TimeIsUp")
                        .setAutoCancel(true)

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mNotificationManager?.createNotificationChannel(NotificationChannel("default",
                    "default",
                    NotificationManager.IMPORTANCE_DEFAULT))
            }

            mNotificationBuilder?.setContentText("변경된 일정을 확인하세요.")
            mNotificationManager?.notify(mNotificationId++, mNotificationBuilder?.build())
        }

        mIsSelfTriggered = false
    }

    fun setIsSelfTriggered(isSelfTriggered: Boolean) {
        Log.d(TAG, "setIsSelfTriggered()")

        mIsSelfTriggered = isSelfTriggered
    }
}