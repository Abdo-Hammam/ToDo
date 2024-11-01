package com.self.todo.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.self.todo.navigation.destinations.listComposable
import com.self.todo.navigation.destinations.taskComposable
import com.self.todo.ui.viewmodels.SharedViewModel
import com.self.todo.util.Action

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    sharedViewModel: SharedViewModel
) {

    val screen = remember(navController) {
        Screens(navController)
    }

    NavHost(
        navController = navController,
        startDestination = "list/${Action.NO_ACTION}"
    ) {

        listComposable(
            navigateToTaskScreen = screen.task,
            sharedViewModel = sharedViewModel
        )

        taskComposable(
            sharedViewModel = sharedViewModel,
            navigateToListScreen = screen.list
        )

    }

}