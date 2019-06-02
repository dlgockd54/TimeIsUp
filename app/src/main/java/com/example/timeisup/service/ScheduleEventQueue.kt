package com.example.timeisup.service

import android.util.Log
import java.util.*

/**
 * Created by hclee on 2019-06-01.
 */

class ScheduleEventQueue {
    private val TAG: String = ScheduleEventQueue::class.java.simpleName

    private val mQueue: Queue<ScheduleEventWork> = LinkedList<ScheduleEventWork>()

    fun enqueue(work: ScheduleEventWork) {
        Log.d(TAG, "enqueue()")

        mQueue.add(work)

        Log.d(TAG, "queue size: ${mQueue.size}")
    }

    fun getScheduleEventWork(): ScheduleEventWork {
        val eventWork: ScheduleEventWork = mQueue.element()

        dequeue()

        return eventWork
    }

    private fun dequeue() {
        Log.d(TAG, "dequeue()")

        mQueue.remove()
    }

    fun isEmpty(): Boolean = mQueue.isEmpty()
}