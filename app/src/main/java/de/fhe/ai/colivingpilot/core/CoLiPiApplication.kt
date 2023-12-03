package de.fhe.ai.colivingpilot.core

import android.app.Application
import android.util.Log
import de.fhe.ai.colivingpilot.http.RetrofitClient
import de.fhe.ai.colivingpilot.storage.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CoLiPiApplication : Application() {

    val keyValueStore: KeyValueStore by lazy {
        KeyValueStore(this)
    }

    private fun testDatabase() {
        CoroutineScope(Dispatchers.Main).launch {
            val repo = Repository(this@CoLiPiApplication)
            repo.updateAll()
        }
    }

    override fun onCreate() {
        super.onCreate()
        RetrofitClient.initialize(keyValueStore)
        Log.i(LOG_TAG, "Application initialized.")
        testDatabase()
    }

    companion object {
        const val LOG_TAG = "CoLiPi"
    }

}