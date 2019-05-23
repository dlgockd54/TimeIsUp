package com.example.timeisup.schedule

import com.example.hclee.lifeguard.BasePresenter
import com.example.hclee.lifeguard.BaseView
import java.util.*

/**
 * Created by hclee on 2019-05-15.
 */

interface ScheduleListContract {
    interface View: BaseView<Presenter> {
        fun refreshAdapter()
    }

    interface Presenter: BasePresenter {
        fun getScheduleList(): LinkedList<Pair<Schedule, Long>>
        fun addSchedule(schedule: Schedule)
    }
}