package com.example.sincerity.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.sincerity.SincerityViewModel
import com.example.sincerity.room.entity.User

@Composable
fun UsersScreen(viewModel: SincerityViewModel, navController: NavController) {
    val users = viewModel.users.collectAsState().value
    val showDialog = remember { mutableStateOf(false) }
    val currentEditUser = remember { mutableStateOf<User?>(null) }
    val userToDelete = remember { mutableStateOf<User?>(null) }
    val showDeleteConfirmDialog = remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog.value = true }) {
                Icon(Icons.Filled.Add, contentDescription = "Add")
            }
        }
    ) { paddingValues ->
        LazyColumn(contentPadding = paddingValues) {
            items(users) { user ->
                UserRow(
                    user = user,
                    onUserEdit = { editItem: User ->
                        currentEditUser.value = editItem
                        showDialog.value = true
                    },
                    onUserDelete = { deleteItem: User ->
                        userToDelete.value = deleteItem
                        showDeleteConfirmDialog.value = true
                    },
                    onItemClick = {
                        navController.navigate("UserCardsScreen/${it.id}")
                    }
                )
                Spacer(modifier = Modifier.height(8.dp)) // Spacer for padding between items
            }
        }
    }

    // Add Dialog and Delete Confirmation Dialog
    if (showDialog.value) {
        if (currentEditUser.value == null) {
            UserAddDialog(
                onDismiss = { showDialog.value = false },
                onAddUser = { username ->
                    viewModel.upsertUser(User(username = username))
                    showDialog.value = false
                })
        } else {
            UserEditDialog(user = currentEditUser.value!!, onDismiss = {
                showDialog.value = false
                currentEditUser.value = null
            }, onUserItem = { editedUser: User ->
                viewModel.upsertUser(editedUser)
                showDialog.value = false
                currentEditUser.value = null
            })
        }
    }

    if (showDeleteConfirmDialog.value) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmDialog.value = false },
            title = { Text("Confirm Delete") },
            text = { Text("Are you sure you want to delete this user?") },
            confirmButton = {
                Button(
                    onClick = {
                        userToDelete.let { viewModel.deleteUser(it.value!!) }
                        showDeleteConfirmDialog.value = false
                    }
                ) { Text("Yes") }
            },
            dismissButton = {
                Button(onClick = { showDeleteConfirmDialog.value = false }) { Text("No") }
            }
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserRow(user: User, onUserEdit: (User) -> Unit, onUserDelete: (User) -> Unit, onItemClick: (User) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .clip(RoundedCornerShape(10.dp))
            .clickable { onItemClick(user) },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = user.username,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
            )
            Row {
                IconButton(onClick = { onUserEdit(user) }) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Edit",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                IconButton(onClick = { onUserDelete(user) }) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserAddDialog(onDismiss: () -> Unit, onAddUser: (String) -> Unit) {
    val text = remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add User") },
        text = {
            TextField(
                value = text.value,
                onValueChange = { text.value = it },
                label = { Text("Username") }
            )
        },
        confirmButton = {
            Button(onClick = { onAddUser(text.value) }) {
                Text("Add")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserEditDialog(user: User, onDismiss: () -> Unit, onUserItem: (User) -> Unit) {
    val text = remember { mutableStateOf(user.username) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit User") },
        text = {
            TextField(
                value = text.value,
                onValueChange = { text.value = it },
                label = { Text("Username") }
            )
        },
        confirmButton = {
            Button(onClick = { onUserItem(user.copy(username = text.value)) }) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}