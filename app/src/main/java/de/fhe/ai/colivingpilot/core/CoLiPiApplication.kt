package de.fhe.ai.colivingpilot.core

import android.app.Application
import android.util.Log
import de.fhe.ai.colivingpilot.http.RetrofitClient
import de.fhe.ai.colivingpilot.storage.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CoLiPiApplication : Application() {

    private lateinit var keyValueStore: KeyValueStore

    override fun onCreate() {
        super.onCreate()
        instance = this
        keyValueStore = KeyValueStore(this)
        Log.i(LOG_TAG, "Application initialized.")
        testDatabase()
    }

    private fun testDatabase() {
        CoroutineScope(Dispatchers.Main).launch {
            val repo = Repository(this@CoLiPiApplication)
            repo.updateAll()
        }
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