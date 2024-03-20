package com.example.sincerity.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.sincerity.SincerityViewModel
import com.example.sincerity.room.entity.Card
import com.example.sincerity.room.entity.Question


@Composable
fun UserCardsScreen(
    viewModel: SincerityViewModel,
    navController: NavController,
    userId: Long
) {
    val cards = viewModel.getUserCards(userId).collectAsState(emptyList()).value
    val showDialog = remember { mutableStateOf(false) }
    val currentEditCard = remember { mutableStateOf<Card?>(null) }
    val cardToDelete = remember { mutableStateOf<Card?>(null) }
    val showDeleteConfirmDialog = remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog.value = true }) {
                Icon(Icons.Filled.Add, contentDescription = "Add")
            }
        }
    ) { paddingValues ->
        LazyColumn(contentPadding = paddingValues) {
            items(cards) { card ->
                CardRow(
                    card = card,
                    onCardEdit = { editCard: Card ->
                        currentEditCard.value = editCard
                        showDialog.value = true
                    },
                    onCardDelete = { deleteCard: Card ->
                        cardToDelete.value = deleteCard
                        showDeleteConfirmDialog.value = true
                    },
                    onItemClick = {
                        navController.navigate("CardQuestionScreen/${it.id}")
                    },
                    viewModel = viewModel
                )
                Spacer(modifier = Modifier.height(8.dp)) // Spacer for padding between items
            }
        }
    }

    // Add Dialog and Delete Confirmation Dialog
    if (showDialog.value) {
        if (currentEditCard.value == null) {
            CardAddDialog(
                onDismiss = { showDialog.value = false },
                onAddCard = { type ->
                    viewModel.upsertCard(Card(userId = userId, type = type))
                    showDialog.value = false
                })
        } else {
            CardEditDialog(card = currentEditCard.value!!, onDismiss = {
                showDialog.value = false
                currentEditCard.value = null
            }, onCardItem = { editedCard: Card ->
                viewModel.upsertCard(editedCard)
                showDialog.value = false
                currentEditCard.value = null
            })
        }
    }

    if (showDeleteConfirmDialog.value) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmDialog.value = false },
            title = { Text("Confirm Delete") },
            text = { Text("Are you sure you want to delete this card?") },
            confirmButton = {
                Button(
                    onClick = {
                        cardToDelete.let { viewModel.deleteCard(it.value!!) }
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
fun CardRow(
    card: Card,
    onCardEdit: (Card) -> Unit,
    onCardDelete: (Card) -> Unit,
    onItemClick: (Card) -> Unit,
    viewModel: SincerityViewModel
) {
    val questions = viewModel.getCardQuestions(card.id).collectAsState(emptyList()).value

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .clip(RoundedCornerShape(10.dp))
            .clickable { onItemClick(card) },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = card.type,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
                )
                Row {
                    IconButton(onClick = { onCardEdit(card) }) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "Edit",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = { onCardDelete(card) }) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Delete",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
            questions.forEachIndexed { index, element ->
                QuestionRow(question = element, index = (index + 1).toString() + ".")
            }
        }

    }
}

@Composable
fun QuestionRow(question: Question, index: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = index
        )
        Text(
            text = question.text,
            style = if (question.answered) TextStyle(textDecoration = TextDecoration.LineThrough) else TextStyle()
        )
    }
}

@Composable
fun CardAddDialog(onDismiss: () -> Unit, onAddCard: (String) -> Unit) {
    val text = remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Card") },
        text = {
            TextField(
                value = text.value,
                onValueChange = { text.value = it },
                label = { Text("Type") }
            )
        },
        confirmButton = {
            Button(onClick = { onAddCard(text.value) }) {
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
fun CardEditDialog(card: Card, onDismiss: () -> Unit, onCardItem: (Card) -> Unit) {
    val text = remember { mutableStateOf(card.type) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Card") },
        text = {
            TextField(
                value = text.value,
                onValueChange = { text.value = it },
                label = { Text("Type") }
            )
        },
        confirmButton = {
            Button(onClick = { onCardItem(card.copy(type = text.value)) }) {
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