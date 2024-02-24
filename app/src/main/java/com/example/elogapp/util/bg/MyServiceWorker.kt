package com.example.elogapp.util.bg

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.work.Worker
import androidx.work.WorkerParameters


class MyServiceWorker(private val context: Context, params: WorkerParameters) : Worker(context, params) {
    private val TAG = "MyServiceWorker"


    override fun doWork(): Result {
        Log.d(TAG, "doWork called for: " + this.id)
        Log.d(TAG, "Service Running: " + MyBgService?.isServiceRunning)
        try {
            if (!MyBgService?.isServiceRunning!!) {
                Log.d(TAG, "starting service from doWork")
                val intent = Intent(context, MyBgService::class.java)
                if(intent != null) {
                    //ContextCompat.startForegroundService(context, intent)
                }
            }
        }catch (e: Exception){
            e.printStackTrace()
        }
        return Result.success()
    }

    override fun onStopped() {
        Log.d(TAG, "onStopped called for: " + this.id)
        super.onStopped()
    }
}