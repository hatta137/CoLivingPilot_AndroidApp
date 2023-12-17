package de.fhe.ai.colivingpilot.core

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp
import de.fhe.ai.colivingpilot.di.AppModule
import de.fhe.ai.colivingpilot.storage.WgDatabase

@HiltAndroidApp
class CoLiPiApplication : Application() {

    private lateinit var keyValueStore: KeyValueStore

    override fun onCreate() {
        super.onCreate()
        keyValueStore = KeyValueStore(this)
        instance = this
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