package com.example.bikejoyapp.map.ui

import android.annotation.SuppressLint
import android.os.Build
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.bikejoyapp.R
import com.example.bikejoyapp.map.data.BikeLane
import com.example.bikejoyapp.map.data.EstacioBicing
import com.example.bikejoyapp.utils.MyAppRoute
import com.example.bikejoyapp.theme.magentaOscuroCrema
import com.example.bikejoyapp.map.viewmodel.BikeLanesViewModel
import com.example.bikejoyapp.map.viewmodel.EstacionsViewModel
import com.example.bikejoyapp.utils.MainViewModel
import com.example.bikejoyapp.profile.viewmodel.MascotesViewModel
import com.example.bikejoyapp.map.viewmodel.NavigationViewModel
import com.example.bikejoyapp.routes.ui.TempsDistancia_vertical
import com.example.bikejoyapp.users.viewmodel.UserViewModel
import com.example.bikejoyapp.utils.SharedPrefUtils
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.clustering.Clustering
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.sqrt


var deviceLocation = mutableStateOf(LatLng(41.3851, 2.1734))
var locationCallback = object : LocationCallback() {
    override fun onLocationResult(p0: LocationResult) {
        p0 ?: return
        for (location in p0.locations) {
            deviceLocation.value = LatLng(location.latitude, location.longitude)
        }
    }
}


