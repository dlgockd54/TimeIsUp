package com.example.timeisup.service

import android.content.Context
import android.util.Log
import com.example.timeisup.notification.ScheduleNotificationManager
import com.example.timeisup.schedule.ChildEvent
import com.example.timeisup.schedule.ScheduleListContract

/**
 * Created by hclee on 2019-06-01.
 */

object ScheduleEventQueueManager {
    private val TAG: String = ScheduleEventQueueManager::class.java.simpleName
    private val mScheduleEventQueue: ScheduleEventQueue = ScheduleEventQueue()

    lateinit var mPresenter: ScheduleListContract.Presenter
    var mIsScheduleListActivityTop: Boolean = false

    fun enqueue(context: Context, work: ScheduleEventWork) {
        val childEvent: ChildEvent = work.mChildEvent

        mScheduleEventQueue.enqueue(work)

        ScheduleNotificationManager.makeNotification(context, childEvent)

        Log.d(TAG, "mIsScheduleListActivityTop: $mIsScheduleListActivityTop, isQueueHasWork(): ${isQueueHasWork()}")

        // If schedule change occurs after onResume() of ScheduleListActivity already executed
        // performScheduleEventWork() will not be executed.
        // So execute performScheduleEventWork() manually.
        while(mIsScheduleListActivityTop && isQueueHasWork()) {
            performScheduleEventWork()
        }
    }

    private fun performScheduleEventWork() {
//        mActivity.performScheduleEventWork()
        mPresenter.performScheduleEventWork()
    }

    fun getScheduleEventWork(): ScheduleEventWork = mScheduleEventQueue.getScheduleEventWork()

    private fun isEmpty(): Boolean = mScheduleEventQueue.isEmpty()

    fun isQueueHasWork(): Boolean = !isEmpty()
}