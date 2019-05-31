package com.example.timeisup.task

import android.util.Log
import com.example.timeisup.schedule.Schedule
import com.example.timeisup.schedule.ScheduleListContract
import com.example.timeisup.schedule.ScheduleListPresenter
import com.example.timeisup.schedule.ScheduleListTask
import com.google.firebase.database.DataSnapshot
import java.util.*

/**
 * Created by hclee on 2019-05-30.
 */

class ScheduleAddingTask(presenter: ScheduleListContract.Presenter): ScheduleListTask(presenter) {
    private val TAG: String = ScheduleAddingTask::class.java.simpleName

    override lateinit var mScheduleList: LinkedList<Pair<Schedule, String?>>
    override lateinit var mConfirmedScheduleList: LinkedList<Pair<Schedule, String?>>
    override lateinit var mNotConfirmedScheduleList: LinkedList<Pair<Schedule, String?>>

    override fun onPreExecute() {
        mScheduleList = mPresenter.getScheduleList()
        mConfirmedScheduleList = (mPresenter as ScheduleListPresenter).getConfirmedScheduleList()
        mNotConfirmedScheduleList = mPresenter.getNotConfirmedScheduleList()
    }

    override fun doInBackground(vararg params: DataSnapshot?) {
        Log.d(TAG, "doInBackground()")

        params[0]?.let {
            onChildAdded(it)
        }
    }

    /**
     * onChildAdded() function shows added child node under "schedules" node.
     */
    private fun onChildAdded(dataSnapshot: DataSnapshot) {
        val key: String? = dataSnapshot.key
        val scheduleName: String? = dataSnapshot.child("scheduleName").getValue(String::class.java)
        val isConfirmed: Boolean? = dataSnapshot.child("isConfirmed").getValue(Boolean::class.java)
        val latitude: Double? = dataSnapshot.child("latitude").getValue(Double::class.java)
        val longitude: Double? = dataSnapshot.child("longitude").getValue(Double::class.java)
        val time: Long? = dataSnapshot.child("time").getValue(Long::class.java)
        val placeName: String? = dataSnapshot.child("placeName").getValue(String::class.java)

        Log.d(TAG, "key: $key")
        Log.d(TAG, "scheduleName: $scheduleName")
        Log.d(TAG, "isConfirmed: $isConfirmed")
        Log.d(TAG, "latitude: $latitude")
        Log.d(TAG, "longitude: $longitude")
        Log.d(TAG, "time: $time")
        Log.d(TAG, "placeName: $placeName")

        val schedule: Schedule = Schedule(scheduleName, time, placeName, latitude, longitude, isConfirmed)

        isConfirmed?.let {
            if(it) {
                addConfirmedScheduleToList(schedule, key)
            }
            else {
                addNotConfirmedScheduleToList(schedule, key)
            }
        }

        mergeScheduleList()
    }

    private fun addConfirmedScheduleToList(schedule: Schedule, key: String?) {
        Log.d(TAG, "addConfirmedScheduleToList(), key: $key")

        mConfirmedScheduleList.add(Pair(schedule, key))
    }

    private fun addNotConfirmedScheduleToList(schedule: Schedule, key: String?) {
        Log.d(TAG, "addNotConfirmedScheduleToList(), key: $key")

        mNotConfirmedScheduleList.add(Pair(schedule, key))
    }

    override fun onProgressUpdate(vararg values: Pair<Schedule, String?>?) {

    }

    override fun onCancelled() {

    }
}