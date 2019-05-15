package com.example.timeisup.schedule

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.timeisup.R

class ScheduleListActivity : AppCompatActivity() {
    private val TAG: String = ScheduleListActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule_list)
    }
}
