package com.example.bikejoyapp.routes.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.bikejoyapp.utils.MainViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.DialogProperties
import com.example.bikejoyapp.R
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.Marker
import com.example.bikejoyapp.routes.data.Comentario
import com.example.bikejoyapp.utils.MyAppRoute
import com.example.bikejoyapp.theme.magentaOscuroCrema
import com.example.bikejoyapp.map.viewmodel.NavigationViewModel
import com.example.bikejoyapp.routes.data.CompletedRoute
import com.example.bikejoyapp.routes.data.RutaUsuari
import com.example.bikejoyapp.routes.viewmodel.RoutesViewModel
import com.google.android.gms.maps.model.BitmapDescriptorFactory

/*
val rutaUsuariPreview = RutaUsuari(
    RuteId = 1,
    RuteName = "Ruta por Montjuïc",
    RuteDescription = "Esta es una ruta de prueba",
    RuteDistance = 10.0,
    RuteTime = 60,
    RuteRating = 3,
    PuntIniciLat = 41.3851f,
    PuntIniciLong = 2.1734f
)
*/
/*
@Preview(showBackground = true)
@Composable
fun PreviewRouteDetailScreen() {
    // Crear instancias de ViewModel vacías para el preview
    val routesViewModel = RoutesViewModel()
    val mainViewModel = MainViewModel()

    RouteDetailScreen(routesViewModel, mainViewModel, rutaUsuariPreview, userHasCompletedRoute = true, NavigationViewModel())
}
*/

enum class ViewType {
    Details,
    Comments
}

@Composable
fun RouteDetailScreen(
    routesViewModel: RoutesViewModel,
    mainViewModel: MainViewModel,
    route: RutaUsuari,
    rutaCompletada: CompletedRoute?,
    navegationviewmodel: NavigationViewModel
) {
    val userHasCompletedRoute = rutaCompletada != null

    if (userHasCompletedRoute && rutaCompletada!!.rated) {
        val isRated = rutaCompletada?.rated ?: false
        routesViewModel.setRatingSent(isRated)
    }

    val fixedRating by routesViewModel.fixedRating.observeAsState(0)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(route.PuntIniciLat.toDouble(), route.PuntIniciLong.toDouble()), 16f)
    }
    val currentView by routesViewModel.currentView.observeAsState(ViewType.Details)
    val puntosIntermedios by routesViewModel.puntosIntermedios.observeAsState(emptyList())

    routesViewModel.getPuntosIntermedios(route.RuteId ?: 0)
    routesViewModel.getAverageRating(route.RuteId ?: 0)

    val puntFinalLatLng = puntosIntermedios.lastOrNull() ?: LatLng(route.PuntIniciLat.toDouble(), route.PuntIniciLong.toDouble())

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Box(
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
                .border(2.dp, Color.Black, RoundedCornerShape(10.dp)) // Agrega esta línea
        ) {
            GoogleMap(
                cameraPositionState = cameraPositionState,
                modifier = Modifier.matchParentSize(),
                properties = MapProperties(isMyLocationEnabled = true, mapType = MapType.NORMAL)
            ) {

                Marker(
                    state = MarkerState(position = LatLng(route.PuntIniciLat.toDouble(), route.PuntIniciLong.toDouble())),
                    icon = BitmapDescriptorFactory.fromResource(R.mipmap.bandera_inicit_escala)
                )
                Marker(
                    state = MarkerState(position = LatLng(puntFinalLatLng.latitude, puntFinalLatLng.longitude)),
                    icon = BitmapDescriptorFactory.fromResource(R.mipmap.bandera_start_escala)
                )
                Polyline(points = puntosIntermedios, color = magentaOscuroCrema, width = 15.0f)
            }
        }

        Spacer(Modifier.height(8.dp))
        RouteHeader(route, userHasCompletedRoute,mainViewModel,navegationviewmodel,puntosIntermedios, fixedRating)
        Box(modifier = Modifier.fillMaxSize()) {
            when (currentView) {
                ViewType.Details -> DetailsView(routesViewModel, route, userHasCompletedRoute)
                ViewType.Comments -> CommentsView(routesViewModel, route, userHasCompletedRoute)
            }
            Spacer(Modifier.height(16.dp))
            FloatingActionButton(
                onClick = { routesViewModel.toggleView() },
                modifier = Modifier
                    .align(if (currentView == ViewType.Comments) Alignment.BottomStart else Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                Text(text = if (currentView == ViewType.Details) "Ver comentarios >>" else "<< Ver Detalles")
            }
        }
    }
}


