package com.example.timeisup.schedule

import android.os.AsyncTask
import com.google.firebase.database.DataSnapshot
import java.util.*

/**
 * Created by hclee on 2019-05-30.
 */

abstract class ScheduleListTask(val mPresenter: ScheduleListContract.Presenter): AsyncTask<DataSnapshot, Pair<Schedule, String?>, Unit>() {
    open lateinit var mScheduleList: LinkedList<Pair<Schedule, String?>>
    open lateinit var mConfirmedScheduleList: LinkedList<Pair<Schedule, String?>>
    open lateinit var mNotConfirmedScheduleList: LinkedList<Pair<Schedule, String?>>

    private fun sortScheduleList() {
        mConfirmedScheduleList.sortWith(object: Comparator<Pair<Schedule, String?>> {
            override fun compare(o1: Pair<Schedule, String?>, o2: Pair<Schedule, String?>): Int {
                var ret: Int = 0

                o1.first.getTime()?.run {
                    o2.first.getTime()?.let {
                        if (this > it) {
                            ret = 1
                        }
                        else if(this == it) {
                            ret = 0
                        }
                        else {
                            ret = -1
                        }
                    }
                }

                return ret
            }
        })

        mNotConfirmedScheduleList.sortWith(object: Comparator<Pair<Schedule, String?>> {
            override fun compare(o1: Pair<Schedule, String?>, o2: Pair<Schedule, String?>): Int {
                var ret: Int = 0

                o1.first.getTime()?.run {
                    o2.first.getTime()?.let {
                        if (this > it) {
                            ret = 1
                        }
                        else if(this == it) {
                            ret = 0
                        }
                        else {
                            ret = -1
                        }
                    }
                }

                return ret
            }
        })
    }

    fun mergeScheduleList() {
        sortScheduleList()

        mScheduleList.clear()

        for(item in mConfirmedScheduleList) {
            addScheduleToList(item)
        }

        for(item in mNotConfirmedScheduleList) {
            addScheduleToList(item)
        }
    }

    private fun addScheduleToList(scheduleItem: Pair<Schedule, String?>) {
        mScheduleList.add(scheduleItem)
    }

    override fun onPostExecute(result: Unit?) {
        (mPresenter as ScheduleListPresenter).refreshAdapter()
        mPresenter.makeNotification()
    }
}