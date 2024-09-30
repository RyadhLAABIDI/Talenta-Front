package com.example.talenta.models

data class AuthResponse(
    val token: String,
    val expiresIn: Long
)

data class AuthRequest(
    val email: String,
    val password: String
)