package de.fhe.ai.colivingpilot.core

import android.app.Application
import android.util.Log
import de.fhe.ai.colivingpilot.http.RetrofitClient
import de.fhe.ai.colivingpilot.storage.Repository

class CoLiPiApplication : Application() {

    lateinit var keyValueStore: KeyValueStore
    lateinit var repository: Repository

    override fun onCreate() {
        super.onCreate()
        instance = this
        keyValueStore = KeyValueStore(this)
        repository = Repository()
        RetrofitClient.initialize(keyValueStore)
        Log.i(LOG_TAG, "Application initialized.")
    }

    companion object {

        const val LOG_TAG = "CoLiPi"

        lateinit var instance: CoLiPiApplication
            private set

    }

}