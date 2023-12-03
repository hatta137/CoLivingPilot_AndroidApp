package de.fhe.ai.colivingpilot.core

import android.app.Application
import android.util.Log

class CoLiPiApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Log.i(LOG_TAG, "Application initialized.")
    }

    companion object {
        const val LOG_TAG = "CoLiPi"
    }

}