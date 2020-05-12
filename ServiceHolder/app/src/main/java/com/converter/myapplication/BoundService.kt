package com.converter.myapplication

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Handler
import android.os.IBinder

class BoundService: Service() {

    private val TAG = "MyService"

    private val mBinder: IBinder = MyBinder()
    private var mHandler: Handler? = null
    private var mProgress = 0
    private var mMaxValue: Int = 0
    private var mIsPaused: Boolean? = null

    class MyBinder : Binder() {
        fun getService(): BoundService {
            return BoundService()
        }
    }

    override fun onCreate() {
        super.onCreate()
        mHandler = Handler()
        mProgress = 0
        mIsPaused = true
        mMaxValue = 5000
    }

    override fun onBind(p0: Intent?): IBinder? {
        return mBinder
    }

    fun getIsPaused(): Boolean? {
        return mIsPaused
    }

    fun getProgress(): Int {
        return mProgress
    }

    fun getMaxValue(): Int {
        return mMaxValue
    }

    fun pausePretendLongRunningTask() {
        mIsPaused = true
    }

    fun unPausePretendLongRunningTask() {
        mIsPaused = false
        startPretendLongRunningTask()
    }

    fun startPretendLongRunningTask() {
        val runnable: Runnable = object : Runnable {
            override fun run() {
                if (mProgress >= mMaxValue || mIsPaused!!) {
                    mHandler!!.removeCallbacks(this) // remove callbacks from runnable
                    pausePretendLongRunningTask()
                } else {
                    mProgress += 100 // increment the progress
                    mHandler!!.postDelayed(this, 100) // continue incrementing
                }
            }
        }
        mHandler!!.postDelayed(runnable, 100)
    }

    fun resetTask() {
        mProgress = 0
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}