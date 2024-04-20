package com.example.bikejoyapp.ui

import android.annotation.SuppressLint
import android.app.Notification
import androidx.compose.foundation.layout.Arrangement
import com.example.bikejoyapp.viewmodel.GravarRutaViewModel
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontSynthesis.Companion.Style
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
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
import com.example.bikejoyapp.ui.theme.magentaClaroCrema
import com.example.bikejoyapp.ui.theme.magentaOscuroCrema
import com.example.bikejoyapp.viewmodel.MainViewModel

@SuppressLint("Range")
@Composable
fun  GravarRutaScreen(viewModel: GravarRutaViewModel,mainViewModel: MainViewModel) {
    val barcelona = LatLng(41.38879, 2.15899)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(barcelona, 13f)
    }
    val pl: List<LatLng> by viewModel.pl.observeAsState(mutableListOf<LatLng>())
    val posstart: LatLng by viewModel.posstart.observeAsState(LatLng(0.0,0.0))
    val referEnable: Boolean by viewModel.referEnable.observeAsState(false)
    val desferEnable: Boolean by viewModel.desferEnable.observeAsState(false)
    val guardarEnable: Boolean by viewModel.guardarEnable.observeAsState(false)
    val showDialog: Boolean by viewModel.showDialog.observeAsState(false)
    val nomRuta: String by viewModel.nomRuta.observeAsState("")
    val descRuta: String by viewModel.descRuta.observeAsState("")

    val distanciaRuta: Double by viewModel.distanciaRuta.observeAsState(0.0)
    val tempsRuta: Int by viewModel.tempsRuta.observeAsState(0)
    if  (showDialog) {
        Dialog(onDismissRequest = { /*TODO*/ }) {
            Card(
                modifier = Modifier
                    .width(500.dp)
                    .height(400.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                    ){Text(
                        text = "Introdueix nom de la ruta",
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp

                    )}
                    TextField(
                        value = nomRuta,
                        onValueChange = { viewModel.assignaNom(it) },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = TextStyle(fontSize = 20.sp),
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.height(30.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        ){
                        Text(
                            text = "Descrpció de la ruta",
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp

                        )
                    }
                    TextField(
                        value = descRuta,
                        onValueChange = { viewModel.assignaDesc(it) },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = TextStyle(fontSize = 20.sp),
                        maxLines = 3
                        )
                    Spacer(modifier = Modifier.weight(1f))
                    TempsDistancia_vertical(distanciaRuta = distanciaRuta, tempsRuta = tempsRuta)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth().weight(1f),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        TextButton(
                            onClick = { viewModel.guardarRuta(mainViewModel) },
                        ) {
                            Text("Guardar", fontSize = 15.sp)
                        }
                        TextButton(
                                onClick = { viewModel.dialogGuardarRutaDismiss() },
                        ) {
                            Text("Cancel·lar", fontSize = 15.sp)
                        }
                    }
                }
            }
        }
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val mapHeight = maxHeight.value * 0.83 // Adjust this value to set the height of the map
        val buttonHeight = maxHeight.value - mapHeight

        Column {
            TempsDistancia_horitzontal(distanciaRuta = distanciaRuta, tempsRuta = tempsRuta)
            Box(modifier = Modifier.height(mapHeight.dp)) {
                GoogleMap(
                    onMapClick = { latLng ->
                        viewModel.onselected("${latLng.longitude},${latLng.latitude}")
                    },
                    cameraPositionState = cameraPositionState,
                    properties = MapProperties(isMyLocationEnabled = true, mapType = MapType.NORMAL)
                ) {
                    Polyline(points = pl, color = magentaOscuroCrema, width = 15.0f)
                    Marker(state = MarkerState(position = posstart),
                        icon = BitmapDescriptorFactory.fromResource(R.mipmap.bandera_start_escala))
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.align(Alignment.CenterHorizontally)){
                Button(
                    onClick = { viewModel.ReferInici()},
                    Modifier.height(48.dp),
                    enabled = referEnable
                ) {
                    Text(text = "Refer Inici")
                }
                Button(
                    onClick = { viewModel.dialogGuardarRuta()},
                    Modifier.height(48.dp),
                    enabled = guardarEnable

                ) {
                    Text(text = "Gravar Ruta")
                }
                Button(
                    onClick = { viewModel.Desfer()},
                    Modifier.height(48.dp),
                    enabled = desferEnable
                ) {
                    Text(text = "Desfer Punt")
                }

            }
        }
    }
}

@Composable
fun TempsDistancia_horitzontal(distanciaRuta: Double, tempsRuta: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(R.drawable.baseline_access_time_24),
                contentDescription = "Tiempo de navegación"
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Temps: ${tempsRuta} min")
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(R.drawable.baseline_directions_bike_24),
                contentDescription = "Kilómetros"
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Dist: ${"%.2f".format(distanciaRuta)} m")
        }
    }
}
@Composable
fun TempsDistancia_vertical(distanciaRuta: Double, tempsRuta: Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(R.drawable.baseline_access_time_24),
                contentDescription = "Tiempo de navegación"
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Temps: ${tempsRuta} min")
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(R.drawable.baseline_directions_bike_24),
                contentDescription = "Kilómetros"
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Dist: ${"%.2f".format(distanciaRuta)} m")
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