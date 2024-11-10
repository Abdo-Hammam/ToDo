package com.self.todo.navigation.destinations

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.self.todo.navigation.Screen
import com.self.todo.ui.screens.list.ListScreen
import com.self.todo.ui.viewmodels.SharedViewModel
import com.self.todo.util.Action

fun NavGraphBuilder.listComposable(
    navigateToTaskScreen: (Int) -> Unit,
    sharedViewModel: SharedViewModel,
    startScreenVisited: Boolean
) {

    var startScreen = startScreenVisited
    composable<Screen.List>(
        enterTransition = {
            if (!startScreen) null else slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                tween(350)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                tween(500)
            )
        }
    ) { navBackStackEntry ->


        val action = navBackStackEntry.toRoute<Screen.List>().action

        var myAction by rememberSaveable { mutableStateOf(Action.NO_ACTION) }

        LaunchedEffect(key1 = myAction) {
            if (action != myAction) {
                myAction = action
                sharedViewModel.updateAction(action)
            }
        }
        val databaseAction = sharedViewModel.action

        LaunchedEffect(key1 = true) {
            startScreen = true
        }

        ListScreen(
            action = databaseAction,
            navigateToTaskScreen,
            sharedViewModel
        )

    }


}