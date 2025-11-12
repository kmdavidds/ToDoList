package com.example.todolist.data

import com.example.todolist.model.Todo
import com.example.todolist.viewmodel.TodoRepository

class TodoRepositoryImpl : TodoRepository {
    private val todos = mutableListOf<Todo>()

    override suspend fun loadAll(): List<Todo> = todos.toList()

    override suspend fun insert(title: String) {
        val nextId = (todos.maxOfOrNull { it.id } ?: 0) + 1
        todos.add(Todo(id = nextId, title = title))
    }

    override suspend fun toggle(id: Int) {
        val index = todos.indexOfFirst { it.id == id }
        if (index != -1) {
            val t = todos[index]
            todos[index] = t.copy(isDone = !t.isDone)
        }
    }

    override suspend fun delete(id: Int) {
        todos.removeAll { it.id == id }
    }
}

