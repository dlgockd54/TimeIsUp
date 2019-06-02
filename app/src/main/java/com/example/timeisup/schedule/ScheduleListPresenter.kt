package com.example.timeisup.schedule

import android.util.Log
import com.example.timeisup.firebase.FirebaseManager
import com.example.timeisup.notification.ScheduleNotificationManager
import com.example.timeisup.service.ScheduleEventQueueManager
import com.example.timeisup.service.ScheduleEventWork
import com.example.timeisup.service.TempScheduleListManager
import com.example.timeisup.task.taskmanager.TaskManager
import com.google.firebase.database.DataSnapshot
import java.util.*

/**
 * Created by hclee on 2019-05-15.
 */

class ScheduleListPresenter(private val mView: ScheduleListContract.View)
    : ScheduleListContract.Presenter {
    companion object {
        const val DB_NODE_NAME: String = "schedules"
    }

    private val TAG: String = ScheduleListPresenter::class.java.simpleName
    private var mScheduleList: LinkedList<Pair<Schedule, String?>> = LinkedList<Pair<Schedule, String?>>()
    private val mNotConfirmedScheduleList: LinkedList<Pair<Schedule, String?>> = LinkedList<Pair<Schedule, String?>>()
    private val mConfirmedScheduleList: LinkedList<Pair<Schedule, String?>> = LinkedList<Pair<Schedule, String?>>()

    override fun restoreScheduleListObject() {
        TempScheduleListManager.restoreScheduleObject(this@ScheduleListPresenter)
    }

    override fun performScheduleEventWork() {
        Log.d(TAG, "performScheduleEventWork()")

        while(ScheduleEventQueueManager.isQueueHasWork()) {
            val scheduleEventWork: ScheduleEventWork = ScheduleEventQueueManager.getScheduleEventWork()
            val dataSnapshot: DataSnapshot = scheduleEventWork.mDataSnapshot

            when(scheduleEventWork.mChildEvent) {
                ChildEvent.CHILDADDED -> {
                    mView.getAndroidThings(ChildEvent.CHILDADDED)?.let {
                        val taskItemArray: Array<Any> = arrayOf(it, dataSnapshot)
                        TaskManager.runTask(taskItemArray)
                    }
                }
                ChildEvent.CHILDCHANGED -> {
                    mView.getAndroidThings(ChildEvent.CHILDCHANGED)?.let {
                        val taskItemArray: Array<Any> = arrayOf(it, dataSnapshot)
                        TaskManager.runTask(taskItemArray)
                    }
                }
                ChildEvent.CHILDREMOVED -> {
                    mView.getAndroidThings(ChildEvent.CHILDREMOVED)?.let {
                        val taskItemArray: Array<Any> = arrayOf(it, dataSnapshot)
                        TaskManager.runTask(taskItemArray)
                    }
                }
                ChildEvent.CHILDMOVED -> {

                }
            }
        }
    }

    override fun saveScheduleListObject() {
        TempScheduleListManager.saveScheduleListObject(mScheduleList)
    }

    fun makeNotification() {
        ScheduleNotificationManager.makeNotification(mView)
    }

    fun setScheduleListToRestore(scheduleList: LinkedList<Pair<Schedule, String?>>) {
        mScheduleList = scheduleList

        Log.d(TAG, "mScheduleList.size: ${mScheduleList.size}")
    }

    override fun getScheduleList(): LinkedList<Pair<Schedule, String?>> {
        return mScheduleList
    }

    override fun removeScheduleFromDatabase(key: String?) {
        Log.d(TAG, "removeSchedule()")

        key?.let {
            FirebaseManager.removeScheduleFromDatabase(it)
        }
    }

    fun getConfirmedScheduleList(): LinkedList<Pair<Schedule, String?>> = mConfirmedScheduleList

    fun getNotConfirmedScheduleList(): LinkedList<Pair<Schedule, String?>> = mNotConfirmedScheduleList

    fun refreshAdapter() {
        mView.refreshAdapter()
    }
}