package com.example.bikejoyapp.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bikejoyapp.R
import com.example.bikejoyapp.data.MyAppRoute
import com.example.bikejoyapp.data.Route
import com.example.bikejoyapp.ui.GravarRutaScreen

class RoutesViewModel : ViewModel() {
    // LiveData que contiene la lista de rutas
    private val _routes = MutableLiveData<List<Route>>(listOf())
    val routes: LiveData<List<Route>> = _routes

    private val _searchQuery = MutableLiveData<String>()
    val searchQuery: LiveData<String> = _searchQuery

    private val _possibleFilters = MutableLiveData<List<String>>()
    val possibleFilters = listOf("Corta", "Media", "Larga")


    private val _activeFilters = mutableStateOf<List<String>>(emptyList())
    val activeFilters: MutableState<List<String>> = _activeFilters


    init {
        // Añade aquí tus datos falsos de ejemplo
        _routes.value = listOf(
            Route("Ruta 1", "Descripción de la ruta 1", R.drawable.ic_launcher_foreground),
            Route("Ruta 2", "Descripción de la ruta 2", R.drawable.ic_launcher_foreground),
            Route("Ruta 3", "Descripción de la ruta 3", R.drawable.ic_launcher_foreground),
            Route("Ruta 4", "Descripción de la ruta 4", R.drawable.ic_launcher_foreground),
            Route("Ruta 5", "Descripción de la ruta 5", R.drawable.ic_launcher_foreground),
            Route("Ruta 6", "Descripción de la ruta 6", R.drawable.ic_launcher_foreground),
            Route("Ruta 7", "Descripción de la ruta 7", R.drawable.ic_launcher_foreground),
            Route("Ruta 8", "Descripción de la ruta 8", R.drawable.ic_launcher_foreground),
            Route("Ruta 9", "Descripción de la ruta 9", R.drawable.ic_launcher_foreground),
            Route("Ruta 10", "Descripción de la ruta 10", R.drawable.ic_launcher_foreground)
        )
    }
    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun onFilterChanged(filter: String, isSelected: Boolean) {
        val currentFilters = activeFilters.value.toMutableList()
        if (isSelected && !currentFilters.contains(filter)) {
            currentFilters.add(filter)
        } else if (!isSelected) {
            currentFilters.remove(filter)
        }
        activeFilters.value = currentFilters
    }

    // Función para realizar la búsqueda con los filtros activos.
    fun performSearchWithFilters() {
        // Aquí podrías llamar a la API de tu backend pasando los filtros activos.
        // Esta sería una llamada de red similar a la implementación de la función performSearch().
    }
    fun performSearch() {
        // Aquí deberías implementar la lógica de búsqueda
    }



}