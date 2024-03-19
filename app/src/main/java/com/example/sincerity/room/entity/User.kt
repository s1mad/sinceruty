package com.example.sincerity.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    val username: String,

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0
)
