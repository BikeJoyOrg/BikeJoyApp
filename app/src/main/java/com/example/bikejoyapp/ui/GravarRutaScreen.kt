package com.example.bikejoyapp.ui

import com.example.bikejoyapp.viewmodel.GravarRutaViewModel
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.example.bikejoyapp.R

@Composable
fun  GravarRutaScreen(viewModel: GravarRutaViewModel,) {
    val barcelona = LatLng(41.38879, 2.15899)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(barcelona, 13f)
    }
    val pl: List<LatLng> by viewModel.pl.observeAsState(mutableListOf<LatLng>())
    val posstart: LatLng by viewModel.posstart.observeAsState(LatLng(0.0,0.0))
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val mapHeight = maxHeight.value * 0.9 // Adjust this value to set the height of the map
        val buttonHeight = maxHeight.value - mapHeight

        Column {
            Box(modifier = Modifier.height(mapHeight.dp)) {
                GoogleMap(
                    onMapClick = { latLng ->
                        viewModel.onselected("${latLng.longitude},${latLng.latitude}")
                    },
                    cameraPositionState = cameraPositionState,
                    properties = MapProperties(isMyLocationEnabled = true, mapType = MapType.NORMAL)
                ) {
                    Polyline(points = pl, color = Color.Magenta, width = 15.0f)
                    Marker(state = MarkerState(position = posstart),
                        icon = BitmapDescriptorFactory.fromResource(R.mipmap.bandera_start_escala))
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.align(Alignment.CenterHorizontally)){
                Button(
                    onClick = { viewModel.ReferInici()},
                    Modifier.height(48.dp),
                ) {
                    Text(text = "Refer Inici")
                }
                Button(
                    onClick = { viewModel.guardarRuta()},
                    Modifier.height(48.dp),

                ) {
                    Text(text = "Gravar Ruta")
                }
                Button(
                    onClick = { viewModel.Desfer()},
                    Modifier.height(48.dp),
                ) {
                    Text(text = "Desfer Punt")
                }

            }
        }
    }
}

/*
@Composable
fun GravarButton(onselected: () ->Unit) {
    Button(
        onClick = { onselected() },
        Modifier.height(48.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Magenta,
                                                contentColor = Color.White)
        ) {
        Text(text = "Gravar Ruta")
    }


}*/