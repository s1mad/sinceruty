package com.example.sincerity.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.sincerity.room.entity.User
import com.example.sincerity.room.relation.UserWithCards
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Upsert
    suspend fun upsertUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Query("SELECT * FROM users ORDER BY username")
    fun getAllUsers(): Flow<List<User>>

    @Transaction
    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getUserWithCards(id: Long): List<UserWithCards>
}