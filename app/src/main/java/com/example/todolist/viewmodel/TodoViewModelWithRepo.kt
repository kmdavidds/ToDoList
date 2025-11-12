package com.example.todolist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.todolist.model.Todo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.launch

interface TodoRepository {
    suspend fun loadAll(): List<Todo>
    suspend fun insert(title: String)
    suspend fun toggle(id: Int)
    suspend fun delete(id: Int)
}

enum class TodoFilter { ALL, ACTIVE, DONE }

class TodoViewModelWithRepo(
    private val repo: TodoRepository
) : ViewModel() {

    private val _todos = MutableStateFlow<List<Todo>>(emptyList())
    val todos: StateFlow<List<Todo>> = _todos

    private val _filter = MutableStateFlow(TodoFilter.ALL)
    val filter: StateFlow<TodoFilter> = _filter

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    // Daftar yang sudah terfilter sesuai pilihan user dan search query
    val visibleTodos: StateFlow<List<Todo>> =
        combine(_todos, _filter, _searchQuery) { list, f, query ->
            val filtered = when (f) {
                TodoFilter.ALL    -> list
                TodoFilter.ACTIVE -> list.filter { !it.isDone }
                TodoFilter.DONE   -> list.filter { it.isDone }
            }

            if (query.isBlank()) {
                filtered
            } else {
                filtered.filter { it.title.contains(query, ignoreCase = true) }
            }
        }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    // Hitung jumlah todo yang selesai
    val doneCount: StateFlow<Int> = _todos
        .combine(MutableStateFlow(Unit)) { list, _ ->
            list.count { it.isDone }
        }.stateIn(viewModelScope, SharingStarted.Eagerly, 0)

    // Hitung jumlah todo yang aktif
    val activeCount: StateFlow<Int> = _todos
        .combine(MutableStateFlow(Unit)) { list, _ ->
            list.count { !it.isDone }
        }.stateIn(viewModelScope, SharingStarted.Eagerly, 0)

    init {
        viewModelScope.launch { _todos.value = repo.loadAll() }
    }

    fun setFilter(f: TodoFilter) {
        _filter.value = f
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun addTask(title: String) {
        viewModelScope.launch {
            repo.insert(title)
            _todos.value = repo.loadAll()
        }
    }

    fun toggleTask(id: Int) {
        viewModelScope.launch {
            repo.toggle(id)
            _todos.value = repo.loadAll()
        }
    }

    fun deleteTask(id: Int) {
        viewModelScope.launch {
            repo.delete(id)
            _todos.value = repo.loadAll()
        }
    }
}

class TodoViewModelFactory(
    private val repo: TodoRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return TodoViewModelWithRepo(repo) as T
    }
}