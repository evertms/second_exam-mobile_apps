package com.example.ejercicio3.model

import java.util.UUID

// Definimos los estados posibles de una tarea como tipos concretos.
sealed interface TaskState {
    object Pending : TaskState
    object Completed : TaskState
}

// Nuestro modelo de datos
data class Task(
    val id: String = UUID.randomUUID().toString(), // ID único automático
    val title: String,
    val state: TaskState = TaskState.Pending // Estado inicial por defecto
)