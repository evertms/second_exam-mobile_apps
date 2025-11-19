# Gestor de tareas simple - Arquitectura MVVM
Este proyecto implementa un gestor de tareas utilizando la arquitectura MVVM (Model-View-ViewModel) con Jetpack Compose.  
## Arquitectura elegida: MVVM
Se ha seleccionado **MVVM** como patrón de arquitectura de presentación por las siguientes razones prácticas y teóricas:
- Es un estándar de la industria, recomendada por Google
- El uso de ViewModel permite mantener el estado de la UI, o sea, la lista de tareas, independientemente de cambios como rotación de pantalla, etc.
- Separación de responsabilidades: model para la estructura de datos y lógica de estado, viewmodel para la lógica de negocio y exponer el estado en forma reactiva; y view que se encarga de renderizar la interfaz.
- MVVM se integra naturalmente con compose por su naturaleza declarativa y reactiva mediante ``StateFlow`` y `collectAsState()`
## Por qué no usé MVI
- Era sobreingeniería para este ejercicio, solo teníamos una sola pantalla y hubiéramos hecho demasiadas carpetas y archivos
- Simplicidad y velocidad
- Era suficiente con MVVM, además de que también es una práctica recomendada y no es un patrón obsoleto como MVC o MVP para el desarrollo Android con Jetpack Compose