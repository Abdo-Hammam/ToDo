package com.self.todo.ui.screens.list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.self.todo.data.models.Priority
import com.self.todo.data.models.ToDoTask
import com.self.todo.ui.theme.highPriorityColor
import com.self.todo.util.Action
import com.self.todo.util.RequestState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ListContent(
    tasks: RequestState<List<ToDoTask>>,
    lowPriorityTasks: List<ToDoTask>,
    highPriorityTasks: List<ToDoTask>,
    sortState: RequestState<Priority>,
    onSwipeToDelete: (Action, ToDoTask) -> Unit,
    navigateToTaskScreen: (taskId: Int) -> Unit
) {
    if (tasks is RequestState.Success)
        if (tasks.data.isEmpty()) EmptyContent()
        else if (sortState is RequestState.Success) {
            when (sortState.data) {
                Priority.NONE -> {
                    DisableTasks(
                        tasks.data,
                        onSwipeToDelete = onSwipeToDelete,
                        navigateToTaskScreen
                    )
                }

                Priority.LOW -> {
                    DisableTasks(
                        lowPriorityTasks,
                        onSwipeToDelete = onSwipeToDelete,
                        navigateToTaskScreen
                    )
                }

                Priority.HIGH -> {
                    DisableTasks(
                        highPriorityTasks,
                        onSwipeToDelete = onSwipeToDelete,
                        navigateToTaskScreen
                    )
                }

                Priority.MEDIUM -> {}
            }
        } else
            DisableTasks(
                tasks = tasks.data,
                onSwipeToDelete = onSwipeToDelete,
                navigateToTaskScreen
            )
}

@Composable
fun DisableTasks(
    tasks: List<ToDoTask>,
    onSwipeToDelete: (Action, ToDoTask) -> Unit,
    navigateToTaskScreen: (taskId: Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier.padding(8.dp)
    ) {
        items(tasks,
            key = { task ->
                task.id
            }) { task ->

            var isDismissed by remember { mutableStateOf(false) }
            val dismissState = rememberSwipeToDismissBoxState(
                confirmValueChange = { value ->
                    if (value == SwipeToDismissBoxValue.EndToStart) {
                        isDismissed = true
                        true
                    } else false
                },
                positionalThreshold = { value ->
                    value * .6f
                }
            )
            val dismissDirection = dismissState.dismissDirection

            if (isDismissed && dismissDirection == SwipeToDismissBoxValue.EndToStart) {
                val scope = rememberCoroutineScope()
                LaunchedEffect(key1 = true) {
                    scope.launch {
                        delay(300)
                        onSwipeToDelete(Action.DELETE, task)
                    }
                }
            }

            val degrees by animateFloatAsState(
                targetValue = if (dismissState.targetValue == SwipeToDismissBoxValue.Settled) 0f else -45f,
                label = ""
            )

            var itemVisible by remember { mutableStateOf(false) }
            LaunchedEffect(key1 = true) {
                itemVisible = true
            }


            AnimatedVisibility(
                visible = itemVisible && !isDismissed,
                enter = expandVertically(
                    animationSpec = tween(300)
                ),
                exit = shrinkVertically(
                    animationSpec = tween(300)
                )
            ) {
                SwipeToDismissBox(
                    state = dismissState,
                    enableDismissFromEndToStart = true,
                    backgroundContent = {
                        DeleteBackground(
                            degrees = degrees,
                            dismissState = dismissState
                        )
                    },
                    enableDismissFromStartToEnd = false,

                    ) {
                    TaskItem(
                        task,
                        navigateToTaskScreen
                    )
                }
            }

            if (tasks.indexOf(task) < tasks.size - 1)
                HorizontalDivider(
                    thickness = 0.3.dp, color = Color.Gray,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .alpha(.5f)
                )
        }
    }
}

@Composable
fun DeleteBackground(
    degrees: Float,
    dismissState: SwipeToDismissBoxState
) {

    val backgroundColor =
        if (dismissState.dismissDirection == SwipeToDismissBoxValue.EndToStart) highPriorityColor else Color.Transparent

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(24.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        Icon(
            modifier = Modifier.rotate(degrees),
            imageVector = Icons.Filled.Delete,
            contentDescription = "Delete Icon",
            tint = Color.White
        )
    }
}

@Composable
fun TaskItem(
    toDoTask: ToDoTask,
    navigateToTaskScreen: (taskId: Int) -> Unit
) {
    Surface(
        onClick = { navigateToTaskScreen(toDoTask.id) },
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = toDoTask.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Canvas(modifier = Modifier.size(16.dp)) {
                    drawCircle(toDoTask.priority.color)
                }
            }
            Text(
                text = toDoTask.description,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview
@Composable
private fun TaskItemPrev() {
    TaskItem(
        ToDoTask(
            10,
            "Do exercise",
            "i will do exercise at 6 AM",
            Priority.MEDIUM
        )
    ) {

    }
}