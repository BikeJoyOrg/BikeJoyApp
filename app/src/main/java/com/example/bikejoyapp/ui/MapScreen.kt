package com.example.bikejoyapp.ui

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.bikejoyapp.R
import com.example.bikejoyapp.viewmodel.EstacionsViewModel
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.location.*
import android.os.Looper
import androidx.compose.material.icons.Icons
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.ui.res.painterResource
import com.example.bikejoyapp.data.MyAppRoute
import com.example.bikejoyapp.viewmodel.MainViewModel
import com.example.bikejoyapp.viewmodel.NavigationViewModel


@SuppressLint("MissingPermission")
@Composable
fun MapScreen(
    stationViewModel: EstacionsViewModel,
    mainViewModel: MainViewModel,
    navigationViewModel: NavigationViewModel
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

    val isNavigating by navigationViewModel.isNavigating.collectAsState()
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

    val bottomPadding = if (isNavigating) 80.dp else 0.dp

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize().padding(bottom = bottomPadding),
            cameraPositionState = cameraPositionState
        ) {
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
            Marker(state = markerState)
            //cameraPositionState.position = CameraPosition.fromLatLngZoom(deviceLocation.value, 18f)

            LaunchedEffect(deviceLocation.value) {
                markerState.position = deviceLocation.value
            }
        }

        if (!isNavigating) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(onClick = {
                    navigationViewModel.startNavigation()
                    mainViewModel.hideBottomBar()
                }) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_play_circle_24),
                        contentDescription = "Play",
                        modifier = Modifier.size(48.dp)
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
