package com.example.timeisup.schedule

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.example.timeisup.AndroidThings
import com.example.timeisup.BaseActivity
import com.example.timeisup.R
import com.example.timeisup.scheduleadding.ScheduleAddingActivity
import com.example.timeisup.service.ScheduleEventQueueManager
import com.example.timeisup.task.RescheduleTask
import com.example.timeisup.task.ScheduleAddingTask
import com.example.timeisup.task.ScheduleRemovingTask
import kotlinx.android.synthetic.main.activity_schedule_list.*

class ScheduleListActivity : BaseActivity(), ScheduleListContract.View, View.OnClickListener {
    private val TAG: String = ScheduleListActivity::class.java.simpleName

    override lateinit var mPresenter: ScheduleListContract.Presenter

    lateinit var mRecyclerView: RecyclerView
    lateinit var mAdapter: ScheduleListAdapter
    lateinit var mLayoutManager: RecyclerView.LayoutManager
    lateinit var mGlideRequestManager: RequestManager
    lateinit var mFloatingAction: FloatingActionButton
    lateinit var mDividerItemDecoration: DividerItemDecoration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule_list)
        setSupportActionBar(toolbar_schedule_list as Toolbar)

        Log.d(TAG, "onCreate()")

        init()
    }

    private fun init() {
        mPresenter = ScheduleListPresenter(this)
        mLayoutManager = LinearLayoutManager(this).apply {
            orientation = LinearLayoutManager.VERTICAL
        }
        mDividerItemDecoration = DividerItemDecoration(this, LinearLayoutManager(this).orientation)
        mRecyclerView = rv_schedule_list.apply {
            setHasFixedSize(true)
            layoutManager = mLayoutManager
            addItemDecoration(mDividerItemDecoration)
        }
        mGlideRequestManager = Glide.with(this)
        mFloatingAction = (fab_schedule_list as FloatingActionButton).apply {
            setOnClickListener(this@ScheduleListActivity)
        }

        initAdapter()
        restoreScheduleListObject()
    }

    private fun restoreScheduleListObject() {
        mPresenter.restoreScheduleListObject()
        refreshAdapter()
    }

    override fun onResume() {
        super.onResume()

        Log.d(TAG, "onResume()")

        ScheduleEventQueueManager.mIsScheduleListActivityTop = true
        ScheduleEventQueueManager.mPresenter = mPresenter

        if(ScheduleEventQueueManager.isQueueHasWork()) {
            mPresenter.performScheduleEventWork()
        }
    }

    override fun onPause() {
        super.onPause()

        ScheduleEventQueueManager.mIsScheduleListActivityTop = false
    }

    override fun onDestroy() {
        super.onDestroy()

        Log.d(TAG, "onDestroy()")

        mPresenter.saveScheduleListObject()
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.fab_schedule_list -> {
                Log.d(TAG, "fab clicked!")

                startActivity(Intent(this, ScheduleAddingActivity::class.java))
                overridePendingTransition(R.anim.animation_slide_from_right, R.anim.animation_slide_to_left)
            }
        }
    }

    fun startScheduleAddingActivityToEditSchedule(intent: Intent) {
        intent.component = ComponentName(this, ScheduleAddingActivity::class.java)

        startActivity(intent)
        overridePendingTransition(R.anim.animation_slide_from_right, R.anim.animation_slide_to_left)
    }

    private fun initAdapter() {
        Log.d(TAG, "initAdapter()")

        mAdapter = ScheduleListAdapter(this, mPresenter.getScheduleList(), mGlideRequestManager)
        mRecyclerView.adapter = mAdapter
    }

    fun removeSchedule(key: String?) {
        Log.d(TAG, "removeSchedule()")

        mPresenter.removeScheduleFromDatabase(key)
    }

    override fun refreshAdapter() {
        Log.d(TAG, "refreshAdapter()")

        mAdapter.replaceScheduleList(mPresenter.getScheduleList())
        mAdapter.notifyDataSetChanged()
    }

    override fun getAndroidThings(event: ChildEvent): AndroidThings? {
        var androidThings: AndroidThings? = null

        when(event) {
            ChildEvent.CHILDADDED -> {
                androidThings = getScheduleAddingAndroidThings()
            }
            ChildEvent.CHILDCHANGED -> {
                androidThings = getRescheduleAndroidThings()
            }
            ChildEvent.CHILDMOVED -> {

            }
            ChildEvent.CHILDREMOVED -> {
                androidThings = getScheduleRemovingAndroidThings()
            }
        }

        return androidThings
    }

    private fun getScheduleAddingAndroidThings(): AndroidThings {
        return ScheduleAddingAndroidThings().apply {
            mScheduleListTask = ScheduleAddingTask(mPresenter) // AsyncTask must not be recycled
        }
    }

    private fun getRescheduleAndroidThings(): AndroidThings {
        return RescheduleAndroidThings().apply {
            mScheduleListTask = RescheduleTask(mPresenter)
        }
    }

    private fun getScheduleRemovingAndroidThings(): AndroidThings {
        return ScheduleRemovingAndroidThings().apply {
            mScheduleListTask = ScheduleRemovingTask(mPresenter)
        }
    }
}
