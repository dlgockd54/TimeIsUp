package com.example.timeisup.service

import com.example.timeisup.schedule.ChildEvent
import com.google.firebase.database.DataSnapshot

/**
 * Created by hclee on 2019-06-01.
 */

data class ScheduleEventWork(val mChildEvent: ChildEvent, val mAddTime: Long, val mDataSnapshot: DataSnapshot)