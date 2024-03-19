package com.example.sincerity.room.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.example.sincerity.room.entity.Card
import com.example.sincerity.room.entity.User

data class UserWithCards(
    @Embedded val user: User,
    @Relation(
        parentColumn = "id",
        entityColumn = "userId"
    )
    val cards: List<Card>
)