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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bikejoyapp.data.MascotaAconseguida
import com.example.bikejoyapp.data.MascotaImatges
import com.example.bikejoyapp.data.Mascotes
import com.example.bikejoyapp.data.MascotesAconseguides

@Composable
fun InfoPetWidget(mascotaAconseguida: MascotaAconseguida, onDismiss:()->Unit) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = { /*TODO*/ },
        modifier = Modifier.height((800.dp)),

        title = {
            Row(modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = mascotaAconseguida.nomMascota, fontSize = 32.sp, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
            }
        },

        text = {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Image(
                        painter = painterResource(id = MascotaImatges.numDrawables[Mascotes.getMascotaPorNombre(mascotaAconseguida.nomMascota)?.img1!!]),
                        contentDescription = mascotaAconseguida.nomMascota,
                        modifier = Modifier.height(100.dp)
                    )
                }
                item {
                    Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Evo")
                }
                item {
                    if (mascotaAconseguida.nivell >= 2) {
                        Image(
                            painter = painterResource(
                                id = MascotaImatges.numDrawables[Mascotes.getMascotaPorNombre(
                                    mascotaAconseguida.nomMascota
                                )?.img2!!]
                            ),
                            contentDescription = mascotaAconseguida.nomMascota,
                            modifier = Modifier.height(100.dp)
                        )
                    }
                    else {
                        Image(
                            painter = painterResource(
                                id = MascotaImatges.numDrawables[Mascotes.getMascotaPorNombre(
                                    mascotaAconseguida.nomMascota
                                )?.img2l!!]
                            ),
                            contentDescription = mascotaAconseguida.nomMascota,
                            modifier = Modifier.height(100.dp)
                        )
                    }
                }
                item {
                    Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Evo")
                }
                item {
                    if (mascotaAconseguida.nivell >= 3) {
                        Image(
                            painter = painterResource(
                                id = MascotaImatges.numDrawables[Mascotes.getMascotaPorNombre(
                                    mascotaAconseguida.nomMascota
                                )?.img3!!]
                            ),
                            contentDescription = mascotaAconseguida.nomMascota,
                            modifier = Modifier.height(100.dp)
                        )
                    }
                    else {
                        Image(
                            painter = painterResource(
                                id = MascotaImatges.numDrawables[Mascotes.getMascotaPorNombre(
                                    mascotaAconseguida.nomMascota
                                )?.img3l!!]
                            ),
                            contentDescription = mascotaAconseguida.nomMascota,
                            modifier = Modifier.height(100.dp)
                        )
                    }
                }
                item {
                    Text(text = "Nivell: "+mascotaAconseguida.nivell.toString(), fontSize = 32.sp, modifier = Modifier.padding(top = 30.dp))
                }
                item {
                    Text(text = "Bonus: "+ (mascotaAconseguida.nivell*10).toString() + Mascotes.getMascotaPorNombre(mascotaAconseguida.nomMascota)?.bonus, fontSize = 32.sp, modifier = Modifier.padding(top = 30.dp))
                }
                item {
                    Button(onClick = { MascotesAconseguides.equipar(mascotaAconseguida.nomMascota, mascotaAconseguida.nicknameUsuari); println(mascotaAconseguida.nomMascota + mascotaAconseguida.nicknameUsuari) }) {
                        Text(text = "Equipar")
                    }
                }
            }
        }
    )
}