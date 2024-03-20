package com.example.sincerity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sincerity.room.entity.Card
import com.example.sincerity.room.entity.Question
import com.example.sincerity.room.entity.User
import com.example.sincerity.room.repository.SincerityRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SincerityViewModel(
    private val repository: SincerityRepository
) : ViewModel() {

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users.asStateFlow()

    init {
        viewModelScope.launch { repository.users.collect {
            _users.value = it
        }}
    }

    fun upsertUser(user: User) = viewModelScope.launch {
        repository.upsertUser(user)
    }
    fun deleteUser(user: User) = viewModelScope.launch {
        repository.deleteUser(user)
    }

    fun upsertCard(card: Card) = viewModelScope.launch {
        repository.upsertCard(card)
    }
    fun deleteCard(card: Card) = viewModelScope.launch {
        repository.deleteCard(card)
    }
    fun upsertQuestion(question: Question) = viewModelScope.launch {
        repository.upsertQuestion(question)
    }
    fun deleteQuestion(question: Question) = viewModelScope.launch {
        repository.deleteQuestion(question)
    }

    fun getUserCards(userId: Long) = repository.getUserCards(userId)
    fun getCardQuestions(cardId: Long) = repository.getCardQuestions(cardId)
}