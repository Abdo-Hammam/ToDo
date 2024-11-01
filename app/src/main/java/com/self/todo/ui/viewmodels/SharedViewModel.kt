package com.self.todo.ui.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.self.todo.data.models.Priority
import com.self.todo.data.models.ToDoTask
import com.self.todo.data.repositories.ToDoRepository
import com.self.todo.util.Action
import com.self.todo.util.RequestState
import com.self.todo.util.SearchAppBarState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val repo: ToDoRepository
) : ViewModel() {

    val action: MutableState<Action> = mutableStateOf(Action.NO_ACTION)

    private val id: MutableState<Int> = mutableIntStateOf(0)
    val title: MutableState<String> = mutableStateOf("")
    val description: MutableState<String> = mutableStateOf("")
    val priority: MutableState<Priority> = mutableStateOf(Priority.LOW)

    var searchAppBarState: MutableState<SearchAppBarState> =
        mutableStateOf(SearchAppBarState.CLOSED)

    var searchTextState: MutableState<String> = mutableStateOf("")

    private val _searchedTasks =
        MutableStateFlow<RequestState<List<ToDoTask>>>(RequestState.Idle)
    val searchedTasks: StateFlow<RequestState<List<ToDoTask>>> = _searchedTasks


    private val _allTasks =
        MutableStateFlow<RequestState<List<ToDoTask>>>(RequestState.Idle)
    val allTasks: StateFlow<RequestState<List<ToDoTask>>> = _allTasks

    fun getSelectedTask(taskId: Int) {
        viewModelScope.launch {
            repo.getSelectedTask(taskId = taskId).collect { task ->
                _selectedTask.value = task
            }
        }
    }

    fun getAllTasks() {
        _allTasks.value = RequestState.Loading
        try {
            viewModelScope.launch {
                if (searchTextState.value.isNotEmpty()) {
                    repo.searchDatabase(searchTextState.value).collect {
                        _searchedTasks.value = RequestState.Success(it)
                    }
                } else repo.getAllTasks.collect {
                    _allTasks.value = RequestState.Success(it)
                }
            }
        } catch (e: Exception) {
            _allTasks.value = RequestState.Error(e)
        }
    }

    private var _selectedTask: MutableStateFlow<ToDoTask?> = MutableStateFlow(null)
    val selectedTask: StateFlow<ToDoTask?> = _selectedTask

    private fun addTask() {
        viewModelScope.launch(Dispatchers.IO) {
            val todoTask = ToDoTask(
                title = title.value,
                description = description.value.trim(),
                priority = priority.value
            )
            repo.addTask(toDoTask = todoTask)
        }
    }

    private fun updateTask() {
        viewModelScope.launch(Dispatchers.IO) {
            val todoTask = ToDoTask(
                id = id.value,
                title = title.value,
                description = description.value.trim(),
                priority = priority.value
            )
            repo.updateTask(toDoTask = todoTask)
        }
    }

    private fun deleteTask() {
        viewModelScope.launch(Dispatchers.IO) {
            val todoTask = ToDoTask(
                id = id.value,
                title = title.value,
                description = description.value.trim(),
                priority = priority.value
            )
            repo.deleteTask(todoTask)
        }
    }

    private fun deleteAllTasks() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteAllTask()
        }
    }

    fun handleDatabaseActions(action: Action) {
        when (action) {
            Action.ADD -> {
                addTask()
            }

            Action.UPDATE -> {
                updateTask()
            }

            Action.DELETE -> {
                deleteTask()
            }

            Action.DELETE_ALL -> {
                deleteAllTasks()
            }

            Action.UNDO -> {
                addTask()
            }

            else -> {

            }
        }
        this.action.value = Action.NO_ACTION
    }


    fun updateTaskFields(selectedTask: ToDoTask?) {
        if (selectedTask != null) {
            id.value = selectedTask.id
            title.value = selectedTask.title
            description.value = selectedTask.description
            priority.value = selectedTask.priority
        } else {
            id.value = 0
            title.value = ""
            description.value = ""
            priority.value = Priority.LOW
        }
    }

    fun validateFields(): Boolean {
        return (title.value.isNotEmpty() && description.value.isNotEmpty())
    }

}