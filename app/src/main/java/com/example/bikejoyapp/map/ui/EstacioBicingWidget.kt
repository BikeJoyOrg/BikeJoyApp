package com.example.bikejoyapp.map.ui

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.bikejoyapp.map.viewmodel.EstacionsViewModel
import com.example.bikejoyapp.utils.MainViewModel

@SuppressLint("RememberReturnType")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EstacioBicingWidget(
    navController: NavController,
    mainViewModel: MainViewModel,
    stationViewModel: EstacionsViewModel
) {
    // Inicializa stationId como null
    val stationId = remember { mutableStateOf<String?>(null) }

    // LaunchedEffect con una clave constante se ejecuta solo una vez
    LaunchedEffect(true) {
        stationId.value = navController.currentBackStackEntry?.arguments?.getString("stationId")
    }
    val estacioBicing = stationId.let { it.value?.let { it1 -> stationViewModel.getStationById(it1).observeAsState().value } }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(top = 16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Estació bicing",
                color = Color.Black,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center),
                textAlign = TextAlign.Center
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        estacioBicing?.let {
            it.second?.let { it1 ->
                Text(
                    text = it1,
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Light,
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
            it.first?.let { it1 ->
                BicingInfoRow(
                    labelText = "Bicicletes manuals:",
                    value = it1.mechanical,
                    color = Color.Red
                )
                BicingInfoRow(
                    labelText = "Bicicletes elèctriques:",
                    value = it1.ebike,
                    color = Color.Blue
                )
                BicingInfoRow(
                    labelText = "Anclatges disponibles:",
                    value = it1.num_docks_available,
                    color = Color.Black
                )
            }
        }
    }
}

@Composable
fun BicingInfoRow(
    labelText: String,
    value: Int,
    color: Color,
) {
    Row(
        modifier = Modifier
            .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(modifier = Modifier.width(24.dp))
        Text(
            text = labelText,
            color = Color.Black,
            fontSize = 20.sp,
            fontWeight = FontWeight.Normal,
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp)
                .align(Alignment.CenterVertically),
            textAlign = TextAlign.Start
        )
        ColoredCircleValue(
            value = value,
            color = color
        )
        Spacer(modifier = Modifier.width(24.dp))
    }
}

@Composable
fun ColoredCircleValue(value: Int, color: Color) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .background(color, shape = CircleShape)
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = value.toString(),
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}