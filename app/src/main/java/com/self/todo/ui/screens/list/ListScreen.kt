package com.self.todo.ui.screens.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.self.todo.R
import com.self.todo.ui.theme.systemBarLightColor
import com.self.todo.ui.viewmodels.SharedViewModel
import com.self.todo.util.Action
import com.self.todo.util.SearchAppBarState
import kotlinx.coroutines.launch

@Composable
fun ListScreen(
    action: Action,
    navigateToTaskScreen: (Int) -> Unit,
    sharedViewModel: SharedViewModel
) {


    LaunchedEffect(key1 = action) {
        sharedViewModel.handleDatabaseActions(action = action )
    }

    val allTasks by sharedViewModel.allTasks.collectAsState()
    val searchedTasks by sharedViewModel.searchedTasks.collectAsState()

    val sortState by sharedViewModel.sortState.collectAsState()
    val lowPriorityTasks by sharedViewModel.lowPriorityTasks.collectAsState()
    val highPriorityTasks by sharedViewModel.highPriorityTasks.collectAsState()


    val searchAppBarState: SearchAppBarState by sharedViewModel.searchAppBarState
    val searchTextState: String by sharedViewModel.searchTextState


    val snackBarHostState = remember { SnackbarHostState() }

    DisplaySnackBar(
        snackBarHostState = snackBarHostState,
        onComplete = { sharedViewModel.updateAction(it) },
        taskTitle = sharedViewModel.title.value,
        onUndoClicked = {
            sharedViewModel.updateAction(it)
        },
        action = action
    )

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },
        modifier = Modifier
            .statusBarsPadding()
            .padding(WindowInsets.ime.asPaddingValues()),
        topBar = {
            ListAppBar(
                sharedViewModel,
                searchAppBarState,
                searchTextState,
                allTasks
            )
        },
        floatingActionButton = { ListFab(onFabClicked = navigateToTaskScreen) }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            ListContent(
                tasks = if (sharedViewModel.searchTextState.value.isNotEmpty()) searchedTasks else allTasks,
                lowPriorityTasks = lowPriorityTasks,
                highPriorityTasks = highPriorityTasks,
                sortState = sortState,
                onSwipeToDelete = { action, task ->
                    sharedViewModel.updateAction(action)
//                    sharedViewModel.action.value = action
                    sharedViewModel.updateTaskFields(task)
                    snackBarHostState.currentSnackbarData?.dismiss()
                },
                navigateToTaskScreen = navigateToTaskScreen
            )
        }
    }
}

@Composable
fun ListFab(
    onFabClicked: (taskId: Int) -> Unit
) {
    FloatingActionButton(
        onClick = {
            onFabClicked(-1)
        },
        containerColor = systemBarLightColor,
        contentColor = Color.White
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = stringResource(R.string.add_btn)
        )
    }
}

@Preview
@Composable
private fun ListScreenPreview() {
    ListScreen(Action.NO_ACTION,navigateToTaskScreen = {}, viewModel())
}


@Composable
fun DisplaySnackBar(
    snackBarHostState: SnackbarHostState,
    onComplete: (Action) -> Unit,
    taskTitle: String,
    onUndoClicked: (Action) -> Unit,
    action: Action
) {

    val scope = rememberCoroutineScope()
    LaunchedEffect(key1 = action) {
        if (action != Action.NO_ACTION && action != Action.UNDO) {
            scope.launch {
                val snackBarResult = snackBarHostState.showSnackbar(
                    message = when (action) {
                        Action.ADD -> "New Task Added: $taskTitle"
                        Action.DELETE -> "Task Deleted"
                        Action.UPDATE -> "Task Updated"
                        Action.DELETE_ALL -> "All Tasks Removed "
                        else -> ""
                    },
                    actionLabel = if (action == Action.DELETE) "UNDO" else "Ok",
                    duration = SnackbarDuration.Short
                )
                if (snackBarResult == SnackbarResult.ActionPerformed && action == Action.DELETE) {
                    onUndoClicked(Action.UNDO)
                }
            }
            onComplete(Action.NO_ACTION)
        }
    }
}