@Composable
fun RouteHeader(route: RutaUsuari, userHasCompletedRoute: Boolean, mainViewModel: MainViewModel, navegationviewmodel: NavigationViewModel, puntos: List<LatLng>, rating: Int) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = route.RuteName ?: "Nombre de ruta",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
        Spacer(Modifier.height(16.dp))
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {
                    navegationviewmodel.mostrarRuta(puntos, route.RuteId ?: 0)

                    mainViewModel.navigateTo(MyAppRoute.Map)
                    mainViewModel.showBottomBar()
                },
                modifier = Modifier
                    .padding(16.dp)
                    .background(Color.Blue, CircleShape)
                    .border(1.dp, Color.Black, CircleShape),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
            ) {
                Text("Seguir ruta")
            }
            Icon(
                painter = if (userHasCompletedRoute) painterResource(id = R.drawable.route_completed) else painterResource(
                    id = R.drawable.route_uncompleted
                ),
                tint = Color.Unspecified,
                contentDescription = "Status de la ruta",
                modifier = Modifier.size(64.dp)
            )
        }
        Text(
            text = if (userHasCompletedRoute) "¡Ruta completada!" else "Aún no has completado esta ruta",
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            color = Color.Black,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Valoración:",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.weight(1f)
        )
        RatingBikes(
            rating = rating,
            enabled = false,
            onRatingChanged = {}
        )
    }
}
@Composable
fun RatingBikes(rating: Int, enabled: Boolean, onRatingChanged: (Int) -> Unit) {
    Row {
        for (i in 1..5) {
            val image = if (i <= rating) {
                painterResource(id = R.drawable.bycicle_filled)
            } else {
                painterResource(id = R.drawable.bycicle_outlined)
            }

            IconButton(onClick = { if (enabled) onRatingChanged(i) }) {
                Icon(painter = image, contentDescription = "Bici $i", tint = Color.Unspecified)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RatingDialog(
    viewModel: RoutesViewModel,
    routeId: Int
) {
    val userRating by viewModel.userRating.observeAsState(0)

    if (viewModel.showDialog.value == true) {
        AlertDialog(
            onDismissRequest = { viewModel.hideRatingDialog() },
            content = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = MaterialTheme.colorScheme.surface)
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Dejanos tu valoración",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        RatingBikes(
                            rating = userRating,
                            enabled = true,
                            onRatingChanged = { viewModel.updateUserRating(it) })
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(onClick = {
                            viewModel.hideRatingDialog()
                        }) {
                            Text("Cancelar")
                        }
                        Button(onClick = {
                            viewModel.submitUserRating(routeId, userRating)
                            viewModel.hideRatingDialog()
                        }) {
                            Text("Enviar")
                        }
                    }
                }
            },
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = false
            )
        )
    }
}

@Composable
fun DetailsView(
    viewModel: RoutesViewModel,
    route: RutaUsuari,
    userHasCompletedRoute: Boolean
) {
    val showDialog by viewModel.showDialog.observeAsState(false)
    val ratingSent by viewModel.ratingSent.observeAsState(false)

    Card(modifier = Modifier.fillMaxWidth()) {
        Column {
            Text (
                text = "Descripción",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(16.dp).align(Alignment.CenterHorizontally),
                color = Color.Black
            )
            Text(
                text = route.RuteDescription ?: "Descripción no disponible",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(horizontal = 16.dp),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Column {
                Text(
                    text = "Distancia: ${"%.2f".format(route.RuteDistance)} m",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(8.dp)
                )
                Text(
                    text = "Tiempo estimado: ${route.RuteTime} min",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(8.dp)
                )
            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (userHasCompletedRoute && !ratingSent) {
                    Button(
                        onClick = {
                            viewModel.showRatingDialog()
                        },
                        modifier = Modifier
                            .padding(16.dp)
                            .background(Color.Blue, CircleShape)
                            .border(1.dp, Color.Black, CircleShape),
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
                    ) {
                        Text("Enviar Valoración")
                    }
                }
                if (ratingSent) {
                    Text(
                        text = "Valoración enviada",
                        style = MaterialTheme.typography.bodyLarge.copy(color = Color.Green),
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
            if (showDialog) {
                RatingDialog(viewModel = viewModel, routeId = route.RuteId ?: 0)
            }
        }
    }
}

@Composable
fun CommentItem(Comentario: Comentario) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            //Text(Comentario.user.username, style = MaterialTheme.typography.titleMedium)
            //Spacer(modifier = Modifier.height(4.dp))
            Text(Comentario.text, style = MaterialTheme.typography.bodyMedium)
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
    route: RutaUsuari,
    userHasCompletedRoute: Boolean
) {
    val routeComments by routesViewModel.routeComments.observeAsState(initial = emptyList())
    routesViewModel.getComments(route.RuteId ?: 0)
    Column(modifier = Modifier.fillMaxSize()) {
        if (userHasCompletedRoute) {
            Card(modifier = Modifier.fillMaxWidth()) {

                NewCommentSection(onCommentAdded = { commentText ->
                    route.RuteId?.let { routesViewModel.addComment(it, commentText) }
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
                    if (routeComments.isEmpty()) {
                        Text(
                            "No hay comentarios aún",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(horizontal = 16.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                    else {
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
}


