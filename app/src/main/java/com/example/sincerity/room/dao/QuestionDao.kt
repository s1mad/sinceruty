package com.example.sincerity.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.sincerity.room.entity.Question
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestionDao {
    @Upsert
    suspend fun upsertQuestion(question: Question)

    @Delete
    suspend fun deleteQuestion(question: Question)

    @Query("SELECT * FROM questions WHERE cardId = :cardId ORDER BY id")
    fun getAllCardQuestions(cardId: Long): Flow<List<Question>>
}