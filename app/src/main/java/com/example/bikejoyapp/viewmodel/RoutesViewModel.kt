package com.example.bikejoyapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bikejoyapp.api.ApiServiceFactory
import com.example.bikejoyapp.data.Comentario
import com.example.bikejoyapp.data.CommentRequest
import com.example.bikejoyapp.data.LoggedUser
import com.example.bikejoyapp.data.PuntoIntermedio
import com.example.bikejoyapp.data.RatingRequest
import com.example.bikejoyapp.data.RutaUsuari
import com.example.bikejoyapp.data.SharedPrefUtils
import com.example.bikejoyapp.ui.ViewType
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.http.GET
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
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


    private val _routeComments = MutableLiveData<List<Comentario>>()
    val routeComments: LiveData<List<Comentario>> = _routeComments

    private val _currentView = MutableLiveData<ViewType>(ViewType.Details)
    val currentView: LiveData<ViewType> = _currentView

    private val _showDialog = MutableLiveData<Boolean>(false)
    val showDialog: LiveData<Boolean> = _showDialog

    private val _userRating = MutableLiveData<Int>(0)
    val userRating: LiveData<Int> = _userRating

    private val _fixedRating = MutableLiveData<Int>()
    val fixedRating: LiveData<Int> = _fixedRating

    private val _ratingSent = MutableLiveData<Boolean>(false)
    val ratingSent: LiveData<Boolean> = _ratingSent


    fun onDurationChange(value: Float) {
        _durationFilter.value = value
        Log.d("Filters", "Duración cambiada a: $value")
    }

    fun setRatingSent(value: Boolean) {
        _ratingSent.value = value
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

    fun toggleView() {
        _currentView.value = when (_currentView.value) {
            ViewType.Details -> ViewType.Comments
            ViewType.Comments, null -> ViewType.Details
        }
    }

    fun showRatingDialog() {
        _showDialog.value = true
    }

    fun hideRatingDialog() {
        _showDialog.value = false
        _userRating.value = 0
    }
    fun updateUserRating(newRating: Int) {
        if (!_ratingSent.value!!) {
            _userRating.value = newRating
        }
    }


    fun performSearchWithFilters() {
        viewModelScope.launch {
            try {
                val startLocation = startLocationFilter.value ?: "Cualquier zona"
                val response = ApiServiceFactory.apiServiceGson.searchRoutes(null, distanceFilter.value, durationFilter.value?.toInt(), startLocation)
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
                val response = ApiServiceFactory.apiServiceGson.searchRoutes(searchQuery.value, null, null, "Cualquier zona")
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
                val response = ApiServiceFactory.apiServiceGson.getPuntosIntermedios(ruteId)
                if (response.isSuccessful && response.body() != null) {
                    val puntosLatLng = response.body()!!.map { LatLng(it.lat.toDouble(), it.lng.toDouble()) }
                    _puntosIntermedios.postValue(puntosLatLng)


                } else {
                    Log.d("MapDebug", "Error fetching points: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("API Exception", "Error occurred: ${e.message}")
            }
        }
    }

    fun submitUserRating(ruteId: Int, rating: Int) {
        viewModelScope.launch {
            val token = SharedPrefUtils.getToken()
            val user = LoggedUser.getLoggedUser()
            if (token != null && user != null) {
                val ratingRequest = RatingRequest(mark = rating)
                val response = ApiServiceFactory.apiServiceGson.submitRating("Token $token", ruteId, ratingRequest)
                if (response.isSuccessful) {
                    _ratingSent.value = true
                } else {
                    Log.e("API Error", "Failed with response: ${response.errorBody()?.string()}")
                }
            }
        }
    }

    fun addComment(ruteId: Int, comment: String) {
        viewModelScope.launch {
            val token = SharedPrefUtils.getToken()
            val user = LoggedUser.getLoggedUser()
            if (token != null && user != null) {
                val commentRequest = CommentRequest(text = comment)
                val response = ApiServiceFactory.apiServiceGson.addComment("Token $token", ruteId, commentRequest)
                if (response.isSuccessful) {
                    Log.d("API", "Comment added successfully")
                } else {
                    Log.e("API Error", "Failed with response: ${response.errorBody()?.string()}")
                }
            }
        }
    }

    fun getComments(ruteId: Int) {
        viewModelScope.launch {
            try {
                val response = ApiServiceFactory.apiServiceGson.getComments(ruteId)
                if (response.isSuccessful && response.body() != null) {
                    _routeComments.postValue(response.body())
                } else {
                    Log.e("API Error", "Failed with response: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("API Exception", "Error occurred: ${e.message}")
            }
        }
    }

    fun getAverageRating(ruteId: Int) {
        viewModelScope.launch {
            try {
                val response = ApiServiceFactory.apiServiceGson.getAverageRating(ruteId)
                if (response.isSuccessful && response.body() != null) {
                    _fixedRating.postValue(response.body())
                    Log.d("API", "Average rating: ${response.body()}")
                } else {
                    Log.e("API Error", "Failed with response: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("API Exception", "Error occurred: ${e.message}")
            }
        }
    }
}