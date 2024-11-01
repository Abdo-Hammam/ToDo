package com.self.todo.navigation.destinations

import android.util.Log
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.self.todo.ui.screens.list.ListScreen
import com.self.todo.ui.viewmodels.SharedViewModel
import com.self.todo.util.Constant.LIST_ARGUMENT_KEY
import com.self.todo.util.Constant.LIST_SCREEN
import com.self.todo.util.toAction

fun NavGraphBuilder.listComposable(
    navigateToTaskScreen: (Int) -> Unit,
    sharedViewModel: SharedViewModel
) {

    composable(
        route = LIST_SCREEN,
        arguments = listOf(navArgument(LIST_ARGUMENT_KEY) {
            type = NavType.StringType
        }),
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                tween(500)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                tween(500)
            )
        }
    ) {navBackStackEntry ->

        val action = navBackStackEntry.arguments!!.getString(LIST_ARGUMENT_KEY).toAction()
        LaunchedEffect(key1 = action) {
            sharedViewModel.action.value = action
        }

        ListScreen(
            navigateToTaskScreen,
            sharedViewModel
        )

    }

}