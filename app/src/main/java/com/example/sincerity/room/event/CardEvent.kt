package com.example.sincerity.room.event

import com.example.sincerity.room.entity.Card

sealed interface CardEvent {
    object SortCards : CardEvent
    data class DeleteCard(val card: Card) : CardEvent
    data class SaveCard(
        val userId: Long,
        val type: String
    ) : CardEvent
}