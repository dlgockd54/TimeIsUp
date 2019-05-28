package com.example.timeisup.scheduleadding

import com.example.hclee.lifeguard.BasePresenter
import com.example.hclee.lifeguard.BaseView
import com.example.timeisup.schedule.Schedule

/**
 * Created by hclee on 2019-05-17.
 */

interface ScheduleAddingContract {
    interface View: BaseView<Presenter> {

    }

    interface Presenter: BasePresenter {
        fun addScheduleToDatabase(schedule: Schedule)
        fun reschedule(key: String, schedule: Schedule)
    }
}