package com.example.bikejoyapp.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
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
import com.example.bikejoyapp.data.MyAppRoute
import com.example.bikejoyapp.ui.components.SearchPreviewWidget
import com.example.bikejoyapp.viewmodel.EstacionsViewModel
import com.example.bikejoyapp.viewmodel.MainViewModel
import com.example.bikejoyapp.viewmodel.NavigationViewModel
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
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
import org.json.JSONObject

fun parseGeoJson(context: Context, resourceId: Int): List<List<LatLng>> {
    val bikeLanes = mutableListOf<List<LatLng>>()
    try {
        val geoJson =
            context.resources.openRawResource(resourceId).bufferedReader().use { it.readText() }
        val jsonObject = JSONObject(geoJson)
        val features = jsonObject.getJSONArray("features")

        for (i in 0 until features.length()) {
            val feature = features.getJSONObject(i)
            val geometry = feature.getJSONObject("geometry")
            val coordinates = geometry.getJSONArray("coordinates")

            val bikeLane = mutableListOf<LatLng>()
            for (j in 0 until coordinates.length()) {
                val coordinate = coordinates.getJSONArray(j)
                val latLng = LatLng(coordinate.getDouble(1), coordinate.getDouble(0))
                bikeLane.add(latLng)
            }

            bikeLanes.add(bikeLane)
        }
    } catch (e: Exception) {
        println("Error al leer o analizar el archivo GeoJSON: ${e.message}")
    }

    return bikeLanes
}


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("MissingPermission")
@Composable
fun MapScreen(
    stationViewModel: EstacionsViewModel,
    mainViewModel: MainViewModel,
    navigationViewModel: NavigationViewModel
) {
    val context = LocalContext.current
    val searchQuery by navigationViewModel.searchQuery.observeAsState("")
    val deviceLocation = remember { mutableStateOf(LatLng(41.390205, 2.154007)) }
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    val locationRequest = LocationRequest.create().apply {
        interval = 1000
        fastestInterval = 500
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }
    val locationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            p0 ?: return
            for (location in p0.locations) {
                deviceLocation.value = LatLng(location.latitude, location.longitude)
            }
        }
    }
    val estacions by stationViewModel.estacions.observeAsState(emptyList())
    val selectedPlace by navigationViewModel.selectedPlace.observeAsState()

    val isNavigating by navigationViewModel.isNavigating.collectAsState()
    val PaintSearchFields by navigationViewModel.PaintSearchFields.collectAsState()
    val navigationTime by navigationViewModel.navigationTime.collectAsState()
    val navigationKm by navigationViewModel.navigationKm.collectAsState()

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

    val markerState = rememberMarkerState()
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(deviceLocation.value, 18f)
    }
    val bikeLanes = parseGeoJson(LocalContext.current, R.raw.bike_lanes)

    val bottomPadding = if (isNavigating) 80.dp else 0.dp

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = bottomPadding),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(isMyLocationEnabled = true, mapType = MapType.NORMAL)
        ) {
            bikeLanes.forEach { bikeLane ->
                Polyline(bikeLane, color = Color.Blue, width = 10f)
            }
            estacions.forEach { station ->
                Marker(
                    state = MarkerState(LatLng(station.lat, station.lon)),
                    onClick = {
                        val route = MyAppRoute.Station.createRoute(station.stationId.toString())
                        mainViewModel.navigateToDynamic(route)
                        true
                    },
                    icon = BitmapDescriptorFactory.fromResource(R.mipmap.checkpoint)
                )
            }
            if (isNavigating) {
                selectedPlace?.let { place ->
                    val cameraPosition = CameraPosition.Builder()
                        .target(place.latLng) // Asume que place tiene una propiedad latLng
                        .zoom(15f) // Ajusta el zoom según tus necesidades
                        .build()
                    cameraPositionState.position = cameraPosition
                    Marker(
                        state = MarkerState(position = place.latLng),
                        icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE) // O usa tu propio ícono
                    )

                    // Actualiza la posición de la cámara aquí si deseas centrar automáticamente al seleccionar
                    LaunchedEffect(place) {
                        cameraPositionState.animate(
                            CameraUpdateFactory.newLatLngZoom(
                                place.latLng,
                                15f
                            )
                        )
                    }
                }
            }

            Marker(state = markerState)
            //cameraPositionState.position = CameraPosition.fromLatLngZoom(deviceLocation.value, 18f)

            LaunchedEffect(deviceLocation.value) {
                markerState.position = deviceLocation.value
            }
        }

        if (PaintSearchFields) {
            Column {
                SearchField(searchQuery = searchQuery,
                    onSearchQueryChanged = { query -> navigationViewModel.onSearchQueryChanged(query) },
                    onPerformSearch = { navigationViewModel.performSearch() }
                )
                SearchResultsList(navigationViewModel, mainViewModel)
            }
        }

        if (!isNavigating) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextButton(
                    onClick = {
                        navigationViewModel.startNavigation()
                        mainViewModel.hideBottomBar()
                        navigationViewModel.stopPaintSearchFields()
                    },
                    // Estiliza el botón con un fondo, elevación, y forma personalizados
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .clip(RoundedCornerShape(50)), // Bordes redondeados para un aspecto más amigable
                ) {
                    // Agrega un icono junto al texto para un aspecto más interactivo
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        modifier = Modifier.size(ButtonDefaults.IconSize)
                    )
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text(
                        text = "Navegació lliure"
                    )
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
            SearchPreviewWidget(place.getName(), place.getAddress(), onClick = {
                navigationViewModel.selectedPlace.value = place
                navigationViewModel.startNavigation()
                mainViewModel.hideBottomBar()
                mainViewModel.hideTopBar()
                navigationViewModel.stopPaintSearchFields()
            })

        }
    }
}



