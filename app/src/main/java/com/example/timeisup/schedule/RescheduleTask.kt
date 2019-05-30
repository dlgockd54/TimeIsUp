package com.example.timeisup.schedule

import android.util.Log
import com.google.firebase.database.DataSnapshot
import java.util.*

/**
 * Created by hclee on 2019-05-30.
 */

class RescheduleTask(val mPresenter: ScheduleListContract.Presenter): ScheduleListTask() {
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

        val key: String? = dataSnapshot[0].key
        val scheduleName: String? = dataSnapshot[0].child("scheduleName").getValue(String::class.java)
        val isConfirmed: Boolean? = dataSnapshot[0].child("isConfirmed").getValue(Boolean::class.java)
        val latitude: Double? = dataSnapshot[0].child("latitude").getValue(Double::class.java)
        val longitude: Double? = dataSnapshot[0].child("longitude").getValue(Double::class.java)
        val time: Long? = dataSnapshot[0].child("time").getValue(Long::class.java)
        val placeName: String? = dataSnapshot[0].child("placeName").getValue(String::class.java)

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

    override fun onPostExecute(result: Unit?) {
        Log.d(TAG, "onPostExecute()")

        (mPresenter as ScheduleListPresenter).refreshAdapter()
    }
}