package com.example.talenta.api

data class UpdateUserRequest(
    val email: String,
    val password: String
)

data class UpdateTalentRequest(
    val email: String,
    val password: String
)