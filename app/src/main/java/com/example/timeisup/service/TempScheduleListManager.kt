package com.example.timeisup.service

import android.util.Log
import com.example.timeisup.schedule.Schedule
import com.example.timeisup.schedule.ScheduleListPresenter
import java.util.*

/**
 * Created by hclee on 2019-06-02.
 */

object TempScheduleListManager {
    private val TAG: String = TempScheduleListManager::class.java.simpleName
    private var mScheduleList: LinkedList<Pair<Schedule, String?>> = LinkedList<Pair<Schedule, String?>>()

    fun saveScheduleListObject(scheduleList: LinkedList<Pair<Schedule, String?>>) {
        Log.d(TAG, "saveScheduleListObject()")

        mScheduleList = scheduleList

        Log.d(TAG, "schedule list size: ${mScheduleList.size}")
    }

    fun restoreScheduleObject(presenter: ScheduleListPresenter) {
        Log.d(TAG, "restoreScheduleListObject()")
        Log.d(TAG, "tempList.size: ${mScheduleList.size}")

        presenter.setScheduleListToRestore(mScheduleList)
    }
}