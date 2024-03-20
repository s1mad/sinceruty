package com.example.sincerity.room.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "questions",
    foreignKeys = [
        ForeignKey(
            entity = Card::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("cardId"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Question(
    val cardId: Long,
    val text: String,
    val answered: Boolean = false,

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0
)