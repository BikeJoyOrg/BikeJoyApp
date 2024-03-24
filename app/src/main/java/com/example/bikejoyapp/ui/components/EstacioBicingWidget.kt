package com.example.bikejoyapp.ui.components

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.bikejoyapp.data.EstacioBicing
import com.example.bikejoyapp.viewmodel.EstacionsViewModel
import com.example.bikejoyapp.viewmodel.MainViewModel

@Composable
fun EstacioBicingWidget(
    navController: NavController,
    mainViewModel: MainViewModel,
    stationViewModel: EstacionsViewModel
) {
    val stationId = navController.currentBackStackEntry?.arguments?.getString("stationId")
    val estacioBicing = stationId?.let { stationViewModel.getStationById(it).observeAsState().value }

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
            IconButton(
                onClick = { mainViewModel.navigateBack() },
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
            Text(
                text = "Estació bicing",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center),
                textAlign = TextAlign.Center
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
        estacioBicing?.let {
            BicingInfoRow(
                labelText = "Bicicletes manuals:",
                value = it.mechanical,
                color = Color.Red
            )
            BicingInfoRow(
                labelText = "Bicicletes elèctriques:",
                value = it.ebike,
                color = Color.Blue
            )
            BicingInfoRow(
                labelText = "Anclatges disponibles:",
                value = it.num_docks_available,
                color = Color.Black
            )
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
        Text(
            text = labelText,
            fontSize = 20.sp,
            fontWeight = FontWeight.Normal,
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp),
            textAlign = TextAlign.Start
        )
        ColoredCircleValue(
            value = value,
            color = Color.Red
        )
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