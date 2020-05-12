package com.converter.myapplication

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainActivityViewModel:ViewModel() {

    private val TAG = "MainActivityViewModel"

    private val mIsProgressBarUpdating: MutableLiveData<Boolean> = MutableLiveData()
    private val mBinder: MutableLiveData<BoundService.MyBinder> = MutableLiveData()

    private val serviceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, iBinder: IBinder) {
            Log.d(TAG, "ServiceConnection: connected to service.")
            // We've bound to MyService, cast the IBinder and get MyBinder instance
            val binder: BoundService.MyBinder = iBinder as BoundService.MyBinder
            mBinder.postValue(binder)
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            Log.d(TAG, "ServiceConnection: disconnected from service.")
            mBinder.postValue(null)
        }
    }

    fun getServiceConnection(): ServiceConnection {
        return serviceConnection
    }

    fun getBinder(): LiveData<BoundService.MyBinder> {
        return mBinder
    }


    fun getIsProgressBarUpdating(): LiveData<Boolean> {
        return mIsProgressBarUpdating
    }

    fun setIsProgressBarUpdating(isUpdating: Boolean) {
        mIsProgressBarUpdating.postValue(isUpdating)
    }

}