package com.example.bikejoyapp.ui

import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Button
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.getValue
import com.example.bikejoyapp.ui.components.RoutePreviewWidget
import com.example.bikejoyapp.viewmodel.RoutesViewModel
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import com.example.bikejoyapp.data.MyAppRoute
import com.example.bikejoyapp.viewmodel.MainViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.example.bikejoyapp.data.RutaUsuari
import com.example.bikejoyapp.data.SharedPrefUtils


@Composable
fun RoutesScreen(rutesviewModel: RoutesViewModel,mainViewModel: MainViewModel) {
    val routes by rutesviewModel.routes.observeAsState(initial = emptyList())
    val searchQuery by rutesviewModel.searchQuery.observeAsState("")

    Column(modifier = Modifier
        .fillMaxSize()
    ) {
        AddRouteForm(rutesviewModel,mainViewModel)
        SearchBar(searchQuery = searchQuery,
            onSearchQueryChanged = { query -> rutesviewModel.onSearchQueryChanged(query) },
            onPerformSearch = { rutesviewModel.performSearch() }
        )
        FilterForm(
            durationRange = rutesviewModel.durationRange,
            initialDuration = rutesviewModel.durationFilter.value ?: 0f,
            distanceRange = rutesviewModel.distanceRange,
            initialDistance = rutesviewModel.distanceFilter.value ?: 0f,
            initialSelectedLocation = rutesviewModel.startLocationFilter.value ?: "Cualquier zona",
            startLocations = rutesviewModel.startLocations,
            onApplyFilters = { duration, distance, location ->
                rutesviewModel.onDurationChange(duration)
                rutesviewModel.onDistanceChange(distance)
                rutesviewModel.onStartLocationChange(location)
                rutesviewModel.performSearchWithFilters()
            }
        )
        RoutesList(mainViewModel, modifier = Modifier.weight(1f), routes = routes)

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(searchQuery: String,
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
        placeholder = { Text("Cerca les rutes d'altres usuaris", color = Color(0xFF000000)) },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(onSearch = {
            if (searchQuery.isNotBlank()) {
                onPerformSearch()
                keyboardController?.hide()
            }
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
fun FilterForm(
    durationRange: ClosedFloatingPointRange<Float>,
    initialDuration: Float,
    distanceRange: ClosedFloatingPointRange<Float>,
    initialDistance: Float,
    initialSelectedLocation: String,
    startLocations: List<String>,
    onApplyFilters: (Float, Float, String) -> Unit
) {
    var tempDuration by remember { mutableStateOf(initialDuration) }
    var tempDistance by remember { mutableStateOf(initialDistance) }
    var tempSelectedLocation by remember { mutableStateOf(initialSelectedLocation) }
    var showFiltersDialog by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween // Esto coloca los elementos al principio y al final del Row
    ) {
        TextButton(onClick = { showFiltersDialog = true }) {
            Text("Mostrar filtros")
        }
        Button(
            onClick = {
                onApplyFilters(tempDuration, tempDistance, tempSelectedLocation)
            }
        ) {
            Text("Aplicar filtros")
        }
    }

    if (showFiltersDialog) {
        AlertDialog(
            onDismissRequest = { showFiltersDialog = false },
            title = { Text("Filtros") },
            text = {
                Column {
                    Text("Máxima Duración: ${if (tempDuration == durationRange.endInclusive) "Máx." else "${tempDuration.toInt()}h"}")
                    Slider(
                        value = tempDuration,
                        onValueChange = { tempDuration = it },
                        valueRange = durationRange,
                        steps = 5
                    )
                    Text("Máxima Distancia: ${if (tempDistance == distanceRange.endInclusive) "Máx." else "${tempDistance.toInt()}km"}")
                    Slider(
                        value = tempDistance,
                        onValueChange = { tempDistance = it },
                        valueRange = distanceRange,
                        steps = 9
                    )
                    Text("Lugar de inicio:")
                    LazyColumn {
                        items(startLocations) { location ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { tempSelectedLocation = location }
                                    .padding(8.dp)
                            ) {
                                if (location == tempSelectedLocation) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Seleccionado",
                                        tint = Color.Green
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = location,
                                    color = if (location == tempSelectedLocation) Color.Green else Color.Black,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = { showFiltersDialog = false }
                ) { Text("Aceptar") }
            },
            dismissButton = {
                TextButton(
                    onClick = { showFiltersDialog = false }
                ) { Text("Cancelar") }
            }
        )
    }
}





@Composable
fun RoutesList(mainViewModel : MainViewModel, modifier : Modifier, routes: List<RutaUsuari>) {
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
                    " ${if (routes.isNotEmpty()) "BikeJoy Routes" else "No se han encontrado rutas"}",
                )
            }
        }
        items(routes) { route ->
            RoutePreviewWidget(route.RuteName, route.RuteDescription, onClick ={
                mainViewModel.selectedRoute = route
                mainViewModel.hideBottomBar()
                mainViewModel.navigateToDynamic("RouteDetail")
            })
        }
    }
}

@Composable
fun AddRouteForm(rutesviewModel: RoutesViewModel, mainViewModel: MainViewModel){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Button(
            onClick = {
                if(SharedPrefUtils.getToken() != null) {
                    mainViewModel.navigateTo(MyAppRoute.GravarRuta)
                }
                else {
                    mainViewModel.navigateTo(MyAppRoute.LoginRequired)

                }
                      },
            modifier = Modifier
                .wrapContentWidth()
                .align(Alignment.CenterVertically)
        ) {
            Text("Crear Ruta")
        }
    }
}