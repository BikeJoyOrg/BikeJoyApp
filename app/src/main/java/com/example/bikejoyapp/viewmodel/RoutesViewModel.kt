package com.example.bikejoyapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bikejoyapp.data.Comment
import com.example.bikejoyapp.data.PuntoIntermedio
import com.example.bikejoyapp.data.RutaUsuari
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch
import retrofit2.http.GET
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Path
import retrofit2.http.Query


class RoutesViewModel : ViewModel() {
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

    private val _puntosIntermedios = MutableLiveData<List<LatLng>>()
    val puntosIntermedios: LiveData<List<LatLng>> = _puntosIntermedios

    private val _routeComments = MutableLiveData<List<Comment>>()
    val routeComments: LiveData<List<Comment>> = _routeComments

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


    fun performSearchWithFilters() {
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

    fun getPuntosIntermedios(ruteId: Int) {
        viewModelScope.launch {
            try {
                val response = apiService.getPuntosIntermedios(ruteId)
                if (response.isSuccessful && response.body() != null) {
                    Log.d("API RESPONSE", "Puntos Intermedios: ${response.body()}")
                    val puntosLatLng = response.body()!!.map { LatLng(it.lat.toDouble(), it.lng.toDouble()) }
                    Log.d("API", "Puntos recibidos: $puntosLatLng")
                    _puntosIntermedios.postValue(puntosLatLng)
                } else {
                    Log.e("API Error", "Failed with response: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("API Exception", "Error occurred: ${e.message}")
            }
        }
    }

    interface ApiService {
        @GET("rutes/")
        suspend fun searchRoutes(
            @Query("query") query: String?,
            @Query("distance") distance: Float?,
            @Query("duration") duration: Int?,
            @Query("nombreZona") nombreZona: String,
        ): Response<List<RutaUsuari>>


        @GET("puntos-intermedios/{ruteId}/")
        suspend fun getPuntosIntermedios(
            @Path("ruteId") ruteId: Int
        ): Response<List<PuntoIntermedio>>
    }



}