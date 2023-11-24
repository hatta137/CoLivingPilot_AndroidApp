package de.fhe.ai.colivingpilot.http.service

import de.fhe.ai.colivingpilot.http.data.request.LoginRequest
import de.fhe.ai.colivingpilot.http.data.request.RegisterRequest
import de.fhe.ai.colivingpilot.http.data.response.BackendResponse
import de.fhe.ai.colivingpilot.http.data.response.datatypes.JwtData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface BackendService {

    @POST("api/user/login")
    fun login(@Body loginRequest: LoginRequest): Call<BackendResponse<JwtData>>

    @POST("api/user/")
    fun register(@Body registerRequest: RegisterRequest): Call<BackendResponse<JwtData>>

}