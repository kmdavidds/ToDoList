package com.example.todolist.model

data class Todo(
    val id: Int,
    val title: String,
    val isDone: Boolean = false
)