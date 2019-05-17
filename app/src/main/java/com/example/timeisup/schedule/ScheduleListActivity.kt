package com.example.timeisup.schedule

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.example.timeisup.BaseActivity
import com.example.timeisup.R
import kotlinx.android.synthetic.main.activity_schedule_list.*
import java.util.*

class ScheduleListActivity : BaseActivity(), ScheduleListContract.View {
    private val TAG: String = ScheduleListActivity::class.java.simpleName

    override lateinit var mPresenter: ScheduleListContract.Presenter

    lateinit var mRecyclerView: RecyclerView
    lateinit var mAdapter: ScheduleListAdapter
    lateinit var mLayoutManager: RecyclerView.LayoutManager
    lateinit var mGlideRequestManager: RequestManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule_list)

        Log.d(TAG, "onCreate()")

        init()
        addSchedule()
    }

    private fun init() {
        mPresenter = ScheduleListPresenter(this)
        mLayoutManager = LinearLayoutManager(this).apply {
            orientation = LinearLayoutManager.VERTICAL
        }
        mRecyclerView = rv_schedule_list.apply {
            setHasFixedSize(true)
            layoutManager = mLayoutManager
        }
        mGlideRequestManager = Glide.with(this)

        initAdapter()
    }

    private fun initAdapter() {
        Log.d(TAG, "initAdapter()")

        mAdapter = ScheduleListAdapter(mPresenter.getScheduleList(), mGlideRequestManager)
        mRecyclerView.adapter = mAdapter
    }

    override fun refreshAdapter() {
        Log.d(TAG, "refreshAdapter()")

        mAdapter.replaceScheduleList(mPresenter.getScheduleList())
        mAdapter.notifyDataSetChanged()
    }

    fun addSchedule() {
        Log.d(TAG, "addSchedule()")

        mPresenter.addSchedule(Schedule(Date(), "집"))
    }
}
