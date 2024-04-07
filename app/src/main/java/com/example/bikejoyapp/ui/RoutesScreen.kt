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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
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


@Composable
fun RoutesScreen(rutesviewModel: RoutesViewModel,mainViewModel: MainViewModel) {
    val routes by rutesviewModel.routes.observeAsState(initial = emptyList())
    val searchQuery by rutesviewModel.searchQuery.observeAsState("")
    val activeFilters by rutesviewModel.activeFilters
    val possibleFilters = rutesviewModel.possibleFilters

    Column(modifier = Modifier
        .fillMaxSize()
    ) {
        AddRouteForm(rutesviewModel,mainViewModel)
        SearchBar(searchQuery = searchQuery,
            onSearchQueryChanged = { query -> rutesviewModel.onSearchQueryChanged(query) },
            onPerformSearch = { rutesviewModel.performSearch() }
        )
        FilterForm(possibleFilters = possibleFilters,
            activeFilters = activeFilters,
            onFilterChanged = { filter, isSelected ->
                rutesviewModel.onFilterChanged(filter, isSelected)
            },
            onApplyFilters = {
                rutesviewModel.performSearchWithFilters()
            }

        )
        RoutesList(modifier = Modifier.weight(1f), routes = routes)

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
fun FilterForm(
    possibleFilters: List<String>,
    activeFilters: List<String>,
    onFilterChanged: (String, Boolean) -> Unit,
    onApplyFilters: () -> Unit
) {
    var showFiltersDialog by remember { mutableStateOf(false) }

    Column {
        TextButton(onClick = { showFiltersDialog = true }) {
            Text("Mostrar filtros")
        }

        if (showFiltersDialog) {
            // Esta es una alternativa al menú desplegable utilizando un AlertDialog
            AlertDialog(
                onDismissRequest = { showFiltersDialog = false },
                title = { Text("Filtros") },
                text = {
                    // Lista de todos los filtros posibles con un Checkbox
                    Column {
                        possibleFilters.forEach { filter ->
                            val isChecked = activeFilters.contains(filter)
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth().padding(8.dp)
                            ) {
                                Checkbox(
                                    checked = isChecked,
                                    onCheckedChange = { isSelected ->
                                        onFilterChanged(filter, isSelected)
                                    }
                                )
                                Text(text = filter)
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            onApplyFilters()
                            showFiltersDialog = false
                        }
                    ) {
                        Text("Aplicar")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showFiltersDialog = false }
                    ) {
                        Text("Cancelar")
                    }
                }
            )
        }
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
fun AddRouteForm(rutesviewModel: RoutesViewModel, mainViewModel: MainViewModel){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp) // Asegúrate de que este padding sea consistente con el de SearchBar
    ) {
        Button(
            onClick = { mainViewModel.navigateTo(MyAppRoute.GravarRuta)},
            modifier = Modifier
                .wrapContentWidth() // Esto asegura que el botón no estire el Row innecesariamente
        ) {
            Text("Crear Ruta")
        }
    }
}