package com.example.timeisup

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import com.example.timeisup.schedule.ScheduleListActivity

class MainActivity : BaseActivity() {
    private val REQUEST_CODE: Int = 100
    private val mRequestList: Array<String> = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
    private val TAG: String = MainActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(mRequestList, REQUEST_CODE)
        }

        startActivity(Intent(this, ScheduleListActivity::class.java))
        finish()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        for(result in grantResults) {
            if(result == PackageManager.PERMISSION_DENIED) {
                finish()
            }
        }
    }
}
