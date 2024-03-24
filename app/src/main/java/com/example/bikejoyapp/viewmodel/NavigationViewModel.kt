    package com.example.bikejoyapp.viewmodel

    import android.location.Location
    import android.util.Log
    import androidx.lifecycle.LiveData
    import androidx.lifecycle.MutableLiveData
    import androidx.lifecycle.ViewModel
    import androidx.lifecycle.ViewModelProvider
    import androidx.lifecycle.viewModelScope
    import com.google.android.gms.maps.model.LatLng
    import com.google.android.gms.maps.model.LatLngBounds
    import com.google.android.libraries.places.api.model.Place
    import com.google.android.libraries.places.api.model.Place.Field
    import com.google.android.libraries.places.api.model.RectangularBounds
    import com.google.android.libraries.places.api.net.FetchPlaceRequest
    import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
    import com.google.android.libraries.places.api.net.PlacesClient
    import com.google.maps.android.SphericalUtil
    import kotlinx.coroutines.Dispatchers
    import kotlinx.coroutines.delay
    import kotlinx.coroutines.flow.MutableStateFlow
    import kotlinx.coroutines.flow.StateFlow
    import kotlinx.coroutines.launch
    import kotlinx.coroutines.tasks.await
    import kotlinx.coroutines.withContext


    class NavigationViewModel(private val placesClient: PlacesClient) : ViewModel()  {

        class Factory(private val placesClient: PlacesClient) : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(NavigationViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return NavigationViewModel(placesClient) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }

        private val _searchQuery = MutableLiveData<String>()
        val searchQuery: LiveData<String> = _searchQuery

        private val _searchResults = MutableLiveData<List<Place>>()
        val searchResults: LiveData<List<Place>> = _searchResults

        val selectedPlace = MutableLiveData<Place?>()

        fun onSearchQueryChanged(query: String) {
            _searchQuery.value = query
        }

        fun performSearch() {
            val barcelonaCenter = LatLng(41.3851, 2.1734)
            val radiusInMeters = 10000 // 10 km radius

            val locationBias = RectangularBounds.newInstance(
                toBounds(barcelonaCenter, radiusInMeters.toDouble())
            )

            val query = _searchQuery.value ?: return
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val request = FindAutocompletePredictionsRequest.builder()
                        .setQuery(query)
                        .setLocationBias(locationBias)
                        .build()

                    val response = placesClient.findAutocompletePredictions(request).await()
                    val placeDetailsList = mutableListOf<Place>()

                    response.autocompletePredictions.forEach { prediction ->
                        val placeFields = listOf(Field.ID, Field.NAME, Field.ADDRESS, Field.LAT_LNG)
                        val placeRequest = FetchPlaceRequest.newInstance(prediction.placeId, placeFields)

                        try {
                            val fetchResponse = placesClient.fetchPlace(placeRequest).await()
                            val place = fetchResponse.place
                            placeDetailsList.add(place)
                        } catch (e: Exception) {
                            Log.e("NavigationViewModel", "Error fetching place details", e)
                        }
                    }

                    withContext(Dispatchers.Main) {
                        _searchResults.value = placeDetailsList.filter { place ->
                            // Filtrar adicionalmente si es necesario verificar que el lugar está dentro del radio deseado
                            place.latLng?.let { location ->
                                distanceBetween(barcelonaCenter, location) <= radiusInMeters
                            } ?: true
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        // Función para calcular el área rectangular alrededor de un punto central con un radio dado
        private fun toBounds(center: LatLng, radiusInMeters: Double): LatLngBounds {
            val distanceFromCenterToCorner = radiusInMeters * Math.sqrt(2.0)
            val southwestCorner = SphericalUtil.computeOffset(center, distanceFromCenterToCorner, 225.0)
            val northeastCorner = SphericalUtil.computeOffset(center, distanceFromCenterToCorner, 45.0)
            return LatLngBounds(southwestCorner, northeastCorner)
        }

        // Función para calcular la distancia entre dos puntos LatLng
        private fun distanceBetween(start: LatLng, end: LatLng): Double {
            val result = FloatArray(1)
            Location.distanceBetween(start.latitude, start.longitude, end.latitude, end.longitude, result)
            return result[0].toDouble()
        }


        private val _isNavigating = MutableStateFlow(false)
        val isNavigating: StateFlow<Boolean> = _isNavigating

        private val _navigationTime = MutableStateFlow(0) // Tiempo en segundos
        val navigationTime: StateFlow<Int> = _navigationTime

        private val _navigationKm = MutableStateFlow(0.0)
        val navigationKm: MutableStateFlow<Double> = _navigationKm

        private val _PaintSearchFields = MutableStateFlow(true)
        val PaintSearchFields: StateFlow<Boolean> = _PaintSearchFields

        fun PaintSearchFields() {
            _PaintSearchFields.value = true
        }

        fun stopPaintSearchFields() {
            _PaintSearchFields.value = false
        }

        fun startNavigation() {
            _isNavigating.value = true
            _navigationTime.value = 0
            viewModelScope.launch {
                while (_isNavigating.value) {
                    delay(1000) //
                    _navigationTime.value += 1
                }
            }
            _searchResults.value = emptyList()
            _searchQuery.value = ""
        }
        fun stopNavigation() {
            _isNavigating.value = false
            selectedPlace.value = null
        }
    }