@SuppressLint("MissingPermission")
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(MapsComposeExperimentalApi::class)
@Composable
fun MapScreen(
    stationViewModel: EstacionsViewModel,
    mainViewModel: MainViewModel,
    navigationViewModel: NavigationViewModel,
    bikeLanesViewModel: BikeLanesViewModel,
    mascotesViewModel: MascotesViewModel,
    userViewModel: UserViewModel
) {
    val context = LocalContext.current
    val searchQuery by navigationViewModel.searchQuery.observeAsState("")

    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    val locationRequest = LocationRequest.create().apply {
        interval = 1000
        fastestInterval = 500
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }



    val selectedPlace by navigationViewModel.selectedPlace.observeAsState()
    val consultarOpcio by navigationViewModel.consultarOpcio.observeAsState()
    val isNavigating by navigationViewModel.isNavigating.collectAsState()
    val PaintSearchFields by navigationViewModel.PaintSearchFields.collectAsState()
    val navigationTime by navigationViewModel.navigationTime.collectAsState()
    val navigationKm by navigationViewModel.navigationKm.collectAsState()
    val ruta by navigationViewModel.ruta.observeAsState()
    val primer_cop by navigationViewModel.primer_cop.observeAsState(true)
    val showRouteResume by navigationViewModel.showRouteResume.observeAsState(false)
    val avis by navigationViewModel.avis.observeAsState(false)
    val buscat by navigationViewModel.buscat.observeAsState(false)
    val puntIntermedi by navigationViewModel.puntIntermedi.observeAsState()
    val desvio by navigationViewModel.desvio.observeAsState(false)

    LaunchedEffect(Unit) {
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    DisposableEffect(Unit) {
        onDispose {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(deviceLocation.value, 18f)
    }
    val bottomPadding = if (isNavigating) 80.dp else 0.dp
    if (avis){
        Dialog_avis(navigationKm, navigationTime,navigationViewModel, mainViewModel, mascotesViewModel,userViewModel)
    }
    if (showRouteResume){
        Dialog_Resume(navigationKm, navigationTime,navigationViewModel, mainViewModel, mascotesViewModel,userViewModel)
    }
    if(desvio){
        Dialog_desvio(navigationViewModel, mainViewModel,mascotesViewModel,userViewModel)
    }

    val clickState = remember { mutableStateOf(false) }
    val stationClicked = remember { mutableStateOf<EstacioBicing?>(null) }
    LaunchedEffect(clickState.value) {
        if (stationClicked.value == null) return@LaunchedEffect
        cameraPositionState.animate(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(stationClicked.value!!.lat, stationClicked.value!!.lon),
                24.0f),
            1000
        )
        delay(1000L)
        val route = MyAppRoute.Station.createRoute(stationClicked.value!!.station_id.toString())
        mainViewModel.navigateToDynamic(route)
    }

    val estacions by stationViewModel.estacions.observeAsState(emptyList())
    val bikeLanes by bikeLanesViewModel.bikeLanes.observeAsState(emptyList())
    val visibleClusters = remember { mutableStateOf(emptyList<EstacioBicing>()) }
    val visibleLanes = remember { mutableStateOf(emptyList<BikeLane>()) }
    val chargedClusters = remember { mutableStateOf(emptyList<EstacioBicing>()) }
    val chargedLanes = remember { mutableStateOf(emptyList<BikeLane>()) }
    val lastCameraPosition = remember { mutableStateOf<LatLng?>(null) }
    val lastZoomLevel = remember { mutableStateOf<Float?>(null) }
    LaunchedEffect(cameraPositionState.position) {
        val newCameraPosition = LatLng(cameraPositionState.position.target.latitude,
            cameraPositionState.position.target.longitude)
        val newZoomLevel = cameraPositionState.position.zoom
        if (lastCameraPosition.value == null ||
            distanceBetween(lastCameraPosition.value!!, newCameraPosition) > 0.001 ||
            abs(newZoomLevel - (lastZoomLevel.value ?: 0f)) > 0.8f) {

            val visibleRegion = cameraPositionState.projection?.visibleRegion
            val latLngBounds = visibleRegion?.latLngBounds

            // Calcular las coordenadas que están a un 20% de distancia de los bordes de la región visible
            val latDiff = (latLngBounds?.northeast?.latitude ?: 0.0) - (latLngBounds?.southwest?.latitude ?: 0.0)
            val lonDiff = (latLngBounds?.northeast?.longitude ?: 0.0) - (latLngBounds?.southwest?.longitude ?: 0.0)
            val extraLat = latDiff * 0.2
            val extraLon = lonDiff * 0.2
            val northeast = LatLng((latLngBounds?.northeast?.latitude ?: 0.0) + extraLat, (latLngBounds?.northeast?.longitude ?: 0.0) + extraLon)
            val southwest = LatLng((latLngBounds?.southwest?.latitude ?: 0.0) - extraLat, (latLngBounds?.southwest?.longitude ?: 0.0) - extraLon)

            // Expandir los límites para incluir las coordenadas adicionales
            val expandedBounds = latLngBounds?.including(northeast)?.including(southwest)


            visibleClusters.value = estacions.filter { expandedBounds?.contains(LatLng(it.lat, it.lon)) == true }
            visibleLanes.value = bikeLanes.filter { bikeLane ->
                bikeLane.latLng.any { latLng -> expandedBounds?.contains(latLng) == true }
            }
            chargedClusters.value = (chargedClusters.value + visibleClusters.value).distinct()
            chargedLanes.value = (chargedLanes.value + visibleLanes.value).distinct()

            lastCameraPosition.value = newCameraPosition
            lastZoomLevel.value = newZoomLevel
        }
    }


    Column {
        Row (
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            if (PaintSearchFields) {
                Column {
                    SearchField(searchQuery = searchQuery,
                        onSearchQueryChanged = { query -> navigationViewModel.onSearchQueryChanged(query) },
                        onPerformSearch = { navigationViewModel.performSearch() }
                    )
                    SearchResultsList(navigationViewModel, mainViewModel)
                }
            }
        }
        Box(modifier = Modifier.fillMaxSize()) {
            GoogleMap(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = bottomPadding),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(isMyLocationEnabled = true, mapType = MapType.NORMAL)
            ) {
                chargedLanes.value.forEach { bikeLane ->
                    Polyline(bikeLane.latLng, color = Color.Blue, width = 10f)
                }

                // Estacions
                Clustering(
                    items = chargedClusters.value,
                    onClusterClick = {
                        cameraPositionState.move(
                            update = CameraUpdateFactory.zoomIn()
                        )
                        false },
                    onClusterItemClick = { station ->
                        clickState.value = true
                        stationClicked.value = station
                        true
                    },
                    clusterItemContent = {
                        Image(
                            painter = painterResource(R.drawable.bikeparking),
                            contentDescription = "bike station marker",
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    clusterContent = {
                        Image(
                            painter = painterResource(R.drawable.bikeparkingdark),
                            contentDescription = "bike station marker",
                            modifier = Modifier.size(24.dp),
                        )
                    },
                )
                // Fi estacions

                if (consultarOpcio == true) {

                    ruta?.let { Polyline(points = it, color = magentaOscuroCrema, width = 15.0f) }

                    if (buscat) {
                        selectedPlace?.let { place ->
                            if (primer_cop) {
                                val cameraPosition = CameraPosition.Builder()
                                    .target(place.latLng)
                                    .zoom(15f)
                                    .build()
                                cameraPositionState.position = cameraPosition
                                navigationViewModel.primer_cop()
                            }
                            Marker(
                                state = MarkerState(position = place.latLng),
                                icon = BitmapDescriptorFactory.fromResource(R.mipmap.bandera_start_escala),
                                onClick = {
                                    navigationViewModel.startNavigation()
                                    mainViewModel.hideBottomBar()
                                    mainViewModel.hideTopBar()
                                    true
                                }
                            )

                        }
                    } else {
                        if (primer_cop) {
                            Log.d("aris", "Primer cop")
                            val cameraPosition = CameraPosition.Builder()
                                .target(ruta?.first() ?: deviceLocation.value)
                                .zoom(15f)
                                .build()
                            cameraPositionState.position = cameraPosition
                            navigationViewModel.primer_cop()
                        }
                        Marker(
                            state = MarkerState(position = ruta?.first() ?: deviceLocation.value),
                            icon = BitmapDescriptorFactory.fromResource(R.mipmap.bandera_inicit_escala)
                        )
                        Marker(
                            state = MarkerState(position = ruta?.last() ?: deviceLocation.value),
                            icon = BitmapDescriptorFactory.fromResource(R.mipmap.bandera_start_escala),
                        )
                        Circle(center = ruta?.last() ?: deviceLocation.value, radius = 50.0,
                            strokeColor = Color(0xFF000000), strokeWidth = 0f, fillColor = magentaOscuroCrema.copy(alpha = 0.25f))
                        Marker(
                            state = MarkerState(position = ruta?.get(puntIntermedi!!) ?: deviceLocation.value),
                            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)
                        )
                        Circle(center = ruta?.get(puntIntermedi!!) ?: deviceLocation.value, radius = 25.0,
                            strokeColor = Color(0xFF000000), strokeWidth = 0f, fillColor = magentaOscuroCrema.copy(alpha = 0.25f))
                    }
                }
            }

            if (!isNavigating) {
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = {
                            val token = SharedPrefUtils.getToken()
                            if (token == null) {
                                mainViewModel.navigateTo(MyAppRoute.Login)
                                mainViewModel.hideBottomBar()
                            }
                            else{
                                navigationViewModel.startNavigation()
                                mainViewModel.hideBottomBar()
                            }
                        },
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .clip(RoundedCornerShape(50)),
                    ) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            modifier = Modifier.size(ButtonDefaults.IconSize)
                        )
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        if (consultarOpcio == true){
                            Text(
                                text = "Iniciar navegació",
                            )
                        }
                        else {
                            Text(
                                text = "Navegació lliure"
                            )
                        }
                    }
                }
            }
            else {
                BottomAppBar(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                painter = painterResource(R.drawable.baseline_access_time_24),
                                contentDescription = "Tiempo de navegación"
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            if(navigationTime > 60){
                                val minuts = navigationTime / 60
                                val segons = navigationTime % 60
                                Text(text = "Temps: ${minuts}m ${segons}s")
                            }
                            else
                                Text(text = "Temps: ${navigationTime}s")
                        }

                        IconButton(onClick = {
                            navigationViewModel.avis()
                        }) {
                            Icon(
                                painter = painterResource(R.drawable.baseline_stop_circle_24),
                                contentDescription = "Stop",
                                modifier = Modifier.size(48.dp)
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                painter = painterResource(R.drawable.baseline_directions_bike_24),
                                contentDescription = "Kilómetros"
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Km: ${"%.2f".format(navigationKm)} m")
                        }
                        LaunchedEffect(deviceLocation.value) {
                            cameraPositionState.animate(
                                CameraUpdateFactory.newLatLngZoom(
                                    deviceLocation.value,
                                    17f
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchField(
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    onPerformSearch: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    TextField(
        value = searchQuery,
        onValueChange = onSearchQueryChanged,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        placeholder = { Text("Search", color = Color(0xFF000000)) },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(onSearch = {
            onPerformSearch()
            keyboardController?.hide()
        }),
        singleLine = true,
        maxLines = 1,
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color(0xFFE0E0E0),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

@Composable
fun SearchResultsList(navigationViewModel: NavigationViewModel, mainViewModel: MainViewModel) {
    val results by navigationViewModel.searchResults.observeAsState(initial = emptyList())
    if (results.isEmpty()) {
        return
    }
    LazyColumn {
        items(results) { place ->
            place.name?.let {
                place.address?.let { it1 ->
                    SearchPreviewWidget(it, it1, onClick = {
                        navigationViewModel.assignaPuntBusqueda(place, deviceLocation.value)
                    })
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun Dialog_Resume(distanciaRuta: Double, tempsRuta: Int, navigationViewModel: NavigationViewModel, mainViewModel: MainViewModel, mascotesViewModel: MascotesViewModel, userViewModel: UserViewModel){
    Dialog(onDismissRequest = { /*TODO*/ }) {
        Card (
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            shape = RoundedCornerShape(16.dp)
        ){
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ){
                Row(     modifier = Modifier
                    .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,) {
                    Icon(
                        painter = painterResource(R.drawable.celebration_24),
                        contentDescription = "Celebarcion" )
                    Text("¡Ruta completada!",
                        fontSize = 20.sp
                    )
                    Icon(
                        painter = painterResource(R.drawable.celebration_24),
                        contentDescription = "Celebarcion" )
                }
                Spacer(modifier = Modifier.height(16.dp))
                TempsDistancia_vertical(distanciaRuta = distanciaRuta, tempsRuta = tempsRuta/60)
                Row (     modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                    horizontalArrangement = Arrangement.Center,){
                    TextButton(onClick = {
                        CoroutineScope(Dispatchers.Main).launch {
                            navigationViewModel.stopNavigation(true,mascotesViewModel, userViewModel)
                            mainViewModel.showBottomBar()
                        }
                    }) {
                        Text("Acceptar")
                    }
                }
            }
        }
    }
}
@Composable
fun Dialog_avis(distanciaRuta: Double, tempsRuta: Int, navigationViewModel: NavigationViewModel, mainViewModel: MainViewModel, mascotesViewModel: MascotesViewModel, userViewModel: UserViewModel){
    Dialog(onDismissRequest = { /*TODO*/ }) {
        Card (
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            shape = RoundedCornerShape(16.dp)
        ){
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ){
                Row(     modifier = Modifier
                    .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,) {
                    Text("Segure que vols finalitzar?",
                        fontSize = 20.sp
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row (     modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                    horizontalArrangement = Arrangement.Center,){
                    TextButton(onClick = {
                        CoroutineScope(Dispatchers.Main).launch {
                            navigationViewModel.stopNavigation(false,mascotesViewModel, userViewModel)
                            mainViewModel.showBottomBar()
                        }
                    }) {
                        Text("Finalitzar")
                    }
                    TextButton(onClick = { navigationViewModel.continuar() }) {
                        Text("Continuar ruta")
                    }

                }
            }
        }
    }
}
@Composable
fun Dialog_desvio(navigationViewModel: NavigationViewModel, mainViewModel: MainViewModel, mascotesViewModel: MascotesViewModel, userViewModel: UserViewModel){
    Dialog(onDismissRequest = { /*TODO*/ }) {
        Card (
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            shape = RoundedCornerShape(16.dp)
        ){
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ){
                Row(     modifier = Modifier
                    .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,) {
                    Text("'No estas seguint la ruta, si us plau torna a la ruta marcada'",
                        fontSize = 20.sp
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row (     modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                    horizontalArrangement = Arrangement.Center,){
                    TextButton(onClick = {
                        CoroutineScope(Dispatchers.Main).launch {
                            navigationViewModel.stopNavigation(false,mascotesViewModel, userViewModel)
                            mainViewModel.showBottomBar()
                        }
                    }) {
                        Text("Finalitzar ruta")
                    }
                    TextButton(onClick = { navigationViewModel.continuar() }) {
                        Text("Continuar ruta")
                    }

                }
            }
        }
    }
}

fun distanceBetween(point1: LatLng, point2: LatLng): Double {
    val latDiff = point2.latitude - point1.latitude
    val lonDiff = point2.longitude - point1.longitude
    return sqrt(latDiff * latDiff + lonDiff * lonDiff)
}