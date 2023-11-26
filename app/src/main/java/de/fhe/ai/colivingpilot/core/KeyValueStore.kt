package de.fhe.ai.colivingpilot.core

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

class KeyValueStore(private val app: Application) {

    private val preferences: SharedPreferences
        get() = app.getSharedPreferences(KEY_VALUE_STORE_FILE_NAME, Context.MODE_PRIVATE)

    fun writeString(key: String, value: String) {
        preferences.edit().putString(key, value).apply()
    }

    fun readString(key: String): String {
        return preferences.getString(key, DEFAULT_STRING_VALUE) ?: DEFAULT_STRING_VALUE
    }

    fun writeInt(key: String, value: Int) {
        preferences.edit().putInt(key, value).apply()
    }

    fun readInt(key: String): Int {
        return preferences.getInt(key, DEFAULT_INT_VALUE)
    }

    fun writeBoolean(key: String, value: Boolean) {
        preferences.edit().putBoolean(key, value).apply()
    }

    fun readBoolean(key: String): Boolean {
        return preferences.getBoolean(key, DEFAULT_BOOLEAN_VALUE)
    }

    companion object {
        private const val KEY_VALUE_STORE_FILE_NAME = "colivingpilot_kv_store"
        private const val DEFAULT_STRING_VALUE = ""
        private const val DEFAULT_INT_VALUE = 0
        private const val DEFAULT_BOOLEAN_VALUE = false
    }

}