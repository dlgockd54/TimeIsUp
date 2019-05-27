package com.example.timeisup.firebase

import android.util.Log
import com.example.timeisup.schedule.Schedule
import com.example.timeisup.schedule.ScheduleListPresenter
import com.google.firebase.database.*

/**
 * Created by hclee on 2019-05-22.
 */

object FirebaseManager {
    private val TAG: String = FirebaseManager::class.java.simpleName

    private val mFirebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val mDatabaseReference: DatabaseReference = mFirebaseDatabase.reference

    fun addScheduleToDatabase(schedule: Schedule) {
        mDatabaseReference.child(ScheduleListPresenter.DB_NODE_NAME).push().setValue(schedule).let {
            it.addOnSuccessListener {
                Log.d(TAG, "onSuccess()")
            }
            it.addOnFailureListener {
                Log.d(TAG, "onFailure()")
            }
            it.addOnCompleteListener {
                Log.d(TAG, "onComplete()")
            }
            it.addOnCanceledListener {
                Log.d(TAG, "onCanceled()")
            }
        }
    }

    fun addChildEventListener(listener: ChildEventListener) {
        mDatabaseReference.child(ScheduleListPresenter.DB_NODE_NAME).addChildEventListener(listener)
    }

    fun removeChildEventListener(listener: ChildEventListener) {
        mDatabaseReference.child(ScheduleListPresenter.DB_NODE_NAME).removeEventListener(listener)
    }
}