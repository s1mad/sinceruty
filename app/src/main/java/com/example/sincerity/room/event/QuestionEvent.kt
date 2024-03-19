package com.example.sincerity.room.event

import com.example.sincerity.room.entity.Question

sealed interface QuestionEvent {
    object SortQuestion : QuestionEvent
    data class DeleteQuestion(val question: Question) : QuestionEvent
    data class SaveQuestion(
        val cardId: Long,
        val text: String
    ) : QuestionEvent
}
