package com.example.fibonacci.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fibonacci.viewmodel.FibonacciViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FibonacciScreen(
    // Inyectamos el ViewModel. Si usamos Hilt sería diferente,
    // pero viewModel() es el estándar para proyectos simples.
    viewModel: FibonacciViewModel = viewModel()
) {
    // 1. Observar el estado de forma reactiva
    val state by viewModel.uiState.collectAsState()

    // Estado local para el texto del input (lo que el usuario escribe)
    var inputText by remember { mutableStateOf("") }

    // Estado para controlar el Snackbar
    val snackbarHostState = remember { SnackbarHostState() }

    // Efecto secundario: Mostrar Snackbar cuando hay un error
    LaunchedEffect(state.errorMessage) {
        state.errorMessage?.let { error ->
            snackbarHostState.showSnackbar(
                message = error,
                duration = SnackbarDuration.Short
            )
            // Una vez mostrado, le decimos al VM que limpie el error
            viewModel.clearError()
        }
    }

    Scaffold(
        // Contenedor del Snackbar
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Generador Fibonacci") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // Respetar padding del Scaffold
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                text = "Ingresa cuántos términos quieres ver:",
                style = MaterialTheme.typography.bodyLarge
            )

            // Campo de Texto
            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it },
                label = { Text("Número entero (n)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            // Botón de Generar
            Button(
                onClick = { viewModel.generateFibonacciSequence(inputText) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Generar Serie")
            }

            HorizontalDivider()

            Text(
                text = "Resultado:",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(Alignment.Start)
            )

            // Área de texto con Scroll (por si la serie es larga)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f) // Ocupa el espacio restante
                    .verticalScroll(rememberScrollState()) // Habilita scroll vertical
            ) {
                if (state.resultSequence.isNotEmpty()) {
                    Text(
                        text = state.resultSequence,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Start
                    )
                } else {
                    Text(
                        text = "El resultado aparecerá aquí...",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}