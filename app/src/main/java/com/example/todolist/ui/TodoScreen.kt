package com.example.todolist.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todolist.viewmodel.TodoFilter
import com.example.todolist.viewmodel.TodoViewModelWithRepo


@Composable
fun TodoScreen(vm: TodoViewModelWithRepo) {
    val todos by vm.visibleTodos.collectAsState()   // gunakan daftar yang sudah terfilter
    val currentFilter by vm.filter.collectAsState()
    val searchQuery by vm.searchQuery.collectAsState()
    val doneCount by vm.doneCount.collectAsState()
    val activeCount by vm.activeCount.collectAsState()

    var text by rememberSaveable { mutableStateOf("") }

    Column(Modifier.padding(16.dp)) {
        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { vm.setSearchQuery(it) },
            label = { Text("Cari tugas...") },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { vm.setSearchQuery("") }) {
                        Icon(Icons.Default.Clear, contentDescription = "Hapus pencarian")
                    }
                }
            },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

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

        // Todo Count Display
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Card(
                modifier = Modifier.weight(1f),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF4CAF50).copy(alpha = 0.1f)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "$doneCount",
                        fontSize = 24.sp,
                        color = Color(0xFF4CAF50)
                    )
                    Text(
                        text = "Selesai",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }

            Card(
                modifier = Modifier.weight(1f),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF2196F3).copy(alpha = 0.1f)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "$activeCount",
                        fontSize = 24.sp,
                        color = Color(0xFF2196F3)
                    )
                    Text(
                        text = "Aktif",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }
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