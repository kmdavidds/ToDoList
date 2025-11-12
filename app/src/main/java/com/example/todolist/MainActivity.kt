package com.example.todolist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todolist.data.TodoRepositoryImpl
import com.example.todolist.ui.TodoScreen
import com.example.todolist.viewmodel.TodoViewModelFactory
import com.example.todolist.viewmodel.TodoViewModelWithRepo

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                // Inisialisasi repository dan factory
                val repo = TodoRepositoryImpl()
                val factory = TodoViewModelFactory(repo)

                // Buat instance ViewModel lewat factory
                val vm: TodoViewModelWithRepo = viewModel(factory = factory)

                // Panggil UI utama dengan ViewModel yang terhubung ke repo
                TodoScreen(vm)
            }
        }
    }
}
