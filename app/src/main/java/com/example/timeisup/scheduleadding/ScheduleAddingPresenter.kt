package com.example.timeisup.scheduleadding

import android.util.Log
import com.example.timeisup.firebase.FirebaseManager
import com.example.timeisup.schedule.Schedule

/**
 * Created by hclee on 2019-05-17.
 */

class ScheduleAddingPresenter(private val mView: ScheduleAddingContract.View)
    : ScheduleAddingContract.Presenter {
    private val TAG: String = ScheduleAddingPresenter::class.java.simpleName

    override fun addScheduleToDatabase(schedule: Schedule) {
        Log.d(TAG, "addScheduleToServer()")

        FirebaseManager.addScheduleToDatabase(schedule)
    }
}