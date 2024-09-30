package com.example.talenta.api
import com.google.gson.annotations.SerializedName





data class LoginRequest(val email: String, val password: String)

// LoginResponse.kt
data class LoginResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("userId") val userId: String,
    @SerializedName("userEmail") val userEmail: String,
    @SerializedName("userRole") val userRole: String
)

data class Profile(val email: String)

