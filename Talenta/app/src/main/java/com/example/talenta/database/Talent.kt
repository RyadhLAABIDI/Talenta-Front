package com.example.talenta.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


@Entity
data class Talent(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val email: String,
    val password: String,
    val role: String
)
{
    companion object {
        const val TABLE_NAME = "user_table"
    }
}