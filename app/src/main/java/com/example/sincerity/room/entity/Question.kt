package com.example.sincerity.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "questions")
data class Question(
    val cardId: Long,
    val text: String,
    val answered: Boolean = false,

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0
)