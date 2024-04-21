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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.bikejoyapp.R
import com.example.bikejoyapp.data.RutaUsuari


@Composable
fun RouteDetailScreen(mainViewModel: MainViewModel, route: RutaUsuari) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        item {
            Box {
                IconButton(onClick = { mainViewModel.navigateBack() }, modifier = Modifier.align(Alignment.TopStart)) {
                    Icon(Icons.Filled.Close, contentDescription = "Cerrar")
                }
            }
            Image(painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Imagen de Ruta",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth().height(200.dp))
            Spacer(Modifier.height(16.dp))
            route.RuteName?.let { Text(text = it) }
            //RatingBikes(route.rating)
            Spacer(Modifier.height(16.dp))
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    route.RuteDescription?.let { Text(it) }
                    Spacer(Modifier.height(8.dp))
                    //Text("Zona inicial: ${route.startZone}")
                }
            }
            Spacer(Modifier.height(16.dp))
            // Comentarios y cualquier otro contenido adicional aquí
        }
        // Aquí puedes agregar un lazy item por cada comentario, por ejemplo
    }
}

/*
@Composable
fun RatingBikes(rating: Int) {
    Row {
        for (i in 1..5) {
            Icon(painter = if (i <= rating) painterResource(id = R.drawable.bike_filled) else painterResource(id = R.drawable.bike_outline),
                contentDescription = "Bici $i",
                modifier = Modifier.size(24.dp))
        }
    }
}
*/
