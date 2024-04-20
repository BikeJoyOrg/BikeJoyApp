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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
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



val rutaUsuariPreview = RutaUsuari(
    RuteId = 1,
    RuteName = "Ruta por Montjuïc",
    RuteDescription = "Esta es una ruta de prueba",
    RuteDistance = 10.0,
    RuteTime = 60,
    RuteRating = 3,
    PuntIniciLat = 41.3851,
    PuntIniciLong = 2.1734
)


@Preview(showBackground = true)
@Composable
fun PreviewRouteDetailScreen() {
    // Crear instancias de ViewModel vacías para el preview
    val routesViewModel = RoutesViewModel()
    val mainViewModel = MainViewModel()

    RouteDetailScreen(routesViewModel, mainViewModel, rutaUsuariPreview)
}


@Composable
fun RouteDetailScreen(routesViewModel: RoutesViewModel, mainViewModel: MainViewModel, route: RutaUsuari) {
    var userRating by remember { mutableIntStateOf(0) }
    val fixedRating = route.RuteRating

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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = route.RuteName ?: "Nombre de ruta",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .weight(1f) // Ocupa la mayor parte del espacio disponible, pero permite que el ícono se ajuste al lado
                )
                RatingBikes(
                    rating = fixedRating,
                    enabled = false,
                )
            }
            Spacer(Modifier.height(16.dp))
            Card(modifier = Modifier.fillMaxWidth()) {
                Column {
                    Text(text = route.RuteDescription ?: "Descripción no disponible")
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "Tu valoración:")
                    RatingBikes(rating = userRating, enabled = true, onRatingChanged = { newRating ->
                        userRating = newRating
                        // Aquí podrías llamar a una función de tu ViewModel para enviar la valoración del usuario al backend
                        //routesViewModel.submitUserRating(routeId = route.RuteId, rating = newRating)
                    })
                }
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}
@Composable
fun RatingBikes(rating: Int, enabled: Boolean, iconSize: Dp = 40.dp, onRatingChanged: (Int) -> Unit = {}) {
    Row {
        for (i in 1..5) {
            val image = if (i <= rating) {
                painterResource(id = R.drawable.bicicleta_filled)
            } else {
                painterResource(id = R.drawable.bicicleta_outlined)
            }

            if (enabled) {
                IconButton(onClick = { onRatingChanged(i) }) {
                    Icon(painter = image, contentDescription = "Bici $i", modifier = Modifier
                        .padding(horizontal = 2.dp)
                        .size(iconSize))
                }
            } else {
                Icon(painter = image, contentDescription = "Bici $i", modifier = Modifier
                    .padding(horizontal = 2.dp)
                    .size(iconSize))
            }
        }
    }
}
