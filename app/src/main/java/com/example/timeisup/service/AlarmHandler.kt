package com.example.timeisup.service

import android.app.AlarmManager
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import com.example.timeisup.firebase.FirebaseManager
import com.example.timeisup.notification.ScheduleNotificationManager
import com.example.timeisup.schedule.Schedule
import java.util.*

/**
 * Created by hclee on 2019-06-04.
 */

interface Listener {
    fun onScheduleMade(schedule: Schedule?)
}

class AlarmHandler(private val mContext: Context, looper: Looper): Handler(looper) {
    companion object {
        const val MSG_ALARM: Int = 0
    }

    private val TAG: String = AlarmHandler::class.java.simpleName
    private val mListener: Listener = object: Listener {
        override fun onScheduleMade(schedule: Schedule?) {
            Log.d(TAG, "onScheduleMade()")

            val scheduleName: String? = schedule?.getScheduleName()
            val calendar: Calendar = Calendar.getInstance()

            schedule?.getTime()?.let {
                val scheduleTime: Calendar = Calendar.getInstance().apply {
                    timeInMillis = it
                }

                if (calendar.get(Calendar.DAY_OF_MONTH) == scheduleTime.get(Calendar.DAY_OF_MONTH)) {
                    ScheduleNotificationManager.makeAlarmNotification(mContext, scheduleName)
                }
            }
        }
    }

    override fun handleMessage(msg: Message?) {
        Log.d(TAG, "handleMessage()")

        when(msg?.what) {
            MSG_ALARM -> {
                FirebaseManager.makeScheduleFromDatabase(mListener)
            }
        }
    }
}