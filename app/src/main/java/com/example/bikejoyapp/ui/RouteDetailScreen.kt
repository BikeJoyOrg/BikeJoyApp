package com.example.bikejoyapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.bikejoyapp.viewmodel.MainViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.bikejoyapp.R
import com.example.bikejoyapp.data.RutaUsuari
import com.example.bikejoyapp.viewmodel.RoutesViewModel
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberMarkerState


@Composable
fun RouteDetailScreen(routesViewModel: RoutesViewModel, mainViewModel: MainViewModel, route: RutaUsuari) {
    var rating by remember { mutableIntStateOf(route.RuteRating ?: 0) }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(route.PuntIniciLat, route.PuntIniciLong), 12f)
    }

    val puntosIntermedios by routesViewModel.puntosIntermedios.observeAsState(initial = emptyList())


    LaunchedEffect(route.RuteId) {
        routesViewModel.getPuntosIntermedios(route.RuteId ?: 0)
    }
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        item {
            Box {
                IconButton(onClick = { mainViewModel.navigateBack() }, modifier = Modifier.align(Alignment.TopStart)) {
                    Icon(Icons.Filled.Close, contentDescription = "Cerrar")
                }
            }
            Box(modifier = Modifier.height(200.dp).fillMaxWidth()) {
                GoogleMap(
                    cameraPositionState = cameraPositionState,
                    modifier = Modifier.matchParentSize(),
                    properties = MapProperties(isMyLocationEnabled = true, mapType = MapType.NORMAL)
                ) {
                    Marker(
                        state = MarkerState(position = LatLng(route.PuntIniciLat, route.PuntIniciLong))
                    )

                    Polyline(
                        points = puntosIntermedios,
                        color = Color.Blue,
                        width = 5f
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
            route.RuteName?.let { Text(text = it) }
            RatingBikes(rating, onRatingChanged = { newRating ->
                rating = newRating
            })
            Spacer(Modifier.height(16.dp))
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    route.RuteDescription?.let { Text(it) }
                    Spacer(Modifier.height(8.dp))
                }
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
fun RatingBikes(rating: Int, onRatingChanged: (Int) -> Unit) {
    Row {
        for (i in 1..5) {
            val image = if (i <= rating) {
                painterResource(id = R.drawable.bicicleta_filled) // Suponiendo que es el recurso para la bicicleta rellena
            } else {
                painterResource(id = R.drawable.bicicleta_outlined) // Suponiendo que es el recurso para la bicicleta delineada
            }

            IconButton(onClick = { onRatingChanged(i) }) {
                Icon(painter = image, contentDescription = "Bici $i")
            }
        }
    }
}
