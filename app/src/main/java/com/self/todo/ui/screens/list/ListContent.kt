package com.self.todo.ui.screens.list

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.self.todo.data.models.Priority
import com.self.todo.data.models.ToDoTask
import com.self.todo.util.RequestState

@Composable
fun ListContent(
    tasks: RequestState<List<ToDoTask>>,
    navigateToTaskScreen: (taskId: Int) -> Unit
) {
    if (tasks is RequestState.Success)
        if (tasks.data.isEmpty()) EmptyContent()
        else DisableTasks(tasks.data, navigateToTaskScreen)
}

@Composable
fun DisableTasks(
    tasks: List<ToDoTask>,
    navigateToTaskScreen: (taskId: Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier.padding(8.dp)
    ) {
        items(tasks,
            key = { task ->
                task.id
            }) { task ->
            TaskItem(
                task,
                navigateToTaskScreen
            )
            if (tasks.indexOf(task) < tasks.size - 1)
                HorizontalDivider(thickness = 0.3.dp, color = Color.Gray,
                    modifier = Modifier.padding(horizontal = 8.dp).alpha(.5f))
        }
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