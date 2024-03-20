package com.example.sincerity.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.sincerity.SincerityViewModel
import com.example.sincerity.room.entity.Question

@Composable
fun CardQuestionScreen(
    viewModel: SincerityViewModel,
    navController: NavController,
    cardId: Long
) {
    val questions = viewModel.getCardQuestions(cardId).collectAsState(emptyList()).value
    val showDialog = remember { mutableStateOf(false) }
    val currentEditQuestion = remember { mutableStateOf<Question?>(null) }
    val questionToDelete = remember { mutableStateOf<Question?>(null) }
    val showDeleteConfirmDialog = remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog.value = true }) {
                Icon(Icons.Filled.Add, contentDescription = "Add")
            }
        }
    ) { paddingValues ->
        LazyColumn(contentPadding = paddingValues) {
            itemsIndexed(questions) { index, question ->
                QuestionRow(
                    question = question,
                    index = (index + 1).toString() + ".",
                    onQuestionEdit = { editItem: Question ->
                        currentEditQuestion.value = editItem
                        showDialog.value = true
                    },
                    onQuestionDelete = { deleteItem: Question ->
                        questionToDelete.value = deleteItem
                        showDeleteConfirmDialog.value = true
                    }
                )
                Spacer(modifier = Modifier.height(8.dp)) // Spacer for padding between items
            }
        }
    }

    // Add Dialog and Delete Confirmation Dialog
    if (showDialog.value) {
        if (currentEditQuestion.value == null) {
            QuestionAddDialog(
                onDismiss = { showDialog.value = false },
                onAddQuestion = { text ->
                    viewModel.upsertQuestion(Question(text = text, cardId = cardId))
                    showDialog.value = false
                })
        } else {
            QuestionEditDialog(question = currentEditQuestion.value!!, onDismiss = {
                showDialog.value = false
                currentEditQuestion.value = null
            }, onQuestionItem = { editedQuestion: Question ->
                viewModel.upsertQuestion(editedQuestion)
                showDialog.value = false
                currentEditQuestion.value = null
            })
        }
    }

    if (showDeleteConfirmDialog.value) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmDialog.value = false },
            title = { Text("Confirm Delete") },
            text = { Text("Are you sure you want to delete this question?") },
            confirmButton = {
                Button(
                    onClick = {
                        questionToDelete.let { viewModel.deleteQuestion(it.value!!) }
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

@Composable
fun QuestionRow(
    question: Question,
    index: String,
    onQuestionEdit: (Question) -> Unit,
    onQuestionDelete: (Question) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(text = index, modifier = Modifier.align(Alignment.Top))
            Text(
                modifier = Modifier.fillMaxWidth().weight(1f),
                text = question.text,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
            )
            Row {
                IconButton(onClick = { onQuestionEdit(question) }) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Edit",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                IconButton(onClick = { onQuestionDelete(question) }) {
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

@Composable
fun QuestionAddDialog(onDismiss: () -> Unit, onAddQuestion: (String) -> Unit) {
    val text = remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Question") },
        text = {
            TextField(
                value = text.value,
                onValueChange = { text.value = it },
                label = { Text("Question") }
            )
        },
        confirmButton = {
            Button(onClick = { onAddQuestion(text.value) }) {
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

@Composable
fun QuestionEditDialog(
    question: Question,
    onDismiss: () -> Unit,
    onQuestionItem: (Question) -> Unit
) {
    val text = remember { mutableStateOf(question.text) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Question") },
        text = {
            TextField(
                value = text.value,
                onValueChange = { text.value = it },
                label = { Text("Question") }
            )
        },
        confirmButton = {
            Button(onClick = { onQuestionItem(question.copy(text = text.value)) }) {
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