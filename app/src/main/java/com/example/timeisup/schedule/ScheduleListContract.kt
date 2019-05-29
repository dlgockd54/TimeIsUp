package com.example.timeisup.schedule

import com.example.hclee.lifeguard.BasePresenter
import com.example.hclee.lifeguard.BaseView
import com.example.timeisup.AndroidThings
import com.google.firebase.database.DataSnapshot
import java.util.*

/**
 * Created by hclee on 2019-05-15.
 */

interface ScheduleListContract {
    interface View: BaseView<Presenter> {
        fun refreshAdapter()
        fun getAndroidThings(): AndroidThings
    }

    interface Presenter: BasePresenter {
        fun getScheduleList(): LinkedList<Pair<Schedule, String?>>
        fun removeScheduleFromDatabase(key: String?)
    }
}