package com.example.timeisup.service

import com.example.timeisup.schedule.Schedule
import com.example.timeisup.schedule.ScheduleListPresenter
import java.util.*

/**
 * Created by hclee on 2019-06-02.
 */

object TempScheduleListManager {
    private var mScheduleList: LinkedList<Pair<Schedule, String?>> = LinkedList<Pair<Schedule, String?>>()

    fun saveSchedule(scheduleList: LinkedList<Pair<Schedule, String?>>) {
        mScheduleList = scheduleList
    }

//    fun restoreSchedule(presenter: ScheduleListPresenter) {
//        presenter.
//    }
}