package com.example.sincerity.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.sincerity.room.entity.Card
import com.example.sincerity.room.relation.CardWithQuestions
import kotlinx.coroutines.flow.Flow

@Dao
interface CardDao {
    @Upsert
    suspend fun upsertCard(card: Card)

    @Delete
    suspend fun deleteCard(card: Card)

    @Query("SELECT * FROM cards WHERE userId = :userId ORDER BY id")
    fun getAllUserCards(userId: Long): Flow<List<Card>>

    @Transaction
    @Query("SELECT * FROM cards WHERE id = :id")
    suspend fun getCardWithQuestions(id: Long): List<CardWithQuestions>
}