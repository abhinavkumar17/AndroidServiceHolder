package com.converter.myapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    // UI Components
    private lateinit var mProgressBar: ProgressBar
    private lateinit var mTextView: TextView
    private lateinit var mButton: Button

    // Vars
    private lateinit var mService: BoundService
    private lateinit var model: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mProgressBar = findViewById(R.id.progresss_bar)
        mTextView = findViewById(R.id.text_view)
        mButton = findViewById(R.id.toggle_updates)

        model = ViewModelProvider(this).get(MainActivityViewModel::class.java)

        setObservers()
    }

    private fun setObservers() {

       model.getBinder().observe(this, Observer {
           if (it == null) {
               Log.d(TAG, "onChanged: unbound from service")
           } else {
               Log.d(TAG, "onChanged: bound to service.")
               mService = it.getService()
           }
       })

        /*model.getIsProgressBarUpdating().observe(this, Observer {
            val handler = Handler()
            val runnable: Runnable =Runnable() {
                fun run() {
                    if (model.getIsProgressBarUpdating().getValue()!!) {
                        if (model.getBinder().getValue() != null) { // meaning the service is bound
                            if (mService.getProgress() === mService.getMaxValue()) {
                                model.setIsProgressBarUpdating(false)
                            }
                            mProgressBar.progress = mService.getProgress()
                            mProgressBar.max = mService.getMaxValue()
                            val progress = String.valueOf(100 * mService.getProgress() / mService.getMaxValue()) + "%"
                            mTextView.text = progress
                        }
                        //handler.postDelayed(this, 100)
                    }else{

                    }
                }

            })*/
        }

    override fun onResume() {
        super.onResume()
        startService()
    }


    override fun onStop() {
        super.onStop()
        if (model.getBinder() != null) {
            unbindService(model.getServiceConnection())
        }
    }

    private fun startService() {
        val serviceIntent = Intent(this, BoundService::class.java)
        startService(serviceIntent)
        bindService()
    }

    private fun bindService() {
        val serviceBindIntent = Intent(this, BoundService::class.java)
        bindService(serviceBindIntent, model.getServiceConnection(), Context.BIND_AUTO_CREATE)
    }
}
