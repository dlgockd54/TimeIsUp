package com.example.timeisup.firebase

import android.util.Log
import com.example.timeisup.notification.ScheduleNotificationManager
import com.example.timeisup.schedule.Schedule
import com.example.timeisup.schedule.ScheduleListPresenter
import com.google.android.gms.tasks.OnCanceledListener
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.*

/**
 * Created by hclee on 2019-05-22.
 */

object FirebaseManager {
    private val TAG: String = FirebaseManager::class.java.simpleName

    private val mFirebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val mDatabaseReference: DatabaseReference = mFirebaseDatabase.reference
    private val onSuccessListener: OnSuccessListener<Void> = OnSuccessListener {
        Log.d(TAG, "onSuccess()")
    }
    private val onFailureListener: OnFailureListener = OnFailureListener {
        Log.d(TAG, "onFailure()")
    }
    private val onCompleteListener: OnCompleteListener<Void> = OnCompleteListener {
        Log.d(TAG, "onComplete()")
    }
    private val onCanceledListener: OnCanceledListener = OnCanceledListener {
        Log.d(TAG, "onCanceled()")
    }

    fun addScheduleToDatabase(schedule: Schedule) {
        val addScheduleThread: Thread = Thread(Runnable {
            Log.d(TAG, "run()")

            mDatabaseReference.child(ScheduleListPresenter.DB_NODE_NAME).push().setValue(schedule).let {
                it.addOnSuccessListener(onSuccessListener)
                it.addOnFailureListener(onFailureListener)
                it.addOnCompleteListener(onCompleteListener)
                it.addOnCanceledListener(onCanceledListener)
            }
        })

        ScheduleNotificationManager.setIsSelfTriggered(true)
        addScheduleThread.start()
    }

    fun rescheduleFromDatabase(key: String, schedule: Schedule) {
        val rescheduleThread: Thread = Thread(Runnable {
            Log.d(TAG, "run()")

            val map: HashMap<String, Any> = HashMap<String, Any>().apply {
                put(key, schedule)
            }

            mDatabaseReference.child(ScheduleListPresenter.DB_NODE_NAME).updateChildren(map).let {
                Log.d(TAG, "rescheduleFromDatabase()")
                it.addOnSuccessListener(onSuccessListener)
                it.addOnFailureListener(onFailureListener)
                it.addOnCompleteListener(onCompleteListener)
                it.addOnCanceledListener(onCanceledListener)
            }
        })

        ScheduleNotificationManager.setIsSelfTriggered(true)
        rescheduleThread.start()
    }

    fun removeScheduleFromDatabase(key: String) {
        Log.d(TAG, "removeScheduleFromDatabase()")
        Log.d(TAG, "key: $key")

        val removeScheduleThread: Thread = Thread(Runnable {
            Log.d(TAG, "run()")

            mDatabaseReference.child(ScheduleListPresenter.DB_NODE_NAME).child(key).removeValue().let {
                it.addOnSuccessListener(onSuccessListener)
                it.addOnFailureListener(onFailureListener)
                it.addOnCompleteListener(onCompleteListener)
                it.addOnCanceledListener(onCanceledListener)
            }
        })

        ScheduleNotificationManager.setIsSelfTriggered(true)
        removeScheduleThread.start()
    }

    fun addChildEventListener(listener: ChildEventListener) {
        mDatabaseReference.child(ScheduleListPresenter.DB_NODE_NAME).addChildEventListener(listener)
    }

    fun removeChildEventListener(listener: ChildEventListener) {
        mDatabaseReference.child(ScheduleListPresenter.DB_NODE_NAME).removeEventListener(listener)
    }
}