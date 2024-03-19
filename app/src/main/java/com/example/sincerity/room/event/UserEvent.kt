package com.example.sincerity.room.event

import com.example.sincerity.room.entity.User

sealed interface UserEvent {
    object SortUsers : UserEvent
    data class DeleteUser(val user: User) : UserEvent
    data class SaveUser(val username: String) : UserEvent
}