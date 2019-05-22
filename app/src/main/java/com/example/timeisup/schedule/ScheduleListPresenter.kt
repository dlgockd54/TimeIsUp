package com.example.timeisup.schedule

import android.util.Log
import com.example.timeisup.firebase.FirebaseManager
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import java.util.*

/**
 * Created by hclee on 2019-05-15.
 */

class ScheduleListPresenter(private val mView: ScheduleListContract.View)
    : ScheduleListContract.Presenter {
    private val TAG: String = ScheduleListPresenter::class.java.simpleName

    private val mScheduleList: LinkedList<Schedule> = LinkedList<Schedule>()
    private val mChildEventListener: ChildEventListener = object: ChildEventListener {
        override fun onCancelled(databaseError: DatabaseError) {
            Log.d(TAG, "onCancelled()")
        }

        override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {
            Log.d(TAG, "onChildMoved()")
        }

        override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {
            Log.d(TAG, "onChildChanged()")
        }

        override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
            Log.d(TAG, "onChildAdded()")

//            var schedule: Schedule? = dataSnapshot.getValue(Schedule::class.java)
//
//            schedule?.let {
//                addSchedule(schedule)
//            }

//            var sampleVal: TestClass? = dataSnapshot.getValue(TestClass::class.java)
//
//            sampleVal?.let {
//                Log.d(TAG, "$sampleVal")
//                Log.d(TAG, it.p0)
//                Log.d(TAG, it.p1)
//                Log.d(TAG, it.p2)
//            }
        }

        override fun onChildRemoved(dataSnapshot: DataSnapshot) {
            Log.d(TAG, "onChildRemoved()")
        }
    }

    init {
        FirebaseManager.addChildEventListener(mChildEventListener)
    }

    override fun getScheduleList(): LinkedList<Schedule> {
        return mScheduleList
    }

    override fun addSchedule(schedule: Schedule) {
        mScheduleList.add(schedule)
        mView.refreshAdapter()
    }
}