package com.example.sincerity

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sincerity.room.dao.CardDao
import com.example.sincerity.room.dao.QuestionDao
import com.example.sincerity.room.dao.UserDao
import com.example.sincerity.room.entity.User
import com.example.sincerity.room.event.UserEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SincerityViewModel(
    private val userDao: UserDao,
    private val cardDao: CardDao,
    private val questionDao: QuestionDao
) : ViewModel() {
    val isSortedByUsername = MutableStateFlow(true)

    //////////////////////////////////////////////////////////////
    // User //////////////////////////////////////////////////////
    private var users = isSortedByUsername.flatMapLatest { sort ->
        if (sort){
            userDao.getUsersByUsername()
        } else{
            userDao.getUsersById()
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    val _state = MutableStateFlow(UserState())
    val state = combine(_state, isSortedByUsername, users) { state, isSortedByUsername, users ->
        state.copy(
            users = users
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserState())
    fun onUserEvent(event: UserEvent) {
        when (event) {
            is UserEvent.DeleteUser -> {
                viewModelScope.launch { userDao.deleteUser(event.user) }
            }

            is UserEvent.SaveUser -> {
                val user = User(
                    username = state.value.username.value
                )
                viewModelScope.launch { userDao.upsertUser(user) }
                _state.update {
                    it.copy(
                        username = mutableStateOf("")
                    )
                }
            }
            UserEvent.SortUsers -> isSortedByUsername.value = !isSortedByUsername.value
        }
    }
}