package com.example.talenta.services

import com.example.talenta.api.BackendService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory




object BackendClient {
    private const val BASE_URL = "mongodb+srv://laabidiryadh:ryadh1@cluster0.zsh66ak.mongodb.net/?retryWrites=true&w=majority"

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val backendService: BackendService = retrofit.create(BackendService::class.java)
}
