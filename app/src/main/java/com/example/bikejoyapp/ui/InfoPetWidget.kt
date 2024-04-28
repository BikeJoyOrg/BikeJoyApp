package com.example.bikejoyapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.bikejoyapp.data.MascotaAconseguida
import com.example.bikejoyapp.data.MascotaImatges
import com.example.bikejoyapp.data.Mascotes
import com.example.bikejoyapp.data.MascotesAconseguides

@Composable
fun InfoPetWidget(mascotaAconseguida: MascotaAconseguida, onDismiss: () -> Unit) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = { /*TODO*/ },
        modifier = Modifier.height((800.dp)),

        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = mascotaAconseguida.mascota.name,
                    fontSize = 32.sp,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        },

        text = {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(mascotaAconseguida.mascota.img1)
                            .build(),
                        contentDescription = mascotaAconseguida.mascota.name,
                        modifier = Modifier
                            .height(130.dp)
                    )
                }
                item {
                    Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Evo")
                }
                item {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(if (mascotaAconseguida.nivell >= 2) mascotaAconseguida.mascota.img2 else mascotaAconseguida.mascota.img2l)
                            .build(),
                        contentDescription = mascotaAconseguida.mascota.name,
                        modifier = Modifier
                            .height(130.dp)
                    )
                }
                item {
                    Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Evo")
                }
                item {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(if (mascotaAconseguida.nivell >= 3) mascotaAconseguida.mascota.img3 else mascotaAconseguida.mascota.img3l)
                            .build(),
                        contentDescription = mascotaAconseguida.mascota.name,
                        modifier = Modifier
                            .height(130.dp)
                    )
                }
                item {
                    Text(
                        text = "Bonus1: " + ((mascotaAconseguida.mascota.bonus1.minus(1)).times(
                            mascotaAconseguida.nivell
                        ).times(100).toInt()).toString() + "%",
                        fontSize = 32.sp, modifier = Modifier.padding(top = 10.dp)
                    )
                }
                item {
                    Text(
                        text = "Bonus2: " + ((mascotaAconseguida.mascota.bonus2.minus(1)).times(
                            mascotaAconseguida.nivell
                        ).times(100).toInt()).toString() + "%",
                        fontSize = 32.sp, modifier = Modifier.padding(top = 10.dp)
                    )
                }
                item {
                    Text(
                        text = "Bonus3: " + ((mascotaAconseguida.mascota.bonus3.minus(1)).times(
                            mascotaAconseguida.nivell
                        ).times(100).toInt()).toString() + "%",
                        fontSize = 32.sp, modifier = Modifier.padding(top = 10.dp, bottom = 10.dp)
                    )
                }
                item {
                    Button(onClick = {
                        MascotesAconseguides.equipar(
                            mascotaAconseguida.mascota.name,
                            mascotaAconseguida.nicknameUsuari
                        )
                        onDismiss()
                    }) {
                        Text(text = "Equipar")
                    }
                }
            }
        }
    )
}