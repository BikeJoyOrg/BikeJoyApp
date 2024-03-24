package com.example.bikejoyapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bikejoyapp.data.MascotaImatges
import com.example.bikejoyapp.data.Mascotes
import com.example.bikejoyapp.data.MascotesAconseguides


val user = "a"
var nom = ""

@Preview
@Composable
fun PetScreen() {
    var selectedPet by remember { mutableStateOf(false) }
    LazyColumn(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxSize()
    )
    {
        for (j in 0..2) {
            item {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    for (i in j * 3..j * 3 + 2) {
                        Column(modifier = Modifier)
                        {
                            if (MascotesAconseguides.teMascota(Mascotes.mascotes[i].name, user)) {
                                if (MascotesAconseguides.estaEquipat(
                                        Mascotes.mascotes[i].name,
                                        user
                                    )
                                ) {
                                    if (MascotesAconseguides.getNivell(
                                            Mascotes.mascotes[i].name,
                                            user
                                        ) == 1
                                    ) {
                                        Row(
                                            horizontalArrangement = Arrangement.Center,
                                            modifier = Modifier.width(130.dp)
                                        ) {
                                            Text(
                                                text = Mascotes.mascotes[i].name,
                                                fontSize = 30.sp
                                            )
                                        }
                                        Image(
                                            painter = painterResource(id = MascotaImatges.numDrawables[Mascotes.mascotes[i].img1]),
                                            contentDescription = Mascotes.mascotes[i].name,
                                            modifier = Modifier
                                                .height(130.dp)
                                                .border(
                                                    width = 5.dp,
                                                    color = Color.Green
                                                )
                                        )
                                        Box(modifier = Modifier.width(130.dp)) {
                                            Button(onClick = { selectedPet = true; nom = Mascotes.mascotes[i].name }, modifier = Modifier.width(130.dp)) {
                                                Text(text = "Info")
                                            }
                                        }
                                    } else if (MascotesAconseguides.getNivell(
                                            Mascotes.mascotes[i].name,
                                            user
                                        ) == 2
                                    ) {
                                        Row(
                                            horizontalArrangement = Arrangement.Center,
                                            modifier = Modifier.width(130.dp)
                                        ) {
                                            Text(
                                                text = Mascotes.mascotes[i].name,
                                                fontSize = 30.sp
                                            )
                                        }
                                        Image(
                                            painter = painterResource(id = MascotaImatges.numDrawables[Mascotes.mascotes[i].img2]),
                                            contentDescription = Mascotes.mascotes[i].name,
                                            modifier = Modifier
                                                .height(130.dp)
                                                .border(
                                                    width = 5.dp,
                                                    color = Color.Green
                                                )
                                        )
                                        Box(modifier = Modifier.width(130.dp)) {
                                            Button(onClick = { selectedPet = true; nom = Mascotes.mascotes[i].name }, modifier = Modifier.width(130.dp)) {
                                                Text(text = "Info")
                                            }
                                        }
                                    } else if (MascotesAconseguides.getNivell(
                                            Mascotes.mascotes[i].name,
                                            user
                                        ) == 3
                                    ) {
                                        Row(
                                            horizontalArrangement = Arrangement.Center,
                                            modifier = Modifier.width(130.dp)
                                        ) {
                                            Text(
                                                text = Mascotes.mascotes[i].name,
                                                fontSize = 30.sp
                                            )
                                        }
                                        Image(
                                            painter = painterResource(id = MascotaImatges.numDrawables[Mascotes.mascotes[i].img3]),
                                            contentDescription = Mascotes.mascotes[i].name,
                                            modifier = Modifier
                                                .height(130.dp)
                                                .border(
                                                    width = 5.dp,
                                                    color = Color.Green
                                                )
                                        )
                                        Box(modifier = Modifier.width(130.dp)) {
                                            Button(onClick = { selectedPet = true; nom = Mascotes.mascotes[i].name }, modifier = Modifier.width(130.dp)) {
                                                Text(text = "Info")
                                            }
                                        }
                                    }
                                } else {
                                    if (MascotesAconseguides.getNivell(
                                            Mascotes.mascotes[i].name,
                                            user
                                        ) == 1
                                    ) {
                                        Row(
                                            horizontalArrangement = Arrangement.Center,
                                            modifier = Modifier.width(130.dp)
                                        ) {
                                            Text(
                                                text = Mascotes.mascotes[i].name,
                                                fontSize = 30.sp
                                            )
                                        }
                                        Image(
                                            painter = painterResource(id = MascotaImatges.numDrawables[Mascotes.mascotes[i].img1]),
                                            contentDescription = Mascotes.mascotes[i].name,
                                            modifier = Modifier
                                                .height(130.dp)
                                        )
                                        Box(modifier = Modifier.width(130.dp)) {
                                            Button(onClick = { selectedPet = true; nom = Mascotes.mascotes[i].name }, modifier = Modifier.width(130.dp)) {
                                                Text(text = "Info")
                                            }
                                        }
                                    } else if (MascotesAconseguides.getNivell(
                                            Mascotes.mascotes[i].name,
                                            user
                                        ) == 2
                                    ) {
                                        Row(
                                            horizontalArrangement = Arrangement.Center,
                                            modifier = Modifier.width(130.dp)
                                        ) {
                                            Text(
                                                text = Mascotes.mascotes[i].name,
                                                fontSize = 30.sp
                                            )
                                        }
                                        Image(
                                            painter = painterResource(id = MascotaImatges.numDrawables[Mascotes.mascotes[i].img2]),
                                            contentDescription = Mascotes.mascotes[i].name,
                                            modifier = Modifier.height(130.dp)
                                        )
                                        Box(modifier = Modifier.width(130.dp)) {
                                            Button(onClick = { selectedPet = true; nom = Mascotes.mascotes[i].name }, modifier = Modifier.width(130.dp)) {
                                                Text(text = "Info")
                                            }
                                        }
                                    } else if (MascotesAconseguides.getNivell(
                                            Mascotes.mascotes[i].name,
                                            user
                                        ) == 3
                                    ) {
                                        Row(
                                            horizontalArrangement = Arrangement.Center,
                                            modifier = Modifier.width(130.dp)
                                        ) {
                                            Text(
                                                text = Mascotes.mascotes[i].name,
                                                fontSize = 30.sp
                                            )
                                        }
                                        Image(
                                            painter = painterResource(id = MascotaImatges.numDrawables[Mascotes.mascotes[i].img3]),
                                            contentDescription = Mascotes.mascotes[i].name,
                                            modifier = Modifier.height(130.dp)
                                        )
                                        Box(modifier = Modifier.width(130.dp)) {
                                            Button(onClick = { selectedPet = true; nom = Mascotes.mascotes[i].name }, modifier = Modifier.width(130.dp)) {
                                                Text(text = "Info")
                                            }
                                        }
                                    }
                                }
                            } else {
                                Spacer(modifier = Modifier.height(30.dp))
                                Image(
                                    painter = painterResource(id = MascotaImatges.numDrawables[Mascotes.mascotes[i].img1l]),
                                    contentDescription = "Josep",
                                    modifier = Modifier.height(130.dp)
                                )
                                Text(
                                    text = "Locked",
                                    modifier = Modifier.width(130.dp),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                        if(selectedPet) {
                            InfoPetWidget (MascotesAconseguides.getMascotaAconseguida(nom, user), onDismiss = {selectedPet = false})
                        }

                    }

                }
            }
        }
    }
}