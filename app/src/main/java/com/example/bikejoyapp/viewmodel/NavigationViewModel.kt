    package com.example.bikejoyapp.viewmodel

    import android.annotation.SuppressLint
    import android.content.Context
    import android.content.pm.PackageManager
    import android.location.Location
    import android.os.Looper
    import android.util.Log
    import android.widget.Toast
    import androidx.compose.runtime.mutableStateOf
    import androidx.core.content.ContextCompat
    import androidx.lifecycle.LiveData
    import androidx.lifecycle.MutableLiveData
    import androidx.lifecycle.ViewModel
    import androidx.lifecycle.ViewModelProvider
    import androidx.lifecycle.viewModelScope
    import com.example.bikejoyapp.data.RouteResponse
    import com.google.android.gms.location.FusedLocationProviderClient
    import com.google.android.gms.location.LocationCallback
    import com.google.android.gms.location.LocationRequest
    import com.google.android.gms.location.LocationResult
    import com.google.android.gms.location.LocationServices
    import com.google.android.gms.maps.model.LatLng
    import com.google.android.gms.maps.model.LatLngBounds
    import com.google.android.libraries.places.api.model.Place
    import com.google.android.libraries.places.api.model.Place.Field
    import com.google.android.libraries.places.api.model.RectangularBounds
    import com.google.android.libraries.places.api.net.FetchPlaceRequest
    import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
    import com.google.android.libraries.places.api.net.PlacesClient
    import com.google.maps.android.SphericalUtil
    import kotlinx.coroutines.CoroutineScope
    import kotlinx.coroutines.Dispatchers
    import kotlinx.coroutines.delay
    import kotlinx.coroutines.flow.MutableStateFlow
    import kotlinx.coroutines.flow.StateFlow
    import kotlinx.coroutines.launch
    import kotlinx.coroutines.tasks.await
    import kotlinx.coroutines.withContext
    import retrofit2.Retrofit
    import retrofit2.converter.gson.GsonConverterFactory

    import com.google.android.gms.location.*


    open class NavigationViewModel(private val placesClient: PlacesClient, private val context: Context) : ViewModel()  {

        class Factory(private val placesClient: PlacesClient,  private val context: Context) : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(NavigationViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return NavigationViewModel(placesClient, context) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }

        private val _searchQuery = MutableLiveData<String>()
        val searchQuery: LiveData<String> = _searchQuery

        private val _searchResults = MutableLiveData<List<Place>>()
        val searchResults: LiveData<List<Place>> = _searchResults

        val _selectedPlace = MutableLiveData<Place?>()
        val selectedPlace: LiveData<Place?> = _selectedPlace


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

        private val _consultarOpcio = MutableStateFlow(false)
        val consultarOpcio: StateFlow<Boolean> = _consultarOpcio

        private val _isNavigating = MutableStateFlow(false)
        val isNavigating: StateFlow<Boolean> = _isNavigating

        private val _navigationTime = MutableStateFlow(0) // Tiempo en segundos
        val navigationTime: StateFlow<Int> = _navigationTime

        private val _navigationKm = MutableStateFlow(0.0)
        val navigationKm: MutableStateFlow<Double> = _navigationKm

        private val _PaintSearchFields = MutableStateFlow(true)
        val PaintSearchFields: StateFlow<Boolean> = _PaintSearchFields

        private val _ruta = MutableLiveData<MutableList<LatLng>>()
        val ruta: LiveData<MutableList<LatLng>> = _ruta

        private val _primer_cop = MutableLiveData<Boolean>()
        val primer_cop: LiveData<Boolean> = _primer_cop

        private val _showRouteResume = MutableLiveData<Boolean>()
        val showRouteResume: LiveData<Boolean> = _showRouteResume
        private val _avis = MutableLiveData<Boolean>()
        val avis: LiveData<Boolean> = _avis

        private val _buscat = MutableLiveData<Boolean>()
        val buscat: LiveData<Boolean> = _buscat
        fun PaintSearchFields() {
            _PaintSearchFields.value = true
        }

        fun stopPaintResultList() {
            _PaintSearchFields.value = false
        }

        private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        private val locationRequest = LocationRequest.create().apply {
            interval = 1000
            fastestInterval = 500
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        private var primer: LatLng? = null
        private var segon: LatLng? = null
        var start = ""
        var finish = ""
        var coordfinish:LatLng? = null
        var stop = false
        var lliure = false
        @SuppressLint("MissingPermission")
        fun startNavigation() {
            _PaintSearchFields.value = false
            _isNavigating.value = true
            _navigationTime.value = 0
            _navigationKm.value = 0.0
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                null
            )
            lliure = coordfinish == null
            stop = false
            viewModelScope.launch {
                while (_isNavigating.value!!) {
                    delay(1000)
                    if (!stop) {
                        _navigationTime.value = (_navigationTime.value ?: 0) + 1
                        calcularDistancia()
                    }
                }
            }
        }

        private val locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                p0 ?: return
                for (location in p0.locations) {
                    if (primer == null) {
                        primer = LatLng(location.latitude, location.longitude)
                    }
                    else if( _isNavigating.value && !lliure && distanceBetween(
                            LatLng(location.latitude, location.longitude),
                            coordfinish!!
                        ) <= 50.0
                    ){
                        Log.d("aris", "he arribat")
                        if (!_avis.value!!) {
                            rutacompleta()
                            stop = true
                        }

                    }
                    else {
                        segon = LatLng(location.latitude, location.longitude)
                    }
                }
            }
        }

        private fun calcularDistancia() {
            if (primer != null && segon != null) {
                val distance = SphericalUtil.computeDistanceBetween(primer, segon)
                _navigationKm.value = (_navigationKm.value ?: 0.0) + distance
                primer = segon
            }
        }
        fun stopNavigation(rutaCompletada: Boolean) {
            Log.d("aris", "final")
            _PaintSearchFields.value = true
            _isNavigating.value = false
            _selectedPlace.value = null
            _ruta.value = mutableListOf()
            _consultarOpcio.value = false
            _searchResults.value = emptyList()
            _searchQuery.value = ""
            _primer_cop.value = true
            stop = false
            coordfinish = null
            _buscat.value = false
            if (rutaCompletada) {
                _showRouteResume.value = false
            }
            else{
                _avis.value = false
            }
            Log.d("aris", _isNavigating.value.toString())
        }

        open fun assignaPuntBusqueda(place: Place, value: LatLng) {
            _selectedPlace.value = place
            _consultarOpcio.value = true
            _searchResults.value = emptyList()
            _buscat.value = true

            start = "${value.longitude},${value.latitude}"
            finish = place.latLng?.longitude.toString() + "," + place.latLng?.latitude.toString()
            coordfinish = place.latLng
            createRute()
        }

        private fun createRute(){
            Log.d("aris", "heloooo2222222")
            CoroutineScope(Dispatchers.IO).launch {
                val call = getRetrofit().create(ApiRetrofit::class.java)
                    .getRoute("5b3ce3597851110001cf6248ffd3a14535d041289931632bc575ecbf", start, finish)
                if(call.isSuccessful){
                    drawRoute(call.body())
                    Log.d("aris", "aviam si va")
                }
                else{
                    Log.d("aris", "malo")
                    Log.d("aris", start)
                    Log.d("aris", finish)
                }
            }
        }
        private fun getRetrofit(): Retrofit {
            return Retrofit.Builder()
                .baseUrl("https://api.openrouteservice.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        private fun drawRoute(routeResponse: RouteResponse?) {
            val aux = mutableListOf<LatLng>()
            routeResponse?.features?.first()?.geometry?.coordinates?.forEach {
                aux.add(LatLng(it[1], it[0]))
            }
            _ruta.postValue(aux)
        }
        fun primer_cop() {
            _primer_cop.value = false
        }

        fun rutacompleta(){
            _showRouteResume.value = true
        }
        fun avis(){
            _avis.value = true
            stop = true
        }
        fun continuar(){
            _avis.value = false
            stop = false
        }
        fun mostrarRuta(puntos: List<LatLng>){
            _ruta.value = puntos.toMutableList()
            coordfinish = puntos.last()
            _consultarOpcio.value = true
        }
    }
