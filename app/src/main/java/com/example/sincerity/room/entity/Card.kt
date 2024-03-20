package com.example.sincerity.room.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "cards",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("userId"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Card(
    val userId: Long,
    val type: String,

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0
)