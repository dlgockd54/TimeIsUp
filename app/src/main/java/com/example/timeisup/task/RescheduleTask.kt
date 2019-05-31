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

class RescheduleTask(presenter: ScheduleListContract.Presenter): ScheduleListTask(presenter) {
    private val TAG: String = RescheduleTask::class.java.simpleName

    override lateinit var mScheduleList: LinkedList<Pair<Schedule, String?>>
    override lateinit var mConfirmedScheduleList: LinkedList<Pair<Schedule, String?>>
    override lateinit var mNotConfirmedScheduleList: LinkedList<Pair<Schedule, String?>>

    override fun onPreExecute() {
        mScheduleList = mPresenter.getScheduleList()
        mConfirmedScheduleList = (mPresenter as ScheduleListPresenter).getConfirmedScheduleList()
        mNotConfirmedScheduleList = mPresenter.getNotConfirmedScheduleList()
    }

    override fun doInBackground(vararg dataSnapshot: DataSnapshot): Unit {
        Log.d(TAG, "doInBackground()")

        onChildChanged(dataSnapshot[0])
    }

    private fun onChildChanged(dataSnapshot: DataSnapshot) {
        Log.d(TAG, "onChildChanged()")

        val key: String? = dataSnapshot.key
        val scheduleName: String? = dataSnapshot.child("scheduleName").getValue(String::class.java)
        val isConfirmed: Boolean? = dataSnapshot.child("isConfirmed").getValue(Boolean::class.java)
        val latitude: Double? = dataSnapshot.child("latitude").getValue(Double::class.java)
        val longitude: Double? = dataSnapshot.child("longitude").getValue(Double::class.java)
        val time: Long? = dataSnapshot.child("time").getValue(Long::class.java)
        val placeName: String? = dataSnapshot.child("placeName").getValue(String::class.java)

        Log.d(TAG, "scheduleName: $scheduleName")
        Log.d(TAG, "isConfirmed: $isConfirmed")
        Log.d(TAG, "latitude: $latitude")
        Log.d(TAG, "longitude: $longitude")
        Log.d(TAG, "time: $time")
        Log.d(TAG, "placeName: $placeName")
        Log.d(TAG, "key: $key ")

        for(i in 0 until mScheduleList.size) {
            Log.d(TAG, "i: $i, key: ${mScheduleList[i].second}")

            val listItem: Pair<Schedule, String?> = mScheduleList[i]

            if (listItem.second == key) {
                listItem.first.let {
                    scheduleName?.run {
                        it.setScheduleName(scheduleName)
                    }
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

                break
            }
        }

        mConfirmedScheduleList.clear()
        mNotConfirmedScheduleList.clear()

        for(listItem in mScheduleList) {
            listItem.first.getIsConfirmed()?.let {
                if(it) {
                    mConfirmedScheduleList.add(listItem)
                }
                else {
                    mNotConfirmedScheduleList.add(listItem)
                }
            }
        }

        mergeScheduleList()
    }

    override fun onProgressUpdate(vararg values: Pair<Schedule, String?>?) {

    }

    override fun onCancelled() {

    }
}