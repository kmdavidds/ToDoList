package com.example.todolist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.example.todolist.model.Todo
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class TodoViewModel : ViewModel() {
    private val _todos = MutableStateFlow<List<Todo>>(emptyList())
    val todos: StateFlow<List<Todo>> = _todos

    fun addTask(title: String) {
        val nextId = (_todos.value.maxOfOrNull { it.id } ?: 0) + 1
        val newTask = Todo(id = nextId, title = title)
        _todos.value = _todos.value + newTask
    }
    fun toggleTask(id: Int) {
        _todos.value = _todos.value.map { t ->
            if (t.id == id) t.copy(isDone = !t.isDone) else t
        }
    }
    fun deleteTask(id: Int) {
        _todos.value = _todos.value.filterNot { it.id == id }
    }
}