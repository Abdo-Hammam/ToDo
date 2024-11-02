package com.self.todo.ui.screens.list

import android.widget.Toast
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.self.todo.R
import com.self.todo.components.DisplayAlertDialog
import com.self.todo.components.PriorityItem
import com.self.todo.data.models.Priority
import com.self.todo.data.models.ToDoTask
import com.self.todo.ui.theme.systemBarDarkColor
import com.self.todo.ui.theme.systemBarLightColor
import com.self.todo.ui.viewmodels.SharedViewModel
import com.self.todo.util.Action
import com.self.todo.util.RequestState
import com.self.todo.util.SearchAppBarState

@Composable
fun ListAppBar(
    sharedViewModel: SharedViewModel,
    searchAppBarState: SearchAppBarState,
    searchTextState: String,
    allTasks: RequestState<List<ToDoTask?>>
) {
    LaunchedEffect(key1 = searchTextState) {
        sharedViewModel.getAllTasks()
    }
    val onValueChanged = { it: String ->
        sharedViewModel.searchTextState.value = it
    }
    val focusRequester = remember { FocusRequester() }

    when (searchAppBarState) {
        SearchAppBarState.CLOSED -> {
            DefaultAppBar(
                onSearchClicked = {
                    sharedViewModel.searchAppBarState.value = SearchAppBarState.OPENED
                },
                onSortClicked = {
                    sharedViewModel.persistSortState(priority = it)
                },

                onDeleteClicked = {
                    sharedViewModel.action.value = Action.DELETE_ALL
                },
                allTasks = allTasks
            )
        }

        else -> {
            SearchAppBar(
                value = searchTextState,
                onValueChanged = { onValueChanged(it) },
                onSearchClicked = { },
                onCloseClicked = {
                    if (searchTextState.isNotEmpty()) sharedViewModel.searchTextState.value = ""
                    else sharedViewModel.searchAppBarState.value = SearchAppBarState.CLOSED
                },
                focusRequester = focusRequester
            )
        }
    }


}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultAppBar(
    onSearchClicked: () -> Unit,
    onSortClicked: (Priority) -> Unit,
    onDeleteClicked: () -> Unit,
    allTasks: RequestState<List<ToDoTask?>>
) {
    TopAppBar(
        title = {
            Text(text = "Tasks")
        },
        colors = TopAppBarDefaults.topAppBarColors().copy(
            containerColor = if (isSystemInDarkTheme()) systemBarDarkColor else systemBarLightColor,
            titleContentColor = Color.White,
            actionIconContentColor = Color.White
        ),
        actions = {

            SearchAction(onSearchClicked = onSearchClicked)

            SortAction(onSortClicked = onSortClicked)

            DeleteAction(
                onDeleteClicked = onDeleteClicked,
                allTasks = allTasks
            )
        }
    )
}

@Composable
fun SearchAction(
    onSearchClicked: () -> Unit
) {
    IconButton(onClick = { onSearchClicked() }) {
        Icon(imageVector = Icons.Filled.Search, contentDescription = "Search tasks")
    }
}

@Composable
fun SortAction(
    onSortClicked: (Priority) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    IconButton(onClick = { expanded = true }) {
        Icon(
            painter = painterResource(id = R.drawable.baseline_filter_list_24),
            contentDescription = "Sort Tasks"
        )
        DropdownMenu(expanded = expanded,
            onDismissRequest = { expanded = false }) {
            DropdownMenuItem(text = {
                PriorityItem(priority = Priority.LOW)
            },
                onClick = {
                    expanded = false
                    onSortClicked(Priority.LOW)
                })

            DropdownMenuItem(text = {
                PriorityItem(priority = Priority.HIGH)
            },
                onClick = {
                    expanded = false
                    onSortClicked(Priority.HIGH)
                })

            DropdownMenuItem(text = {
                PriorityItem(priority = Priority.NONE)
            },
                onClick = {
                    expanded = false
                    onSortClicked(Priority.NONE)
                })
        }
    }
}


@Composable
fun DeleteAction(
    onDeleteClicked: () -> Unit,
    allTasks: RequestState<List<ToDoTask?>>
) {
    var expanded by remember { mutableStateOf(false) }
    var openDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current


    IconButton(onClick = { expanded = true }) {
        Icon(
            imageVector = Icons.Filled.MoreVert,
            contentDescription = "Sort Tasks"
        )
        DropdownMenu(expanded = expanded,
            onDismissRequest = { expanded = false }) {
            DropdownMenuItem(text = { Text(text = "Delete All") },
                onClick = {
                    expanded = false
                    if (allTasks is RequestState.Success) {
                        if (allTasks.data.isEmpty()) Toast.makeText(
                            context,
                            "There's No Tasks To Remove!",
                            Toast.LENGTH_SHORT
                        ).show()
                        else
                            openDialog = true
                    }
                })
        }
    }

    DisplayAlertDialog(
        title = "Remove All Tasks?",
        message = "Are you sure you want to remove all Tasks? There is no going back after this action",
        openDialog = openDialog,
        closeDialog = { openDialog = false },
        onYesClicked = {
            onDeleteClicked()
            openDialog = false
        })
}


@Composable
fun SearchAppBar(
    value: String,
    onValueChanged: (String) -> Unit,
    onSearchClicked: (String) -> Unit,
    onCloseClicked: () -> Unit,
    focusRequester: FocusRequester = FocusRequester()
) {

    LaunchedEffect(key1 = Unit) {
        focusRequester.requestFocus()
    }

    TextField(
        value = value,
        onValueChange = { onValueChanged(it) },
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .focusRequester(focusRequester),
        singleLine = true,
        shape = RectangleShape,
        colors = TextFieldDefaults.colors().copy(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedContainerColor = if (isSystemInDarkTheme()) systemBarDarkColor else systemBarLightColor,
            unfocusedContainerColor = if (isSystemInDarkTheme()) systemBarDarkColor else systemBarLightColor,
            focusedTrailingIconColor = Color.White,
            unfocusedTrailingIconColor = Color.White,
            focusedLeadingIconColor = Color.White,
            unfocusedLeadingIconColor = Color.White,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedLabelColor = Color.White,
            unfocusedLabelColor = Color.White,
            focusedPlaceholderColor = Color.White,
            unfocusedPlaceholderColor = Color.White,
            cursorColor = Color.White
        ),
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "Search Icon",
                modifier = Modifier
                    .padding(start = 8.dp)
                    .alpha(.6f)
            )
        },
        trailingIcon = {
            IconButton(
                onClick = { onCloseClicked() },
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Close button"
                )
            }
        },
        placeholder = {
            Text(
                text = "Search...",
                modifier = Modifier.alpha(.6f)
            )
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = { onSearchClicked(value) }
        )
    )
}


@Preview
@Composable
private fun SearchAppBarPreview() {
    SearchAppBar(value = "", {}, {}, {})
}
















