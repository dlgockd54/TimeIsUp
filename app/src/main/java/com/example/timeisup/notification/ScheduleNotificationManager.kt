package com.example.timeisup.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.example.timeisup.MainActivity
import com.example.timeisup.R
import com.example.timeisup.schedule.ChildEvent

/**
 * Created by hclee on 2019-05-31.
 */

object ScheduleNotificationManager {
    private val TAG: String = ScheduleNotificationManager::class.java.simpleName

    private const val NOTIFICATION_TIMEOUT: Long = 1000 * 60 * 30 // Auto cancel notification after 30min
    private var mNotificationManager: NotificationManager? = null
    private var mNotificationBuilder: NotificationCompat.Builder? = null
    private var mNotificationId: Int = 0
    private var mIsSelfTriggered: Boolean = false

    fun makeNotification(context: Context, scheduleName: String?, childEvent: ChildEvent) {
        Log.d(TAG, "makeNotification()")
        Log.d(TAG, "mIsSelfTriggered: $mIsSelfTriggered")

        if(!mIsSelfTriggered) {
            Log.d(TAG, "not triggered myself")

            performNotification(context, scheduleName, childEvent)
        }

        mIsSelfTriggered = false
    }

    fun makeAlarmNotification(context: Context, scheduleName: String?) {
        mNotificationManager = mNotificationManager ?:
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mNotificationBuilder = mNotificationBuilder ?:
                NotificationCompat.Builder(context, "default")
                    .setSmallIcon(R.mipmap.ic_launcher_foreground)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setContentTitle("TimeIsUp")
                    .setAutoCancel(true)
                    .setTimeoutAfter(NOTIFICATION_TIMEOUT)

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mNotificationManager?.createNotificationChannel(NotificationChannel("default",
                "default",
                NotificationManager.IMPORTANCE_DEFAULT))
        }

        mNotificationBuilder?.setContentText("오늘의 일정 ${scheduleName}")
        mNotificationManager?.notify(mNotificationId++, mNotificationBuilder?.build())
    }

    private fun performNotification(context: Context, scheduleName: String?, childEvent: ChildEvent) {
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context,
            222,
            Intent(context, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT)

        mNotificationManager = mNotificationManager ?:
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mNotificationBuilder = mNotificationBuilder ?:
                NotificationCompat.Builder(context, "default")
                    .setSmallIcon(R.mipmap.ic_launcher_foreground)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setContentTitle("TimeIsUp")
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setTimeoutAfter(NOTIFICATION_TIMEOUT)

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mNotificationManager?.createNotificationChannel(NotificationChannel("default",
                "default",
                NotificationManager.IMPORTANCE_DEFAULT))
        }

        when(childEvent) {
            ChildEvent.CHILDADDED -> {
                mNotificationBuilder?.setContentText(context.resources.getString(R.string.added_notification_content)
                        + "($scheduleName)")
            }
            ChildEvent.CHILDCHANGED -> {
                mNotificationBuilder?.setContentText(context.resources.getString(R.string.changed_notification_content)
                        + "($scheduleName)")
            }
            ChildEvent.CHILDREMOVED -> {
                mNotificationBuilder?.setContentText(context.resources.getString(R.string.removed_notification_content)
                        + "($scheduleName)")
            }
            ChildEvent.CHILDMOVED -> {

            }
        }

        mNotificationManager?.notify(mNotificationId++, mNotificationBuilder?.build())
    }

    fun setIsSelfTriggered(isSelfTriggered: Boolean) {
        Log.d(TAG, "setIsSelfTriggered()")

        mIsSelfTriggered = isSelfTriggered
    }
}