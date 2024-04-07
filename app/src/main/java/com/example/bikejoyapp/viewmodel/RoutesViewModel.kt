package com.example.bikejoyapp.viewmodel

import android.util.Log
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
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RoutesViewModel : ViewModel() {
    // LiveData que contiene la lista de rutas
    private val _routes = MutableLiveData<List<Route>>(listOf())
    val routes: LiveData<List<Route>> = _routes

    private val _searchQuery = MutableLiveData<String>()
    val searchQuery: LiveData<String> = _searchQuery

    val durationRange = 0f..6f

    private val _durationFilter = MutableLiveData<Float>()
    val durationFilter: LiveData<Float> = _durationFilter

    val distanceRange = 0f..10f

    private val _distanceFilter = MutableLiveData<Float>()
    val distanceFilter: LiveData<Float> = _distanceFilter

    val startLocations = listOf("Cualquier zona",
        "Barri Gotic",
        "El Poble Sec",
        "El Born",
        "El Clot",
        "El Poblenou",
        "El Putxet",
        "El Raval",
        "El Tibidabo",
        "El Vall d'Hebron",
        "Horta",
        "La Barceloneta",
        "La Sagrada Familia",
        "Les Corts",
        "Sant Andreu",
        "Sant Antoni",
        "Sant Gervasi",
        "Sant Marti",
        "Sants",
        "Sarria")

    private val _startLocationFilter = MutableLiveData<String>()
    val startLocationFilter: LiveData<String> = _startLocationFilter



    fun onDurationChange(value: Float) {
        _durationFilter.value = value
        Log.d("Filters", "Duración cambiada a: $value")
    }

    fun onDistanceChange(value: Float) {
        _distanceFilter.value = value
        Log.d("Filters", "Distancia cambiada a: $value")
    }

    fun onStartLocationChange(location: String) {
        _startLocationFilter.value = location
        Log.d("Filters", "Ubicación de inicio cambiada a: $location")
    }


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


    // Función para realizar la búsqueda con los filtros activos.
    fun performSearchWithFilters() {
        // Aquí deberías implementar la lógica de búsqueda con los filtros activos
    }
    fun performSearch() {
        // Aquí deberías implementar la lógica de búsqueda
    }

    interface ApiService {
        // Asume que ya tienes otros endpoints aquí
        @GET("searchRoutes")
        suspend fun searchRoutes(
            @Query("duration") duration: Float,
            @Query("distance") distance: Float,
            @Query("startLocation") startLocation: String
        ): Response<List<Route>>
    }




}