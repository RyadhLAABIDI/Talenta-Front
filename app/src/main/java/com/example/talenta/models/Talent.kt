package com.example.talenta.models

import com.google.gson.annotations.SerializedName

data class Talent(
    @SerializedName("_id") // Assurez-vous que le nom du champ correspond à celui renvoyé par votre API
    val id: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("role")
    val role: String
)