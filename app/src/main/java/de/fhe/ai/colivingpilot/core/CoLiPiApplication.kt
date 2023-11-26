package de.fhe.ai.colivingpilot.core

import android.app.Application
import android.util.Log
import de.fhe.ai.colivingpilot.http.RetrofitClient

class CoLiPiApplication : Application() {

    val keyValueStore: KeyValueStore by lazy {
        KeyValueStore(this)
    }

    override fun onCreate() {
        super.onCreate()
        RetrofitClient.initialize(keyValueStore)
        Log.i(LOG_TAG, "Application initialized.")
    }

    companion object {
        const val LOG_TAG = "CoLiPi"
    }

}