package com.example.talenta.api


import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST

import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path




interface BackendService {
    @POST("auth/register")
    suspend fun registerUser(@Body user: com.example.talenta.database.User): Response<ModelResponse>
}
interface RetrofitService {

    @POST("auth/login")
    fun loginUser(@Body loginRequest: LoginRequest): Call<LoginResponse>


}

interface ApiService {
    @GET("user/getUser/{id}")
    fun getUser(@Path("id") userId: String): Call<UserResponse>

    @GET("talent/getTal/{id}")
    fun getTalent(@Path("id") userId: String): Call<UserResponse>



    @PUT("user/users/{id}")
    fun updateUser(@Path("id") userId: String, @Body updateData: UpdateUserRequest): Call<Void>

    @PUT("talent/updateTalent/{id}")
    fun updateTalent(@Path("id") userId: String, @Body updateData: UpdateUserRequest): Call<Void>



    @GET("auth/logout")
    fun logout(): Call<Void>


    @DELETE("user/users/{userId}")
    fun deleteUser(@Path("userId") userId: String): Call<Void>

    @DELETE("talent/deleteTalent/{Id}")
    fun deleteTalent(@Path("Id") userId: String): Call<Void>

}
