package de.fhe.ai.colivingpilot.core

import android.app.Application
import android.util.Log

class CoLiPiApplication : Application() {

    private lateinit var keyValueStore: KeyValueStore

    override fun onCreate() {
        super.onCreate()
        instance = this
        keyValueStore = KeyValueStore(this)
        Log.i(LOG_TAG, "Application initialized.")
    }

    fun getKeyValueStore(): KeyValueStore {
        return keyValueStore
    }

    companion object {

        const val LOG_TAG = "CoLiPi"

        lateinit var instance: CoLiPiApplication
            private set

    }

}