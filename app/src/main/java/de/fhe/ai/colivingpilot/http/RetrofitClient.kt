package de.fhe.ai.colivingpilot.http

import de.fhe.ai.colivingpilot.http.service.BackendService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // TODO: Change to tool.colivingpilot.de
    private const val BASE_URL = "http://192.168.178.22:20013/"

    val instance: BackendService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(BackendService::class.java)
    }
}