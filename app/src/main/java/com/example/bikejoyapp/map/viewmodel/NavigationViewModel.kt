    package com.example.bikejoyapp.map.viewmodel

    import android.annotation.SuppressLint
    import android.content.Context
    import android.location.Location
    import android.util.Log
    import androidx.lifecycle.LiveData
    import androidx.lifecycle.MutableLiveData
    import androidx.lifecycle.ViewModel
    import androidx.lifecycle.ViewModelProvider
    import androidx.lifecycle.viewModelScope
    import com.example.bikejoyapp.api.ApiServiceFactory
    import com.example.bikejoyapp.users.data.LoggedUser
    import com.example.bikejoyapp.routes.data.PuntsVisitats
    import com.example.bikejoyapp.routes.data.RouteResponse
    import com.example.bikejoyapp.routes.data.RutaCompletada
    import com.example.bikejoyapp.utils.SharedPrefUtils
    import com.example.bikejoyapp.users.data.User
    import com.example.bikejoyapp.profile.viewmodel.MascotesViewModel
    import com.example.bikejoyapp.users.viewmodel.UserViewModel
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

    import kotlinx.serialization.json.Json


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

        private val _consultarOpcio = MutableLiveData<Boolean>(false)
        val consultarOpcio: LiveData<Boolean> = _consultarOpcio

        private val _isNavigating = MutableStateFlow(false)
        val isNavigating: StateFlow<Boolean> = _isNavigating

        private val _navigationTime = MutableStateFlow(0) // Tiempo en segundos
        val navigationTime: StateFlow<Int> = _navigationTime

        private val _navigationM = MutableStateFlow(0.0)
        val navigationKm: MutableStateFlow<Double> = _navigationM

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

        private val _puntIntermedi = MutableLiveData<Int>()
        val puntIntermedi: LiveData<Int> = _puntIntermedi
        private val _puntIntermedi2 = MutableLiveData<Int>()
        val puntIntermedi2: LiveData<Int> = _puntIntermedi2

        private val _desvio = MutableLiveData<Boolean>()
        val desvio: LiveData<Boolean> = _desvio

        private val puntsVisitatslliure = mutableListOf<LatLng>()
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
            _navigationM.value = 0.0
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                null
            )
            _avis.value = false
            if(_consultarOpcio.value == true && _ruta.value?.size!! > 1){
                _puntIntermedi.value = 1
                _puntIntermedi2.value = 2
            }
            lliure = coordfinish == null
            stop = false
            viewModelScope.launch {
                while (_isNavigating.value) {
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
                    val pos = LatLng(location.latitude, location.longitude)
                    if (primer == null) {
                        primer = pos
                    }
                    else if( _isNavigating.value && !lliure && distanceBetween(pos,coordfinish!!) <= 50.0){
                        if (!_avis.value!!) {
                            rutacompleta()
                            stop = true
                        }
                    }
                    else {
                        if (_isNavigating.value && !lliure && seguentpunt(pos)){

                            if (_puntIntermedi.value!! < _ruta.value!!.size - 2) {
                                _puntIntermedi.value = _puntIntermedi.value!! + 1
                            }
                            if (_puntIntermedi2.value!! < _ruta.value!!.size - 2) {
                                _puntIntermedi2.value = _puntIntermedi2.value!! + 1
                            }
                        }
                        else if(_isNavigating.value && !lliure && isdesvio(pos)){
                            _desvio.value = true
                            stop = true
                        }
                        if (lliure){
                            if (lliureStartTime == 0L) {
                                lliureStartTime = System.currentTimeMillis()
                            } else if (System.currentTimeMillis() - lliureStartTime >= 15000) {
                                // Han pasado 15 segundos desde que el usuario se desvió de la ruta
                                puntsVisitatslliure.add(LatLng(location.latitude, location.longitude))
                                lliureStartTime = 0L
                            }
                        }
                        segon = LatLng(location.latitude, location.longitude)
                    }
                }
            }
        }
        private var lliureStartTime: Long = 0
        private var desvioStartTime: Long = 0

        private fun isdesvio(pos: LatLng): Boolean {
            val dist = _ruta.value?.let { distanceBetween(pos, it[_puntIntermedi.value!!]) }
            val dist2 = _ruta.value?.let { distanceBetween(pos, it[_puntIntermedi.value!! -1]) }
            if (dist != null) {
                if (dist > 50.0 && dist2!! > 50.0) {
                    if (desvioStartTime == 0L) {
                        desvioStartTime = System.currentTimeMillis()
                    } else if (System.currentTimeMillis() - desvioStartTime >= 15000) {
                        // Han pasado 15 segundos desde que el usuario se desvió de la ruta
                        desvioStartTime = 0L
                        return true
                    }

                } else {
                    // El usuario ha vuelto a la ruta, reseteamos el contador
                    desvioStartTime = 0L
                }
            }
            return false
        }

        private fun seguentpunt(pos: LatLng): Boolean {
            val dist = _ruta.value?.let { distanceBetween(pos, it[_puntIntermedi.value!!]) }
            if (dist != null) {
                return dist <= 25.0
            }
            return false
        }

        private fun calcularDistancia() {
            if (primer != null && segon != null) {
                val distance = SphericalUtil.computeDistanceBetween(primer, segon)
                _navigationM.value = (_navigationM.value ?: 0.0) + distance
                primer = segon
            }
        }
        suspend fun stopNavigation(rutaCompletada: Boolean, mascotesViewModel: MascotesViewModel, userViewModel: UserViewModel) {
            Log.d("aris", "final")
            if (rutaCompletada) {
                Log.d("aris", "ruta completada")
                _showRouteResume.value = false
                usuariRutaCompletada(true)
                coordfinish = null
                userViewModel.addCompletedRoute(ruta_id!!)
            }
            else{
                _avis.value = false
            }
            _PaintSearchFields.value = true
            _isNavigating.value = false
            _selectedPlace.value = null

            _searchResults.value = emptyList()
            _searchQuery.value = ""
            _primer_cop.value = true
            stop = false
            coordfinish = null
            _buscat.value = false
            _desvio.value = false
            _puntIntermedi.value = 0
            actualitzarestadistiques(mascotesViewModel, userViewModel)
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
                val call = ApiServiceFactory.apiServiceOpenRoute
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
            if(_desvio.value!!){
                _desvio.value = false
            }
        }
        private var ruta_id: Int? = null
        fun mostrarRuta(puntos: List<LatLng>, rute_id: Int){
            ruta_id = rute_id
            _ruta.value = puntos.toMutableList()
            coordfinish = puntos.last()
            _consultarOpcio.value = true
            _puntIntermedi.value = 1
            if (_ruta.value!!.size > 2){
                _puntIntermedi2.value = 2
            }
        }
        private suspend fun actualitzarestadistiques(mascotesViewModel: MascotesViewModel, userViewModel: UserViewModel) {
            Log.d("aris", "entroactualitzarestadistiques")
            try{
                val token = SharedPrefUtils.getToken()

                val user = LoggedUser.getLoggedUser()
                val potenciadors = mascotesViewModel.getBonusMascota()
                val bonus = potenciadors[0]
                if (user != null){
                    val coins = bonus*(_navigationM.value.toInt()/10)
                    val distance = _navigationM.value.toInt()
                    val xp = _navigationM.value.toInt()
                    val stats = User(user.username, coins.toInt(),distance,xp,distance,distance,distance,0,0,0,0)
                    val response = ApiServiceFactory.apiServiceSerializer.updateStats("Token $token", stats)
                    if (response.isSuccessful) {
                        Log.d("aris", "stats actualitzades")
                    } else {
                        Log.d("aris", "stats no actualitzades")
                    }
                    userViewModel.getProfile(token!!)
                }

            }catch (e: Exception){
                Log.d("aris", "error" + e.toString())
            }
        }

        private suspend fun usuariRutaCompletada(b: Boolean) {
            Log.d("aris", "entrousuari rutacompletada")
            Log.d("aris", ruta_id.toString())
            val token = SharedPrefUtils.getToken()
            try{
                val usaurairutacompletada =
                    ruta_id?.let { RutaCompletada(null, it, null, _navigationTime.value/60) }
                Log.d("aris", "Token $token")
                val response = ApiServiceFactory.apiServiceSerializer.completedRoute("Token $token", ruta_id!!, usaurairutacompletada!!)
                Log.d("aris", "hola2")
                if (response.isSuccessful) {
                    Log.d("aris", "ruta completada")
                } else {
                    Log.d("aris", "Error al guardar la ruta " + response.body().toString())
                    Log.d("aris", response.code().toString())
                    response.errorBody()?.let {
                        Log.d("aris","Error body: ${it.string()}")
                    }
                }
            }catch (e: Exception){
                Log.d("aris", "error" + e.toString())
            }
            Log.d("aris", "surtusuari rutacompletada")
            CoroutineScope(Dispatchers.IO).launch {
                try{
                    Log.d("aris", "entro a guardar putns visitats")
                    if(_consultarOpcio.value == true){
                        Log.d("aris", "entro a guardar putns visitats 2")
                        for (punt in _ruta.value!!){
                            Log.d("aris", "entro a guardar putns visitats 3")
                            val puntsVisitats = PuntsVisitats("", punt.latitude, punt.longitude)
                            val response = ApiServiceFactory.apiServiceSerializer.visitedPoint("Token $token", puntsVisitats)
                            if (response.isSuccessful) {
                                Log.d("aris", "punt afegit")
                            } else {
                                Log.d("aris", "punt no afegit")
                            }
                        }
                        _ruta.postValue(mutableListOf())
                        _consultarOpcio.postValue(false)
                    }
                    else{
                        for (punt in puntsVisitatslliure){
                            Log.d("aris", "entro a guardar putns visitats 3")
                            val puntsVisitats = PuntsVisitats("", punt.latitude, punt.longitude)
                            val response = ApiServiceFactory.apiServiceSerializer.visitedPoint("Token $token", puntsVisitats)
                            if (response.isSuccessful) {
                                Log.d("aris", "punt afegit")
                            } else {
                                Log.d("aris", "punt no afegit")
                            }
                        }
                    }
                }catch (e: Exception){
                    Log.d("aris", "error" + e.toString())
                }
            }
        }

        private val json = Json {
            ignoreUnknownKeys = true
        }

    }
