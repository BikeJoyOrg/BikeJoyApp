package com.example.bikejoyapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Button
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.getValue
import com.example.bikejoyapp.ui.components.RoutePreviewWidget
import com.example.bikejoyapp.viewmodel.RoutesViewModel
import androidx.compose.runtime.livedata.observeAsState
import com.example.bikejoyapp.data.Route
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.lazy.items




@Composable
fun RoutesScreen(viewModel: RoutesViewModel = viewModel()) {
    val routes by viewModel.routes.observeAsState(initial = emptyList())
    Column(modifier = Modifier
        .fillMaxSize()
    ) {
        AddRouteForm()
        SearchBar(modifier = Modifier.fillMaxWidth())
        RoutesList(modifier = Modifier.weight(1f), routes = routes)

    }
}

@Composable
fun SearchBar(modifier: Modifier) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp) // Consistencia en el padding con AddRouteForm
    ) {
        TextField(
            value = "",
            onValueChange = {},
            label = { Text("\uD83D\uDD0E Search BikeJoy routes") },
            modifier = Modifier
                .weight(1f) // Ocupa el espacio disponible igual que en AddRouteForm
                .padding(end = 8.dp) // Consistencia en el padding con AddRouteForm
        )
    }
}

@Composable
fun RoutesList(modifier : Modifier, routes: List<Route>) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "BikeJoy Routes"
                )
            }
        }
        items(routes) { route ->
            RoutePreviewWidget(route.name, route.description, route.imageRes)
        }
    }
}

@Composable
fun AddRouteForm() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp) // Asegúrate de que este padding sea consistente con el de SearchBar
    ) {
        TextField(
            value = "",
            onValueChange = {},
            label = { Text("Route Name \uD83D\uDEB4 \uD83D\uDEB4 \uD83D\uDEB4") },
            modifier = Modifier
                .weight(1f) // Mantiene el TextField ajustado al botón
                .padding(end = 8.dp) // Espacio entre el TextField y el Button
        )
        Button(
            onClick = { /* TODO */ },
            modifier = Modifier
                .wrapContentWidth() // Esto asegura que el botón no estire el Row innecesariamente
        ) {
            Text("Crear Ruta")
        }
    }
}