package com.example.bikejoyapp.profile.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bikejoyapp.R
import androidx.compose.foundation.Canvas
import com.example.bikejoyapp.utils.MyAppRoute
import com.example.bikejoyapp.utils.MainViewModel

@Composable
fun ProfileScreen(mainViewModel: MainViewModel) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Box(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .size(100.dp)
            ) {
                Canvas(modifier = Modifier.matchParentSize()) {
                    val purple = Color(206, 194, 242)
                    drawCircle(color = purple)
                }
                Image(
                    painter = painterResource(id = R.drawable.avatar_death),
                    contentDescription = "Profile picture",
                    modifier = Modifier
                        .size(70.dp)
                        .align(Alignment.Center)
                )
            }
        }
        val username = "Username"
        Text(
            text = username,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 16.dp), // Agrega padding superior
            fontSize = 24.sp // Aumenta el tamaño de la letra
        )
        val level = "LEVEL X"
        Text(level, modifier = Modifier.align(Alignment.CenterHorizontally))

        val size = 40.dp
        Row() {
            for (i in 1..4) {
                Box(
                    modifier = Modifier
                        .padding(start = 30.dp, end = 25.dp, top = 16.dp)
                        .size(size)
                ) {
                    Button(onClick = {}) {

                    }
                    Image(
                        painter = painterResource(id = R.drawable.avatar_death),
                        contentDescription = "Profile picture",
                        modifier = Modifier
                            .size(size.times(0.7f))
                            .align(Alignment.Center) // Esto hace que la imagen sea redonda
                    )
                }
            }
        }

        Text(
            "Stats",
            modifier = Modifier.padding(top = 8.dp, start = 12.dp, bottom = 8.dp),
            fontSize = 24.sp
        )
        for (i in 1..5) {
            Row(modifier = Modifier.padding(start = 30.dp)) {
                Image(
                    painter = painterResource(id = R.drawable.avatar_death),
                    contentDescription = "Star icon",
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    "Distancia recorrida",
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .align(Alignment.CenterVertically)
                )
                Text(
                    (i * 100).toString(),
                    modifier = Modifier
                        .padding(start = 20.dp)
                        .align(Alignment.CenterVertically)
                )
            }
        }
        Button(onClick = {
            val route = MyAppRoute.Pet.createRoute()
            mainViewModel.navigateToDynamic(route)
        }) {
            Text("Mascotas")
        }
        Button(onClick = {
            val route = MyAppRoute.PurchaseHistory.createRoute()
            mainViewModel.navigateToDynamic(route)
        }) {
            Text("Historial de compras")
        }
    }
}