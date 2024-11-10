package com.self.todo.navigation

import com.self.todo.util.Action
import kotlinx.serialization.Serializable

//class Screens(navController: NavHostController) {
//    val list: (Action) -> Unit = {action ->
//        navController.navigate("list/${action.name}") {
//            popUpTo(LIST_SCREEN) {inclusive = true}
//        }
//    }
//
//    val task: (Int) -> Unit = {taskId ->
//        navController.navigate("task/$taskId")
//    }
//}


@Serializable
sealed class Screen {
    @Serializable
    data class List(val action: Action = Action.NO_ACTION): Screen()
    @Serializable
    data class Task(val id: Int): Screen()
}