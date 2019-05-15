package com.example.timeisup.schedule

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.example.hclee.lifeguard.BaseView
import com.example.timeisup.R
import kotlinx.android.synthetic.main.activity_schedule_list.*

class ScheduleListActivity : AppCompatActivity(), ScheduleListContract.View {
    private val TAG: String = ScheduleListActivity::class.java.simpleName

    override lateinit var mPresenter: ScheduleListContract.Presenter

    lateinit var mRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule_list)

        Log.d(TAG, "onCreate()")

        mRecyclerView = rv_schedule_list
    }
}
