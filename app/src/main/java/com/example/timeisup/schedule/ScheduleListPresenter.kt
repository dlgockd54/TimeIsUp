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

    private val mScheduleList: LinkedList<Pair<Schedule, Long>> = LinkedList<Pair<Schedule, Long>>()
    private val mChildEventListener: ChildEventListener = object: ChildEventListener {
        override fun onCancelled(databaseError: DatabaseError) {
            Log.d(TAG, "onCancelled()")
        }

        override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {
            Log.d(TAG, "onChildMoved()")
        }

        override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {
            Log.d(TAG, "onChildChanged()")

            val isConfirmed: Boolean? = dataSnapshot.child("isConfirmed").getValue(Boolean::class.java)
            val latitude: Double? = dataSnapshot.child("latitude").getValue(Double::class.java)
            val longitude: Double? = dataSnapshot.child("longitude").getValue(Double::class.java)
            val time: Long? = dataSnapshot.child("time").getValue(Long::class.java)

            Log.d(TAG, "isConfirmed: $isConfirmed")
            Log.d(TAG, "latitude: $latitude")
            Log.d(TAG, "longitude: $longitude")
            Log.d(TAG, "time: $time")

            for(i in 0 until mScheduleList.size) {
                Log.d(TAG, "i: $i")

                mScheduleList[i].let {
                    if(it.second == time) {
                        it.first.let {
                            it.setTime(time)

                            latitude?.run {
                                it.setLatitude(latitude)
                            }

                            longitude?.run {
                                it.setLongitude(longitude)
                            }

                            isConfirmed?.run {
                                it.setIsConfirmed(isConfirmed)
                            }
                        }
                    }
                }
            }
        }

        override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
            Log.d(TAG, "onChildAdded()")

            val isConfirmed: Boolean? = dataSnapshot.child("isConfirmed").getValue(Boolean::class.java)
            val latitude: Double? = dataSnapshot.child("latitude").getValue(Double::class.java)
            val longitude: Double? = dataSnapshot.child("longitude").getValue(Double::class.java)
            val time: Long? = dataSnapshot.child("time").getValue(Long::class.java)

            Log.d(TAG, "isConfirmed: $isConfirmed")
            Log.d(TAG, "latitude: $latitude")
            Log.d(TAG, "longitude: $longitude")
            Log.d(TAG, "time: $time")

            val schedule: Schedule = Schedule(time, latitude, longitude, isConfirmed)

            addSchedule(schedule)
        }

        override fun onChildRemoved(dataSnapshot: DataSnapshot) {
            Log.d(TAG, "onChildRemoved()")
        }
    }

    init {
        FirebaseManager.addChildEventListener(mChildEventListener)
    }

    override fun getScheduleList(): LinkedList<Pair<Schedule, Long>> {
        return mScheduleList
    }

    override fun addSchedule(schedule: Schedule) {
        schedule.getTime()?.let {
            Log.d(TAG, "soon add schedule to list")

            mScheduleList.add(Pair(schedule, it))
        }

        mView.refreshAdapter()
    }
}