package com.example.talenta.api

data class UserResponse(
    val status: String,
    val user: User
)

data class User(
    val email: String,
    val password: String
)
data class TalentResponse(
    val status: String,
    val user: Talent
)
data class Talent(
    val email: String,
    val password: String
)
