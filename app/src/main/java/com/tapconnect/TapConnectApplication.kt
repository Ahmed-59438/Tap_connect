package com.tapconnect

import android.app.Application
import android.util.Log
import com.tapconnect.data.AppContainer

class TapConnectApplication : Application() {
    
    // Global container for dependencies
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "TapConnect Application Initialized")
        
        // Initialize the container
        container = AppContainer(this)
    }

    companion object {
        private const val TAG = "TapConnectApp"
    }
}
