package com.example.timeisup.task.taskmanager

import android.util.Log
import com.example.timeisup.schedule.RescheduleAndroidThings
import com.google.firebase.database.DataSnapshot

/**
 * Created by hclee on 2019-05-30.
 */

/**
 * taskItemArray[0] - AndroidThings
 * taskItemArray[1] - DataSnapshot
 */
class RescheduleTaskManager: TaskManager {
    private val TAG: String = RescheduleTaskManager::class.java.simpleName

    override fun runTask(taskItemArray: Array<Any>) {
        Log.d(TAG, "runTask()")

        val androidThings: RescheduleAndroidThings = taskItemArray[0] as RescheduleAndroidThings
        val dataSnapshot: DataSnapshot = taskItemArray[1] as DataSnapshot

        androidThings.mScheduleTask.execute(dataSnapshot)
    }
}