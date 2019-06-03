package com.example.timeisup

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import com.example.timeisup.schedule.ScheduleListActivity
import com.example.timeisup.service.ScheduleEventService

class MainActivity : BaseActivity() {
    private val REQUEST_CODE: Int = 100
    private val mRequestList: Array<String> = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
    private val TAG: String = MainActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG, "onCreate()")

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.d(TAG, "requestPermissions()")

            requestPermissions(mRequestList, REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        Log.d(TAG, "onRequestPermissionsResult()")

        if(requestCode == REQUEST_CODE) {
            for (result in grantResults) {
                Log.d(TAG, "result: $result")

                if (result == PackageManager.PERMISSION_DENIED) {
                    return
                }
                else if (result == PackageManager.PERMISSION_GRANTED) {
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        startForegroundService(Intent(applicationContext, ScheduleEventService::class.java))
                    }
                    else {
                        startService(Intent(applicationContext, ScheduleEventService::class.java))
                    }

                    startActivity(Intent(this, ScheduleListActivity::class.java))
                    finish()
                }
            }
        }
    }
}
