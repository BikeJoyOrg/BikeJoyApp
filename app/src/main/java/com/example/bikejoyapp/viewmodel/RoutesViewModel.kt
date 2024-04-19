package com.example.bikejoyapp.viewmodel

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bikejoyapp.R
import com.example.bikejoyapp.data.MyAppRoute
import com.example.bikejoyapp.data.Route
import com.example.bikejoyapp.data.RutaUsuari
import com.example.bikejoyapp.ui.GravarRutaScreen
import kotlinx.coroutines.launch
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RoutesViewModel : ViewModel() {
    // LiveData que contiene la lista de rutas
    private val _routes = MutableLiveData<List<RutaUsuari>>(listOf())
    val routes: LiveData<List<RutaUsuari>> = _routes

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

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://nattech.fib.upc.edu:40360/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService = retrofit.create(ApiService::class.java)

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

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }


    // Función para realizar la búsqueda con los filtros activos.
    fun performSearchWithFilters() {
        // Aquí deberías implementar la lógica de búsqueda con los filtros activos
        viewModelScope.launch {
            try {
                val startLocation = startLocationFilter.value ?: "Cualquier zona"
                val response = apiService.searchRoutes(null, distanceFilter.value, durationFilter.value?.toInt(), startLocation)
                if (response.isSuccessful && response.body() != null) {
                    _routes.postValue(response.body())
                } else {
                    Log.e("API Error", "Failed with response: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("API Exception", "Error occurred: ${e.message}")
            }

        }
    }
    fun performSearch() {
        // Aquí deberías implementar la lógica de búsqueda con los filtros activos
        viewModelScope.launch {
            try {
                val response = apiService.searchRoutes(searchQuery.value, null, null, "Cualquier zona")
                if (response.isSuccessful && response.body() != null) {
                    _routes.postValue(response.body())
                } else {
                    Log.e("API Error", "Failed with response: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("API Exception", "Error occurred: ${e.message}")
            }

        }
    }

    /*
    interface ApiService {
        // Asume que ya tienes otros endpoints aquí
        @GET("api/v5/Rutes")
        suspend fun searchRoutes(): Response<List<RutaUsuari>>
    }
    */


    interface ApiService {
        // Asume que ya tienes otros endpoints aquí
        @GET("rutes/")
        suspend fun searchRoutes(
            @Query("query") query: String?,
            @Query("distance") distance: Float?,
            @Query("duration") duration: Int?,
            @Query("nombreZona") nombreZona: String,
        ): Response<List<RutaUsuari>>
    }



}