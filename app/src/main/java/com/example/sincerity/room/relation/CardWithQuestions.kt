package com.example.sincerity.room.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.example.sincerity.room.entity.Card
import com.example.sincerity.room.entity.Question

data class CardWithQuestions(
    @Embedded val card: Card,
    @Relation(
        parentColumn = "id",
        entityColumn = "cardId"
    )
    val questions: List<Question>
)