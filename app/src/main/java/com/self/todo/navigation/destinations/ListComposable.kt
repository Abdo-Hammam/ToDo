package com.self.todo.navigation.destinations

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.self.todo.ui.screens.list.ListScreen
import com.self.todo.ui.viewmodels.SharedViewModel
import com.self.todo.util.constants.LIST_ARGUMENT_KEY
import com.self.todo.util.constants.LIST_SCREEN

fun NavGraphBuilder.listComposable(
    navigateToTaskScreen: (Int) -> Unit,
    sharedViewModel: SharedViewModel
) {

    composable(
        route = LIST_SCREEN,
        arguments = listOf(navArgument(LIST_ARGUMENT_KEY) {
            type = NavType.StringType
        })
    ) {

        ListScreen(
            navigateToTaskScreen,
            sharedViewModel
        )

    }

}