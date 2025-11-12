package com.example.todolist.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.todolist.viewmodel.TodoFilter
import com.example.todolist.viewmodel.TodoViewModelWithRepo


@Composable
fun TodoScreen(vm: TodoViewModelWithRepo) {
    val todos by vm.visibleTodos.collectAsState()   // gunakan daftar yang sudah terfilter
    val currentFilter by vm.filter.collectAsState()
    var text by rememberSaveable { mutableStateOf("") }

    Column(Modifier.padding(16.dp)) {
        // Input tambah tugas
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Tambah tugas...") },
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = {
                if (text.isNotBlank()) {
                    vm.addTask(text.trim())
                    text = ""
                }
            },
            modifier = Modifier.padding(vertical = 8.dp)
        ) { Text("Tambah") }

        // Row Filter
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = currentFilter == TodoFilter.ALL,
                onClick = { vm.setFilter(TodoFilter.ALL) },
                label = { Text("Semua") }
            )
            FilterChip(
                selected = currentFilter == TodoFilter.ACTIVE,
                onClick = { vm.setFilter(TodoFilter.ACTIVE) },
                label = { Text("Aktif") }
            )
            FilterChip(
                selected = currentFilter == TodoFilter.DONE,
                onClick = { vm.setFilter(TodoFilter.DONE) },
                label = { Text("Selesai") }
            )
        }

        Divider()

        // Daftar item (sudah terfilter)
        LazyColumn {
            items(todos) { todo ->
                TodoItem(
                    todo = todo,
                    onToggle = { vm.toggleTask(todo.id) },
                    onDelete = { vm.deleteTask(todo.id) }
                )
            }
        }
    }
}
