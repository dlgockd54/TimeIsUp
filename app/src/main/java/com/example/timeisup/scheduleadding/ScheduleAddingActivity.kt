package com.example.timeisup.scheduleadding

import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.util.Log
import com.example.timeisup.BaseActivity
import com.example.timeisup.R
import kotlinx.android.synthetic.main.activity_schedule_adding.*

class ScheduleAddingActivity : BaseActivity(), ScheduleAddingContract.View {
    private val TAG: String = ScheduleAddingActivity::class.java.simpleName

    override lateinit var mPresenter: ScheduleAddingContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule_adding)
        setSupportActionBar(toolbar_schedule_adding as Toolbar)

        Log.d(TAG, "onCreate()")
    }

    override fun onBackPressed() {
        super.onBackPressed()

        Log.d(TAG, "onBackPressed()")
        overridePendingTransition(R.anim.animation_slide_from_left, R.anim.animation_slide_to_right)
    }
}
