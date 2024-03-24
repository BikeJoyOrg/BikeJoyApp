package com.example.bikejoyapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.bikejoyapp.data.EstacioBicing
import androidx.compose.ui.graphics.Color
import com.example.bikejoyapp.viewmodel.EstacionsViewModel


@Composable
fun HomeScreen(estacionsViewModel: EstacionsViewModel, paddingValues: PaddingValues) {
    val estacions by estacionsViewModel.estacions.observeAsState(emptyList())
    val loading by estacionsViewModel.loading.observeAsState(false)
    var selectedEstacioBicing by remember { mutableStateOf<EstacioBicing?>(null) }
    var showEstacioBicingWidget by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        if (loading) {
            // Muestra un indicador de carga mientras se están cargando las estaciones
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
        else {
            Text(
                text = "Estaciones de Bicing:" + estacions.size.toString(),
                modifier = Modifier.align(Alignment.TopCenter)
            )
            IconButton(
                onClick = {
                    if (estacions.isNotEmpty()) {
                        selectedEstacioBicing = estacions[0]
                        showEstacioBicingWidget = true
                    }
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(50.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Seleccionar estació",
                    Modifier.size(100.dp)
                )
            }

            IconButton(
                onClick = {
                    if (estacions.size > 1) {
                        selectedEstacioBicing = estacions[1]
                        showEstacioBicingWidget = true
                    }
                },
                modifier = Modifier.align(Alignment.TopStart)
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Seleccionar estació",
                    Modifier.size(100.dp)
                )
            }

            if (showEstacioBicingWidget) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f))
                        .clickable(onClick = {
                            showEstacioBicingWidget = false
                        }) // Oculta el widget cuando se hace clic fuera de él
                ) {
                }
            } else println("No se ha seleccionado ninguna estación")
        }
    }
}
