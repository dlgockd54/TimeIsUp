package com.example.timeisup.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log

class BootReceiver : BroadcastReceiver() {
    private val TAG: String = BootReceiver::class.java.simpleName

    override fun onReceive(context: Context, intent: Intent) {
        Log.d(TAG, "onReceive()")

        when(intent.action) {
            Intent.ACTION_BOOT_COMPLETED -> {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(Intent(context, ScheduleEventService::class.java))
                }
            }
        }
    }
}
