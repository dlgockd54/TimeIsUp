package com.example.timeisup.schedule

import android.util.Log
import com.example.timeisup.firebase.FirebaseManager
import com.google.android.gms.maps.model.LatLng
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

            val isConfirmed: Boolean? = dataSnapshot.child("isConfirmed").getValue(Boolean::class.java)
            val latitude: Double? = dataSnapshot.child("latLng").child("latitude").getValue(Double::class.java)
            val longitude: Double? = dataSnapshot.child("latLng").child("longitude").getValue(Double::class.java)
            var latLng: LatLng? = null
            val time: Long? = dataSnapshot.child("time").getValue(Long::class.java)

            latitude?.let {
                longitude?.let {
                    latLng = LatLng(latitude, longitude)
                }
            }

            Log.d(TAG, "isConfirmed: $isConfirmed")
            Log.d(TAG, "latitude: $latitude")
            Log.d(TAG, "longitude: $longitude")
            Log.d(TAG, "latlng: $latLng")
            Log.d(TAG, "time: $time")

            val schedule: Schedule = Schedule(time, latLng, isConfirmed)

            addSchedule(schedule)
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