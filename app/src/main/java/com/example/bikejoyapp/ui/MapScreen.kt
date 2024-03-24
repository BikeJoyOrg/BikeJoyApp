package com.example.bikejoyapp.ui

import android.annotation.SuppressLint
import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.IconButton
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.bikejoyapp.R
import com.example.bikejoyapp.data.EstacioBicing
import com.example.bikejoyapp.viewmodel.EstacionsViewModel
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.location.*
import android.os.Looper
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import com.example.bikejoyapp.viewmodel.MainViewModel
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


@SuppressLint("MissingPermission")
@Composable
fun MapScreen(
    stationViewModel: EstacionsViewModel,
    mainViewModel: MainViewModel
) {
    val context = LocalContext.current
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
    val selectedStation = remember { mutableStateOf<EstacioBicing?>(null) }


    LaunchedEffect(Unit) {
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
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
    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
            bikeLanes.forEach { bikeLane ->
                Polyline(bikeLane, color = Color.Blue, width = 10f)
            }
            estacions.forEach { station ->
                Marker(
                    state = MarkerState(LatLng(station.lat, station.lon)),
                    onClick = {
                        selectedStation.value = station
                        true
                    },
                    icon = BitmapDescriptorFactory.fromResource(R.mipmap.checkpoint)
                )
            }
            Marker(state = markerState)
            //cameraPositionState.position = CameraPosition.fromLatLngZoom(deviceLocation.value, 18f)

            LaunchedEffect(deviceLocation.value) {
                markerState.position = deviceLocation.value
            }
        }
        // Botones de navegación, colocados en la parte inferior o en cualquier lugar que desees
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = {
                mainViewModel.hideTopBar()
                mainViewModel.hideBottomBar()
            }) {
                Text("Iniciar navegación")
            }
            Spacer(modifier = Modifier.height(8.dp)) // Espacio entre botones
            Button(onClick = {
                mainViewModel.showTopBar()
                mainViewModel.showBottomBar()
            }) {
                Text("Parar navegación")
            }
        }


        selectedStation.value?.let { station ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
            )
            EstacioBicingWidget(
                station,
                onBackClicked = {
                    selectedStation.value = null
                }
            )
        }
    }
}