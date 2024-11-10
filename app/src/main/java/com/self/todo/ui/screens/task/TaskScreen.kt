package com.self.todo.ui.screens.task

import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.self.todo.data.models.Priority
import com.self.todo.data.models.ToDoTask
import com.self.todo.ui.viewmodels.SharedViewModel
import com.self.todo.util.Action
import com.self.todo.util.SearchAppBarState

@Composable
fun TaskScreen(
    selectedTask: ToDoTask?,
    sharedViewModel: SharedViewModel,
    navigateToListScreen: (Action) -> Unit
) {
    val title: String by sharedViewModel.title
    val description: String by sharedViewModel.description
    val priority: Priority by sharedViewModel.priority

    val context = LocalContext.current

    BackHandler {
        navigateToListScreen(Action.NO_ACTION)
    }

    Scaffold(
        topBar = {
            TaskAppBar(
                selectedTask = selectedTask,
                navigateToListScreen = { action ->
                    when (action) {
                        Action.NO_ACTION -> navigateToListScreen(action)
                        else -> {
                            if (sharedViewModel.validateFields()) {
                                navigateToListScreen(action)
                                sharedViewModel.searchAppBarState.value = SearchAppBarState.CLOSED
                                sharedViewModel.searchTextState.value = ""
                            } else
                                displayToast(context)
                        }
                    }
                }
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            TaskContent(
                title = title,
                onTitleChanged = { title ->
                    sharedViewModel.title.value = title
                },
                description = description,
                onDescriptionChanged = { description ->
                    sharedViewModel.description.value = description
                },
                priority = priority,
                onPrioritySelected = { priority ->
                    sharedViewModel.priority.value = priority
                }
            )
        }
    }
}

fun displayToast(context: Context) {
    Toast.makeText(
        context,
        "Fields Empty",
        Toast.LENGTH_SHORT
    ).show()
}
