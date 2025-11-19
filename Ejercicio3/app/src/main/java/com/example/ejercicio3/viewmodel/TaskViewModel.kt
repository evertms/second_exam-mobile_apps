package com.example.ejercicio3.viewmodel

import androidx.lifecycle.ViewModel
import com.example.ejercicio3.model.Task
import com.example.ejercicio3.model.TaskState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TaskViewModel : ViewModel() {

    // Estado Global: Lista de tareas
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()

    // Agregar Tarea
    fun addTask(title: String) {
        if (title.isBlank()) return
        _tasks.update { currentList ->
            // Añadimos al inicio de la lista para verla llegar
            listOf(Task(title = title)) + currentList
        }
    }

    // Eliminar Tarea
    fun deleteTask(taskId: String) {
        _tasks.update { currentList ->
            currentList.filter { it.id != taskId }
        }
    }

    // Cambiar Estado (Toggle Pending <-> Completed)
    fun toggleTaskState(taskId: String) {
        _tasks.update { currentList ->
            currentList.map { task ->
                if (task.id == taskId) {
                    // Lógica de transición de estado
                    val newState = when (task.state) {
                        TaskState.Pending -> TaskState.Completed
                        TaskState.Completed -> TaskState.Pending
                    }
                    task.copy(state = newState)
                } else {
                    task
                }
            }
        }
    }
}