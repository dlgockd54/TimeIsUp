package com.example.timeisup.schedule

import java.util.*

/**
 * Created by hclee on 2019-05-15.
 */

class ScheduleListPresenter(private val mView: ScheduleListContract.View)
    : ScheduleListContract.Presenter {
    private val mScheduleList: LinkedList<Schedule> = LinkedList<Schedule>()

    override fun getScheduleList(): LinkedList<Schedule> {
        return mScheduleList
    }

    override fun addSchedule(schedule: Schedule) {
        mScheduleList.add(schedule)
        mView.refreshAdapter()
    }
}