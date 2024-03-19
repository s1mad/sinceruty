package com.example.sincerity.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.sincerity.room.dao.CardDao
import com.example.sincerity.room.dao.QuestionDao
import com.example.sincerity.room.dao.UserDao
import com.example.sincerity.room.entity.Card
import com.example.sincerity.room.entity.Question
import com.example.sincerity.room.entity.User

@Database(
    entities = [User::class, Card::class, Question::class],
    version = 1
)
abstract class SincerityDatabase: RoomDatabase() {
    abstract val userDao: UserDao
    abstract val cardDao: CardDao
    abstract val questionDao: QuestionDao
}