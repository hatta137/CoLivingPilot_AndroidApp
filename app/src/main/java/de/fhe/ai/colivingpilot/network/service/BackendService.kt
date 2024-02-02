package de.fhe.ai.colivingpilot.network.service

import de.fhe.ai.colivingpilot.network.auth.AuthRequired
import de.fhe.ai.colivingpilot.network.data.request.AddShoppingListItemRequest
import de.fhe.ai.colivingpilot.network.data.request.AddTaskRequest
import de.fhe.ai.colivingpilot.network.data.request.CheckShoppingListItemRequest
import de.fhe.ai.colivingpilot.network.data.request.CreateWgRequest
import de.fhe.ai.colivingpilot.network.data.request.LoginRequest
import de.fhe.ai.colivingpilot.network.data.request.RegisterRequest
import de.fhe.ai.colivingpilot.network.data.response.BackendResponse
import de.fhe.ai.colivingpilot.network.data.response.BackendResponseNoData
import de.fhe.ai.colivingpilot.network.data.response.datatypes.InvitationCodeData
import de.fhe.ai.colivingpilot.network.data.response.datatypes.JwtData
import de.fhe.ai.colivingpilot.network.data.response.datatypes.WgData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface BackendService {

    @POST("api/user/login")
    fun login(@Body request: LoginRequest): Call<BackendResponse<JwtData>>

    @POST("api/user")
    fun register(@Body request: RegisterRequest): Call<BackendResponse<JwtData>>

    @AuthRequired
    @GET("api/wg")
    fun getWgData(): Call<BackendResponse<WgData>>

    @AuthRequired
    @POST("api/wg")
    fun createWg(@Body request: CreateWgRequest): Call<BackendResponse<InvitationCodeData>>

    @AuthRequired
    @GET("api/wg/join")
    fun joinWg(@Query("code") code: String): Call<BackendResponseNoData>

    @AuthRequired
    @GET("api/wg/leave")
    fun leaveWg(): Call<BackendResponseNoData>

    // TODO: either use id as path param or username as query param
    @AuthRequired
    @GET("api/wg/kick/{username}")
    fun kickFromWg(@Path("username") username: String): Call<BackendResponseNoData>

    @AuthRequired
    @POST("api/wg/shoppinglist")
    fun addShoppingListItem(@Body request: AddShoppingListItemRequest): Call<BackendResponseNoData>

    @AuthRequired
    @POST("api/wg/shoppinglist/check/{id}")
    fun checkShoppingListItem(@Path("id") itemId: String, @Body request: CheckShoppingListItemRequest): Call<BackendResponseNoData>

    @AuthRequired
    @DELETE("api/wg/shoppinglist/{id}")
    fun removeShoppingListItem(@Path("id") itemId: String): Call<BackendResponseNoData>

    @AuthRequired
    @POST("api/task")
    fun addTask(@Body request: AddTaskRequest): Call<BackendResponseNoData>

    @AuthRequired
    @DELETE("api/task/{id}")
    fun removeTask(@Path("id") taskId: String): Call<BackendResponseNoData>

    @AuthRequired
    @DELETE("api/task/done/{id}")
    fun doneTask(@Path("id") taskId: String): Call<BackendResponseNoData>

}