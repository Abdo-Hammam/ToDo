package com.self.todo.data.repositories

import com.self.todo.data.ToDoDao
import com.self.todo.data.models.ToDoTask
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class ToDoRepository @Inject constructor(private val todoDao: ToDoDao){

    val getAllTasks: Flow<List<ToDoTask>> = todoDao.getAllTasks()
    val sortLowPriority: Flow<List<ToDoTask>> = todoDao.sortByLowPriority()
    val sortHighPriority: Flow<List<ToDoTask>> = todoDao.sortByHighPriority()

    fun getSelectedTask(taskId: Int): Flow<ToDoTask> {
        return todoDao.getSelectedTask(taskId)
    }

    suspend fun addTask(toDoTask: ToDoTask) {
        todoDao.addTask(toDoTask)
    }

    suspend fun updateTask(toDoTask: ToDoTask) {
        todoDao.updateTask(toDoTask)
    }

    suspend fun deleteTask(toDoTask: ToDoTask) {
        todoDao.deleteTask(toDoTask)
    }

    suspend fun deleteAllTask() {
        todoDao.deleteAllTasks()
    }

    fun searchDatabase(searchQuery: String): Flow<List<ToDoTask>> {
        return todoDao.searchDataBase(searchQuery)
    }

}