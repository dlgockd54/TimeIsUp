package com.example.timeisup.task

import android.util.Log
import com.example.timeisup.schedule.Schedule
import com.example.timeisup.schedule.ScheduleListContract
import com.example.timeisup.schedule.ScheduleListPresenter
import com.example.timeisup.schedule.ScheduleListTask
import com.google.firebase.database.DataSnapshot
import java.util.*

/**
 * Created by hclee on 2019-05-31.
 */

class ScheduleRemovingTask(private val mPresenter: ScheduleListContract.Presenter): ScheduleListTask() {
    private val TAG: String = ScheduleListTask::class.java.simpleName

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
            onChildRemoved(it)
        }
    }

    private fun onChildRemoved(dataSnapshot: DataSnapshot) {
        val key: String? = dataSnapshot.key

        removeScheduleFromList(key)
    }

    private fun removeScheduleFromList(key: String?) {
        Log.d(TAG, "removeScheduleFromList()")
        Log.d(TAG, "key: $key")

        for(i in 0 until mScheduleList.size) {
            if(mScheduleList[i].second == key) {
                Log.d(TAG, "remove schedule at index $i")

                mScheduleList.removeAt(i)

                break
            }
        }
    }

    override fun onPostExecute(result: Unit?) {
        Log.d(TAG, "onPostExecute()")

        (mPresenter as ScheduleListPresenter).refreshAdapter()
    }
}