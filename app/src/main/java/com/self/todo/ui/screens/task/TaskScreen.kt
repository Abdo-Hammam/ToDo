package com.self.todo.ui.screens.task

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.self.todo.data.models.Priority
import com.self.todo.data.models.ToDoTask
import com.self.todo.ui.viewmodels.SharedViewModel
import com.self.todo.util.Action

@Composable
fun TaskScreen(
    selectedTask: ToDoTask?,
    sharedViewModel: SharedViewModel,
    navigateToListScreen: (Action) -> Unit
) {
    val title:String by sharedViewModel.title
    val description: String by sharedViewModel.description
    val priority: Priority by sharedViewModel.priority

    Scaffold(
        topBar = {
            TaskAppBar(
                selectedTask = selectedTask,
                navigateToListScreen = navigateToListScreen
            )
        }
    ) {
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(it)) {
            TaskContent(
                title = title,
                onTitleChanged = { title ->
                    sharedViewModel.title.value = title
                },
                description = description,
                onDescriptionChanged = {description ->
                    sharedViewModel.description.value = description
                },
                priority = priority,
                onPrioritySelected = {priority ->
                    sharedViewModel.priority.value = priority
                }
            )
        }
    }
}