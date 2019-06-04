package com.example.timeisup.service

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.os.*
import android.util.Log
import com.example.timeisup.firebase.FirebaseManager
import com.example.timeisup.schedule.ChildEvent
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import java.util.*

class ScheduleEventService : Service() {
    private val TAG: String = ScheduleEventService::class.java.simpleName
    private lateinit var mAlarmHandler: AlarmHandler
    private lateinit var mAlarmThread: Thread
    private var mIsThreadRunning: Boolean = false
    private val mChildEventListener: ChildEventListener = object: ChildEventListener {
        override fun onCancelled(databaseError: DatabaseError) {
            Log.d(TAG, "onCancelled()")
        }

        override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {
            Log.d(TAG, "onChildMoved()")

        }

        override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {
            Log.d(TAG, "onChildChanged()")

            addWorkToQueue(ChildEvent.CHILDCHANGED, dataSnapshot)
        }

        /**
         * onChildAdded() function shows added child node under "schedules" node.
         */
        override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
            Log.d(TAG, "onChildAdded()")

            addWorkToQueue(ChildEvent.CHILDADDED, dataSnapshot)
        }

        override fun onChildRemoved(dataSnapshot: DataSnapshot) {
            Log.d(TAG, "onChildRemoved()")

            addWorkToQueue(ChildEvent.CHILDREMOVED, dataSnapshot)
        }
    }

    private fun addWorkToQueue(event: ChildEvent, dataSnapshot: DataSnapshot) {
        ScheduleEventQueueManager.enqueue(this, ScheduleEventWork(event, Date().time, dataSnapshot))
    }

    override fun onCreate() {
        Log.d(TAG, "onCreate()")

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForeground(1, Notification())
        }

        FirebaseManager.addChildEventListener(mChildEventListener)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand()")

        mAlarmHandler = AlarmHandler(applicationContext, Looper.getMainLooper())
        mAlarmThread = Thread(Runnable {
            Log.d(TAG, "run()")

            while(mIsThreadRunning) {
                val calendar: Calendar = Calendar.getInstance()
                val message: Message = mAlarmHandler.obtainMessage().apply {
                    what = AlarmHandler.MSG_ALARM
                }

                if (calendar.get(Calendar.HOUR_OF_DAY) == 0 || calendar.get(Calendar.HOUR_OF_DAY) == 24) {
                    if(calendar.get(Calendar.MINUTE) in 0..5) {
                        Log.d(TAG, "minute: ${calendar.get(Calendar.MINUTE)}")

                        mAlarmHandler.sendMessage(message)
                    }
                }

                Thread.sleep(1000 * 60 * 5)
            }
        })

        mIsThreadRunning = true
        mAlarmThread.start()

        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy()")

        mIsThreadRunning = false

        FirebaseManager.removeChildEventListener(mChildEventListener)
    }
}
