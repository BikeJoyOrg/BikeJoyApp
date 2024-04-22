package com.example.bikejoyapp.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Looper
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import com.example.bikejoyapp.R
import com.example.bikejoyapp.data.EstacioBicing
import com.example.bikejoyapp.data.MyAppRoute
import com.example.bikejoyapp.ui.components.SearchPreviewWidget
import com.example.bikejoyapp.ui.theme.magentaOscuroCrema
import com.example.bikejoyapp.viewmodel.BikeLanesViewModel
import com.example.bikejoyapp.viewmodel.EstacionsViewModel
import com.example.bikejoyapp.viewmodel.MainViewModel
import com.example.bikejoyapp.viewmodel.NavigationViewModel
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.clustering.Clustering
import kotlinx.coroutines.delay


var deviceLocation = mutableStateOf(LatLng(41.3851, 2.1734))
var locationCallback = object : LocationCallback() {
    override fun onLocationResult(p0: LocationResult) {
        p0 ?: return
        for (location in p0.locations) {
            deviceLocation.value = LatLng(location.latitude, location.longitude)
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(MapsComposeExperimentalApi::class)
@SuppressLint("MissingPermission")
@Composable
fun MapScreen(
    stationViewModel: EstacionsViewModel,
    mainViewModel: MainViewModel,
    navigationViewModel: NavigationViewModel,
    bikeLanesViewModel: BikeLanesViewModel
) {
    val context = LocalContext.current
    val searchQuery by navigationViewModel.searchQuery.observeAsState("")

    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    val locationRequest = LocationRequest.create().apply {
        interval = 1000
        fastestInterval = 500
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    val estacions by stationViewModel.estacions.observeAsState(emptyList())
    val selectedPlace by navigationViewModel.selectedPlace.observeAsState()
    val consultarOpcio by navigationViewModel.consultarOpcio.collectAsState()
    val isNavigating by navigationViewModel.isNavigating.collectAsState()
    val PaintSearchFields by navigationViewModel.PaintSearchFields.collectAsState()
    val navigationTime by navigationViewModel.navigationTime.collectAsState()
    val navigationKm by navigationViewModel.navigationKm.collectAsState()
    val ruta by navigationViewModel.ruta.observeAsState()
    val primer_cop by navigationViewModel.primer_cop.observeAsState(true)
    val bikeLanes by bikeLanesViewModel.bikeLanes.observeAsState(emptyList())

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
    val current = LocalContext.current
    val bottomPadding = if (isNavigating) 80.dp else 0.dp
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
                bikeLanes.forEach { bikeLane ->
                    Polyline(bikeLane.latLng, color = Color.Blue, width = 10f)
                }

                // Estacions
                val clickState = remember { mutableStateOf(false) }
                val stationClicked = remember { mutableStateOf<EstacioBicing?>(null) }
                Clustering(
                    items = estacions,
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
                            modifier = Modifier.size(32.dp)
                        )
                    },
                    clusterContent = {
                        Image(
                            painter = painterResource(R.drawable.bikeparking),
                            contentDescription = "bike station marker",
                            modifier = Modifier.size(32.dp)
                        )
                    },
                )
                LaunchedEffect(clickState.value) {
                    if (stationClicked.value == null) return@LaunchedEffect
                    cameraPositionState.animate(
                        CameraUpdateFactory.newLatLngZoom(
                            LatLng(stationClicked.value!!.lat, stationClicked.value!!.lon),
                            32.0f),
                        1000
                    )
                    delay(1000L)
                    val route = MyAppRoute.Station.createRoute(stationClicked.value!!.station_id.toString())
                    mainViewModel.navigateToDynamic(route)
                }
                // Fi estacions

                if (consultarOpcio) {

                    ruta?.let { Polyline(points = it, color = magentaOscuroCrema, width = 15.0f) }

                    selectedPlace?.let { place ->
                        if (primer_cop){
                            val cameraPosition = CameraPosition.Builder()
                                .target(place.latLng)
                                .zoom(15f)
                                .build()
                            cameraPositionState.position = cameraPosition
                            navigationViewModel.primer_cop()
                        }
                        Marker(
                            state = MarkerState(position = place.latLng),
                            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE),
                            onClick = {
                                navigationViewModel.startNavigation()
                                mainViewModel.hideBottomBar()
                                mainViewModel.hideTopBar()
                                true
                            }
                        )

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
                            navigationViewModel.startNavigation()
                            mainViewModel.hideBottomBar()
                            navigationViewModel.stopPaintSearchFields()
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
                        if (consultarOpcio){
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
                            Text(text = "Temps: ${navigationTime}s")
                        }

                        IconButton(onClick = {
                            navigationViewModel.stopNavigation()
                            mainViewModel.showBottomBar()
                            mainViewModel.showTopBar()
                            navigationViewModel.PaintSearchFields()
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
                        navigationViewModel.assignaPuntBusqueda(place,deviceLocation.value)
                        navigationViewModel.stopPaintSearchFields()
                    })
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

fun resizeMapIcons(context: Context, iconId: Int, width: Int, height: Int): BitmapDescriptor {
    val imageBitmap = BitmapFactory.decodeResource(context.resources, iconId)
    val resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false)
    return BitmapDescriptorFactory.fromBitmap(resizedBitmap)
}