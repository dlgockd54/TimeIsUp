package com.example.timeisup.schedule

import android.util.Log
import com.example.timeisup.firebase.FirebaseManager
import com.example.timeisup.task.taskmanager.TaskManager
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
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
    private val mChildEventListener: ChildEventListener = object: ChildEventListener {
        override fun onCancelled(databaseError: DatabaseError) {
            Log.d(TAG, "onCancelled()")
        }

        override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {
            Log.d(TAG, "onChildMoved()")
        }

        override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {
            Log.d(TAG, "onChildChanged()")

            mView.getAndroidThings(ChildEvent.CHILDCHANGED)?.let {
                val taskItemArray: Array<Any> = arrayOf(it, dataSnapshot)

                TaskManager.runTask(taskItemArray)
            }
        }

        /**
         * onChildAdded() function shows added child node under "schedules" node.
         */
        override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
            Log.d(TAG, "onChildAdded()")

            mView.getAndroidThings(ChildEvent.CHILDADDED)?.let {
                val taskItemArray: Array<Any> = arrayOf(it, dataSnapshot)

                TaskManager.runTask(taskItemArray)
            }
        }

        override fun onChildRemoved(dataSnapshot: DataSnapshot) {
            Log.d(TAG, "onChildRemoved()")

            mView.getAndroidThings(ChildEvent.CHILDREMOVED)?.let {
                val taskItemArray: Array<Any> = arrayOf(it, dataSnapshot)

                TaskManager.runTask(taskItemArray)
            }
        }
    }

    init {
        FirebaseManager.addChildEventListener(mChildEventListener)
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