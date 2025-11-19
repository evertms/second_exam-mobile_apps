package com.example.fibonacci.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// 1. Definimos el Estado de la UI (Model en MVI/MVVM)
// Es un data class inmutable que representa todo lo que la pantalla necesita pintar.
data class FibonacciUiState(
    val resultSequence: String = "",
    val errorMessage: String? = null
)

class FibonacciViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(FibonacciUiState())

    // uiState es público e inmutable (la UI solo lo lee/observa)
    val uiState: StateFlow<FibonacciUiState> = _uiState.asStateFlow()

    fun generateFibonacciSequence(inputText: String) {
        clearError()

        // Validación 1: ¿Es un número?
        val n = inputText.toIntOrNull()
        if (n == null) {
            _uiState.update { it.copy(errorMessage = "Por favor, ingresa un número válido") }
            return
        }

        // Validación 2: ¿Es mayor que 0?
        if (n <= 0) {
            _uiState.update { it.copy(errorMessage = "El número debe ser mayor a 0") }
            return
        }

        // Validación 3 (Opcional): Evitar números gigantes que congelen la UI o desborden Long
        if (n > 93) { // El término 93 es el máximo que cabe en un Long positivo
            _uiState.update { it.copy(errorMessage = "El límite máximo es 93 para evitar desbordamiento") }
            return
        }

        // Cálculo Iterativo (Eficiente)
        val sequence = calculateFibonacciIterative(n)

        // Actualizamos el estado con el resultado
        _uiState.update {
            it.copy(resultSequence = sequence.joinToString(", "))
        }
    }

    private fun calculateFibonacciIterative(n: Int): List<Long> {
        val list = ArrayList<Long>()

        if (n >= 1) list.add(0)
        if (n >= 2) list.add(1)

        // Bucle desde el 3er término hasta n
        for (i in 2 until n) {
            val next = list[i - 1] + list[i - 2]
            list.add(next)
        }
        return list
    }

    // Función para limpiar el error después de mostrar el Snackbar
    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}