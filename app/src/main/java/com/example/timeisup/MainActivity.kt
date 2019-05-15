package com.example.timeisup

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.timeisup.schedule.ScheduleListActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()

        startActivity(Intent(this, ScheduleListActivity::class.java))
        finish()
    }
}
