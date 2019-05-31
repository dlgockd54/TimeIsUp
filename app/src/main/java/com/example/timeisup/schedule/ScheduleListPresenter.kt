package com.example.timeisup.schedule

import android.util.Log
import com.example.timeisup.firebase.FirebaseManager
import com.example.timeisup.task.taskmanager.RescheduleTaskManager
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
    private val mTaskManager: TaskManager = RescheduleTaskManager()
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

                mTaskManager.runTask(taskItemArray)
            }
        }

        /**
         * onChildAdded() function shows added child node under "schedules" node.
         */
        override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
            Log.d(TAG, "onChildAdded()")

            val key: String? = dataSnapshot.key
            val scheduleName: String? = dataSnapshot.child("scheduleName").getValue(String::class.java)
            val isConfirmed: Boolean? = dataSnapshot.child("isConfirmed").getValue(Boolean::class.java)
            val latitude: Double? = dataSnapshot.child("latitude").getValue(Double::class.java)
            val longitude: Double? = dataSnapshot.child("longitude").getValue(Double::class.java)
            val time: Long? = dataSnapshot.child("time").getValue(Long::class.java)
            val placeName: String? = dataSnapshot.child("placeName").getValue(String::class.java)

            Log.d(TAG, "key: $key")
            Log.d(TAG, "scheduleName: $scheduleName")
            Log.d(TAG, "isConfirmed: $isConfirmed")
            Log.d(TAG, "latitude: $latitude")
            Log.d(TAG, "longitude: $longitude")
            Log.d(TAG, "time: $time")
            Log.d(TAG, "placeName: $placeName")

            val schedule: Schedule = Schedule(scheduleName, time, placeName, latitude, longitude, isConfirmed)

            isConfirmed?.let {
                if(it) {
                    addConfirmedScheduleToList(schedule, key)
                }
                else {
                    addNotConfirmedScheduleToList(schedule, key)
                }
            }

            mergeScheduleList()
        }

        override fun onChildRemoved(dataSnapshot: DataSnapshot) {
            Log.d(TAG, "onChildRemoved()")

            val key: String? = dataSnapshot.key

            onScheduleRemovedFromDatabase(key)
        }
    }

    init {
        FirebaseManager.addChildEventListener(mChildEventListener)
    }

    override fun getScheduleList(): LinkedList<Pair<Schedule, String?>> {
        return mScheduleList
    }

    private fun addScheduleToList(scheduleItem: Pair<Schedule, String?>) {
        mScheduleList.add(scheduleItem)
    }

    private fun sortScheduleList() {
        mConfirmedScheduleList.sortWith(object: Comparator<Pair<Schedule, String?>> {
            override fun compare(o1: Pair<Schedule, String?>, o2: Pair<Schedule, String?>): Int {
                var ret: Int = 0

                o1.first.getTime()?.run {
                    o2.first.getTime()?.let {
                        if (this > it) {
                            ret = 1
                        }
                        else if(this == it) {
                            ret = 0
                        }
                        else {
                            ret = -1
                        }
                    }
                }

                return ret
            }
        })

        mNotConfirmedScheduleList.sortWith(object: Comparator<Pair<Schedule, String?>> {
            override fun compare(o1: Pair<Schedule, String?>, o2: Pair<Schedule, String?>): Int {
                var ret: Int = 0

                o1.first.getTime()?.run {
                    o2.first.getTime()?.let {
                        if (this > it) {
                            ret = 1
                        }
                        else if(this == it) {
                            ret = 0
                        }
                        else {
                            ret = -1
                        }
                    }
                }

                return ret
            }
        })
    }

    private fun mergeScheduleList() {
        sortScheduleList()

        mScheduleList.clear()

        for(item in mConfirmedScheduleList) {
            addScheduleToList(item)
        }

        for(item in mNotConfirmedScheduleList) {
            addScheduleToList(item)
        }

        mView.refreshAdapter()
    }

    private fun addConfirmedScheduleToList(schedule: Schedule, key: String?) {
        Log.d(TAG, "addConfirmedScheduleToList(), key: $key")

        mConfirmedScheduleList.add(Pair(schedule, key))
    }

    private fun addNotConfirmedScheduleToList(schedule: Schedule, key: String?) {
        Log.d(TAG, "addNotConfirmedScheduleToList(), key: $key")

        mNotConfirmedScheduleList.add(Pair(schedule, key))
    }

    override fun removeScheduleFromDatabase(key: String?) {
        Log.d(TAG, "removeSchedule()")

        key?.let {
            FirebaseManager.removeScheduleFromDatabase(it)
        }
    }

    private fun onScheduleRemovedFromDatabase(key: String?) {
        Log.d(TAG, "removeScheduleFromList()")
        Log.d(TAG, "key: $key")

        for(i in 0 until mScheduleList.size) {
            if(mScheduleList[i].second == key) {
                Log.d(TAG, "remove schedule at index $i")

                mScheduleList.removeAt(i)

                break
            }
        }

        mView.refreshAdapter()
    }

    fun getConfirmedScheduleList(): LinkedList<Pair<Schedule, String?>> = mConfirmedScheduleList

    fun getNotConfirmedScheduleList(): LinkedList<Pair<Schedule, String?>> = mNotConfirmedScheduleList

    fun refreshAdapter() {
        mView.refreshAdapter()
    }
}