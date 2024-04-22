package com.example.bikejoyapp.ui

import android.util.Log
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
import androidx.compose.material.icons.filled.Check
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
import androidx.compose.ui.text.style.TextAlign
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
import com.example.bikejoyapp.data.Comment



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

    RouteDetailScreen(routesViewModel, mainViewModel, rutaUsuariPreview, userHasCompletedRoute = false)
}


enum class ViewType {
    Details,
    Comments
}

@Composable
fun RouteDetailScreen(
    routesViewModel: RoutesViewModel,
    mainViewModel: MainViewModel,
    route: RutaUsuari,
    userHasCompletedRoute: Boolean
) {
    val fixedRating = route.RuteRating
        val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(route.PuntIniciLat, route.PuntIniciLong), 12f)
    }

    val puntosIntermedios by routesViewModel.puntosIntermedios.observeAsState(initial = emptyList())
    var currentView by remember { mutableStateOf(ViewType.Details) }

    LaunchedEffect(route.RuteId) {
        routesViewModel.getPuntosIntermedios(route.RuteId ?: 0)
        Log.d("UI", "Actualizando puntos en el mapa: $puntosIntermedios")
    }
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Box(
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
        ) {
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
                modifier = Modifier.weight(1f)
            )
            RatingBikes(
                rating = fixedRating,
                enabled = false,
            )
        }
        Spacer(Modifier.height(16.dp))
        Box(
            modifier = Modifier.fillMaxSize(),
        ) {
            when (currentView) {
                ViewType.Details -> {
                    // Mostrar la vista de detalles de la ruta
                    DetailsView(route, userHasCompletedRoute)
                }

                ViewType.Comments -> {
                    // Mostrar la vista de comentarios
                    CommentsView(routesViewModel, userHasCompletedRoute)
                }
            }
            Spacer(Modifier.height(16.dp))
            FloatingActionButton(
                onClick = {
                    currentView = when (currentView) {
                        ViewType.Details -> ViewType.Comments
                        ViewType.Comments -> ViewType.Details
                    }
                },
                modifier = Modifier
                    .align(
                        if (currentView == ViewType.Comments) Alignment.BottomStart else Alignment.BottomEnd
                    )
                    .padding(16.dp)
            ) {
                Text(
                    text = if (currentView == ViewType.Details) "Ver comentarios >>" else "<< Ver Detalles"
                )
            }
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






@Composable
fun DetailsView(
    route: RutaUsuari,
    userHasCompletedRoute: Boolean) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column {
            Text (
                text = "Descripción",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(16.dp)
            )
            Text(
                text = route.RuteDescription ?: "Descripción no disponible",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(horizontal = 16.dp),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row {
                Text(
                    text = "Distancia: ${route.RuteDistance} km",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(16.dp)
                )
                Text(
                    text = "Tiempo estimado: ${route.RuteTime} min",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(16.dp)
                )
            }
            /*
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Check, contentDescription = "Ruta completada", tint = Color.Green)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Ruta Completada",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(horizontal = 16.dp),
                    textAlign = TextAlign.Center
                )
            }
            */
            if (userHasCompletedRoute) {
                var userRating by remember { mutableIntStateOf(0) }
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(
                        text = "Tu valoración:",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(16.dp)
                    )
                    RatingBikes(rating = userRating, enabled = true, onRatingChanged = { newRating ->
                        userRating = newRating
                        // Aquí podrías llamar a una función de tu ViewModel para enviar la valoración del usuario al backend
                        //routesViewModel.submitUserRating(routeId = route.RuteId, rating = newRating)
                    })
                }
            }
        }
    }
}

@Composable
fun CommentItem(comment: Comment) {
    // Reemplaza "Comment" por el modelo de comentario que estés utilizando
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(comment.author, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(comment.text, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun NewCommentSection(onCommentAdded: (String) -> Unit) {
    var commentText by remember { mutableStateOf("") }

    Column {
        TextField(
            value = commentText,
            onValueChange = { commentText = it },
            label = { Text("Deja un comentario...") },
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = {
                onCommentAdded(commentText)
                commentText = "" // Limpiar el campo de texto después de enviar el comentario
            },
            enabled = commentText.isNotBlank(), // El botón está habilitado solo si el comentario no está vacío
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Enviar")
        }
    }
}

@Composable
fun CommentsView(
    routesViewModel: RoutesViewModel,
    userHasCompletedRoute: Boolean
) {
    val routeComments by routesViewModel.routeComments.observeAsState(initial = emptyList())
    Column(modifier = Modifier.fillMaxSize()) {
        if (userHasCompletedRoute) {
            Card(modifier = Modifier.fillMaxWidth()) {

                NewCommentSection(onCommentAdded = { commentText ->
                    //routesViewModel.addNewComment(route.RuteId, commentText)
                })
            }
            Spacer(Modifier.height(16.dp))
        }
        Card(modifier = Modifier.fillMaxWidth()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                item {
                    Text(
                        "Comentarios de la ruta",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(horizontal = 16.dp),
                        textAlign = TextAlign.Center
                    )
                    routeComments.forEach { comment ->
                        CommentItem(comment)
                    }
                }
            }
        }

    }
}


