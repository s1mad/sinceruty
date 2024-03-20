package com.example.sincerity.room.repository

import com.example.sincerity.room.dao.CardDao
import com.example.sincerity.room.dao.QuestionDao
import com.example.sincerity.room.dao.UserDao
import com.example.sincerity.room.entity.Card
import com.example.sincerity.room.entity.Question
import com.example.sincerity.room.entity.User
import kotlinx.coroutines.flow.Flow

class SincerityRepository(
    private val userDao: UserDao,
    private val cardDao: CardDao,
    private val questionDao: QuestionDao
) {
    // Users
    val users: Flow<List<User>> = userDao.getAllUsers()
    suspend fun upsertUser(user: User) = userDao.upsertUser(user)
    suspend fun deleteUser(user: User) = userDao.deleteUser(user)

    // Card
    fun getUserCards(userId: Long) = cardDao.getAllUserCards(userId)
    suspend fun upsertCard(card: Card) = cardDao.upsertCard(card)
    suspend fun deleteCard(card: Card) = cardDao.deleteCard(card)

    // Question
    fun getCardQuestions(cardId: Long) = questionDao.getAllCardQuestions(cardId)
    suspend fun upsertQuestion(question: Question) = questionDao.upsertQuestion(question)
    suspend fun deleteQuestion(question: Question) = questionDao.deleteQuestion(question)
}