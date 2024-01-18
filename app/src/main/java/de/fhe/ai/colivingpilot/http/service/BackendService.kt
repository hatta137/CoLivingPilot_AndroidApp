package de.fhe.ai.colivingpilot.http.service

import de.fhe.ai.colivingpilot.http.data.request.CreateWgRequest
import de.fhe.ai.colivingpilot.http.data.request.LoginRequest
import de.fhe.ai.colivingpilot.http.data.request.RegisterRequest
import de.fhe.ai.colivingpilot.http.data.response.BackendResponse
import de.fhe.ai.colivingpilot.http.data.response.datatypes.InvitationCodeData
import de.fhe.ai.colivingpilot.http.data.response.datatypes.JwtData
import de.fhe.ai.colivingpilot.http.data.response.datatypes.WgData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface BackendService {

    @POST("api/user/login")
    fun login(@Body loginRequest: LoginRequest): Call<BackendResponse<JwtData>>

    @POST("api/user")
    fun register(@Body registerRequest: RegisterRequest): Call<BackendResponse<JwtData>>

    @AuthRequired
    @GET("api/wg")
    fun getWgData(): Call<BackendResponse<WgData>>

    @AuthRequired
    @POST("api/wg")
    fun createWg(@Body createWgRequest: CreateWgRequest): Call<BackendResponse<InvitationCodeData>>

    @AuthRequired
    @GET("api/wg/join")
    fun joinWg(@Query("code") code: String): Call<BackendResponse<Unit>>

}