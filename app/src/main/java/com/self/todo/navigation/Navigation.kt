package com.self.todo.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.self.todo.navigation.destinations.listComposable
import com.self.todo.navigation.destinations.taskComposable
import com.self.todo.ui.viewmodels.SharedViewModel

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    sharedViewModel: SharedViewModel
) {

    val startScreenVisited by remember { mutableStateOf(false) }


    NavHost(
        navController = navController,
        startDestination = Screen.List()
    ) {

        listComposable(
            navigateToTaskScreen = {taskId ->
                navController.navigate(Screen.Task(id = taskId))
            },
            sharedViewModel = sharedViewModel,
            startScreenVisited = startScreenVisited
        )

        taskComposable(
            sharedViewModel = sharedViewModel,
            navigateToListScreen = {action ->
                navController.navigate(Screen.List(action = action)){
                    popUpTo(Screen.List()) {inclusive = true}
                }
            }
        )

    }

}