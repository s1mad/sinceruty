package com.example.sincerity.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cards")
data class Card(
    val userId: Long,
    val type: String,

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0
)