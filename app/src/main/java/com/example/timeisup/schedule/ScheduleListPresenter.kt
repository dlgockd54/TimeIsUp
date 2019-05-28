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
    companion object {
        const val DB_NODE_NAME: String = "schedules"
    }

    private val TAG: String = ScheduleListPresenter::class.java.simpleName
    private val mScheduleList: LinkedList<Pair<Schedule, String?>> = LinkedList<Pair<Schedule, String?>>()
    private val mChildEventListener: ChildEventListener = object: ChildEventListener {
        override fun onCancelled(databaseError: DatabaseError) {
            Log.d(TAG, "onCancelled()")
        }

        override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {
            Log.d(TAG, "onChildMoved()")
        }

        override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {
            Log.d(TAG, "onChildChanged()")

            val isConfirmed: Boolean? = dataSnapshot.child(DB_NODE_NAME).child("isConfirmed").getValue(Boolean::class.java)
            val latitude: Double? = dataSnapshot.child(DB_NODE_NAME).child("latitude").getValue(Double::class.java)
            val longitude: Double? = dataSnapshot.child(DB_NODE_NAME).child("longitude").getValue(Double::class.java)
            val time: Long? = dataSnapshot.child(DB_NODE_NAME).child("time").getValue(Long::class.java)
            val placeName: String? = dataSnapshot.child(DB_NODE_NAME).child("placeName").getValue(String::class.java)
            val key: String? = dataSnapshot.key

            Log.d(TAG, "isConfirmed: $isConfirmed")
            Log.d(TAG, "latitude: $latitude")
            Log.d(TAG, "longitude: $longitude")
            Log.d(TAG, "time: $time")
            Log.d(TAG, "placeName: $placeName")
            Log.d(TAG, "key: $key ")

            for(i in 0 until mScheduleList.size) {
                Log.d(TAG, "i: $i")

                mScheduleList[i].let {
                    if(it.second === key) {
                        it.first.let {
                            time?.run {
                                it.setTime(time)
                            }
                            placeName?.run {
                                it.setPlaceName(placeName)
                            }
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

        /**
         * onChildAdded() function shows added child node under "schedules" node.
         */
        override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
            Log.d(TAG, "onChildAdded()")

            val key: String? = dataSnapshot.key
            val isConfirmed: Boolean? = dataSnapshot.child("isConfirmed").getValue(Boolean::class.java)
            val latitude: Double? = dataSnapshot.child("latitude").getValue(Double::class.java)
            val longitude: Double? = dataSnapshot.child("longitude").getValue(Double::class.java)
            val time: Long? = dataSnapshot.child("time").getValue(Long::class.java)
            val placeName: String? = dataSnapshot.child("placeName").getValue(String::class.java)

            Log.d(TAG, "key: $key")
            Log.d(TAG, "isConfirmed: $isConfirmed")
            Log.d(TAG, "latitude: $latitude")
            Log.d(TAG, "longitude: $longitude")
            Log.d(TAG, "time: $time")
            Log.d(TAG, "placeName: $placeName")

            val schedule: Schedule = Schedule(time, placeName, latitude, longitude, isConfirmed)

            addSchedule(schedule, key)
        }

        override fun onChildRemoved(dataSnapshot: DataSnapshot) {
            Log.d(TAG, "onChildRemoved()")
        }
    }

    init {
        FirebaseManager.addChildEventListener(mChildEventListener)
    }

    override fun getScheduleList(): LinkedList<Pair<Schedule, String?>> {
        return mScheduleList
    }

    override fun addSchedule(schedule: Schedule, key: String?) {
        mScheduleList.add(Pair(schedule, key))
        mView.refreshAdapter()
    }
}