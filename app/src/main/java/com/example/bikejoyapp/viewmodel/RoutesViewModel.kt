package com.example.bikejoyapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bikejoyapp.R
import com.example.bikejoyapp.data.Route

class RoutesViewModel : ViewModel() {
    // LiveData que contiene la lista de rutas
    private val _routes = MutableLiveData<List<Route>>(listOf())
    val routes: LiveData<List<Route>> = _routes

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
}