package de.fhe.ai.colivingpilot.http

import de.fhe.ai.colivingpilot.core.KeyValueStore
import de.fhe.ai.colivingpilot.http.service.AuthInterceptor
import de.fhe.ai.colivingpilot.http.service.BackendService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    // TODO: Change to tool.colivingpilot.de
    private const val BASE_URL = "http://192.168.178.22:20013/"
    private lateinit var okHttpClient: OkHttpClient

    fun initialize(keyValueStore: KeyValueStore) {
        okHttpClient = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(keyValueStore))
            .build()
    }

    val instance: BackendService by lazy {
        if (!::okHttpClient.isInitialized) {
            throw IllegalStateException("OkHttpClient has not been initialized")
        }

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(BackendService::class.java)
    }

}