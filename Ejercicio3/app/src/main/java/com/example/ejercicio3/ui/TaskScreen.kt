package com.example.ejercicio3.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ejercicio3.model.Task
import com.example.ejercicio3.model.TaskState
import com.example.ejercicio3.viewmodel.TaskViewModel

@Composable
fun Ejercicio3Screen(
    viewModel: TaskViewModel = viewModel()
) {
    // Observamos el estado del ViewModel
    val taskList by viewModel.tasks.collectAsState()
    var newTaskText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // --- SECCIÓN DE AGREGAR ---
        Text(
            text = "Mis Tareas",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = newTaskText,
                onValueChange = { newTaskText = it },
                label = { Text("Nueva tarea...") },
                modifier = Modifier.weight(1f),
                singleLine = true
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    viewModel.addTask(newTaskText)
                    newTaskText = "" // Limpiar campo
                },
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- LISTA DE TAREAS (LazyColumn) ---
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f) // Ocupar espacio restante
        ) {
            items(
                items = taskList,
                key = { it.id } // Importante para animaciones eficientes
            ) { task ->
                // COMPONENTE REUTILIZABLE
                TaskItem(
                    task = task,
                    onToggle = { viewModel.toggleTaskState(task.id) },
                    onDelete = { viewModel.deleteTask(task.id) }
                )
            }
        }
    }
}

/**
 * BONUS: Componente Reutilizable Complejo con Animaciones
 * Usa AnimatedVisibility y animateColorAsState
 */
@Composable
fun TaskItem(
    task: Task,
    onToggle: () -> Unit,
    onDelete: () -> Unit
) {
    // Animación de color de fondo: Verde suave si está completa, superficie si no
    val backgroundColor by animateColorAsState(
        targetValue = if (task.state is TaskState.Completed)
            Color(0xFFE8F5E9) else MaterialTheme.colorScheme.surfaceVariant,
        label = "bgColorAnimation"
    )

    // Animación de tachado de texto
    val isCompleted = task.state is TaskState.Completed
    val textDecoration = if (isCompleted) TextDecoration.LineThrough else TextDecoration.None
    val textColor = if (isCompleted) Color.Gray else MaterialTheme.colorScheme.onSurface

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Checkbox con lógica de estado
            Checkbox(
                checked = isCompleted,
                onCheckedChange = { onToggle() }
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Texto de la tarea
            Text(
                text = task.title,
                textDecoration = textDecoration,
                color = textColor,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge
            )

            // Botón de eliminar con Animación de Entrada (BONUS)
            // El botón de eliminar solo es visible/destacado si la tarea está completada
            // (Es una decisión de diseño para mostrar AnimatedVisibility)
            AnimatedVisibility(
                visible = isCompleted,
                enter = scaleIn(animationSpec = spring()) + fadeIn(),
                exit = fadeOut()
            ) {
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }

            // Si no está completada, mostramos el botón normal (o nada, según diseño)
            if (!isCompleted) {
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        tint = Color.Gray
                    )
                }
            }
        }
    }
}