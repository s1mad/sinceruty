package com.example.sincerity

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.sincerity.room.entity.User

data class UserState (
    val users: List<User> = emptyList(),
    val username: MutableState<String> = mutableStateOf("")
)