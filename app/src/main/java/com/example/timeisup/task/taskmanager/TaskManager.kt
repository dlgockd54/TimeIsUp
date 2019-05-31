package com.example.timeisup.task.taskmanager

import com.example.timeisup.AndroidThings
import com.google.firebase.database.DataSnapshot

/**
 * Created by hclee on 2019-05-30.
 */

object TaskManager {
    fun runTask(taskItemArray: Array<Any>) {
        val androidThings: AndroidThings = taskItemArray[0] as AndroidThings
        val dataSnapshot: DataSnapshot = taskItemArray[1] as DataSnapshot

        androidThings.mScheduleListTask.execute(dataSnapshot)
    }
}