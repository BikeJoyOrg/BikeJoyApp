package com.example.bikejoyapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bikejoyapp.R
import com.example.bikejoyapp.data.Item
import com.example.bikejoyapp.data.Mascota
import com.example.bikejoyapp.data.MascotaImatges
import com.example.bikejoyapp.data.Mascotes
import com.example.bikejoyapp.data.MascotesAconseguides
import com.example.bikejoyapp.data.MyAppRoute
import com.example.bikejoyapp.viewmodel.MainViewModel
import com.example.bikejoyapp.viewmodel.MascotesViewModel
import com.example.bikejoyapp.viewmodel.ShopViewModel


val user = "b"
var nom = ""

@Composable
fun PetScreen(mascotesViewModel: MascotesViewModel, mainViewModel: MainViewModel) {
    val pets = mascotesViewModel.pets.value ?: emptyList()
    var selectedPet by remember { mutableStateOf(false) }
    var it = 0
    for (m in MascotesAconseguides.mascotesAconseguides) {
        if(it == 0)m.mascota = pets[0]
        else if(it == 1)m.mascota = pets[1]
        else if(it == 2)m.mascota = pets[2]
        else if(it == 3)m.mascota = pets[3]
        else if(it == 4)m.mascota = pets[0]
        else if(it == 5)m.mascota = pets[5]
        else if(it == 6)m.mascota = pets[2]
        else if(it == 7)m.mascota = pets[6]
        it++
    }
    LazyRow(modifier = Modifier
        .fillMaxSize()) {
        items(pets) { pet ->
            Card(
                modifier = Modifier.fillMaxWidth()
                    .width(230.dp)
                    .padding(10.dp),
                onClick = { if (MascotesAconseguides.teMascota(pet.name, user)) { selectedPet = true; nom = pet.name } }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (MascotesAconseguides.teMascota(pet.name, user)) {
                        Image(
                            painter = painterResource(id = MascotaImatges.numDrawables[pet.img1]),
                            contentDescription = pet.name,
                            modifier = Modifier
                                .height(130.dp)
                        )
                    }
                    else {
                        Image(
                            painter = painterResource(id = MascotaImatges.numDrawables[pet.imgEggl]),
                            contentDescription = pet.name,
                            modifier = Modifier
                                .height(130.dp)
                        )
                    }
                    Text(
                        text = pet.name,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    if (MascotesAconseguides.teMascota(pet.name, user)) {
                        if (MascotesAconseguides.estaEquipat(pet.name, user)) {
                            Text(
                                text = "Equipped",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        } else {
                            Text(
                                text = "Not equipped",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    } else {
                        Text(
                            text = "Locked",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
    if(selectedPet) {
        InfoPetWidget (MascotesAconseguides.getMascotaAconseguida(nom, user), onDismiss = {selectedPet = false})
    }
    /*LazyColumn(
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
                            if (MascotesAconseguides.teMascota(pets[i].name, user)) {
                                if (MascotesAconseguides.estaEquipat(
                                        pets[i].name,
                                        user
                                    )
                                ) {
                                    if (MascotesAconseguides.getNivell(
                                            pets[i].name,
                                            user
                                        ) == 1
                                    ) {
                                        Row(
                                            horizontalArrangement = Arrangement.Center,
                                            modifier = Modifier.width(130.dp)
                                        ) {
                                            Text(
                                                text = pets[i].name,
                                                fontSize = 30.sp
                                            )
                                        }
                                        Image(
                                            painter = painterResource(id = MascotaImatges.numDrawables[pets[i].img1]),
                                            contentDescription = pets[i].name,
                                            modifier = Modifier
                                                .height(130.dp)
                                                .border(
                                                    width = 5.dp,
                                                    color = Color.Green
                                                )
                                        )
                                        Box(modifier = Modifier.width(130.dp)) {
                                            Button(onClick = { selectedPet = true; nom = pets[i].name }, modifier = Modifier.width(130.dp)) {
                                                Text(text = "Info")
                                            }
                                        }
                                    } else if (MascotesAconseguides.getNivell(
                                            pets[i].name,
                                            user
                                        ) == 2
                                    ) {
                                        Row(
                                            horizontalArrangement = Arrangement.Center,
                                            modifier = Modifier.width(130.dp)
                                        ) {
                                            Text(
                                                text = pets[i].name,
                                                fontSize = 30.sp
                                            )
                                        }
                                        Image(
                                            painter = painterResource(id = MascotaImatges.numDrawables[pets[i].img2]),
                                            contentDescription = pets[i].name,
                                            modifier = Modifier
                                                .height(130.dp)
                                                .border(
                                                    width = 5.dp,
                                                    color = Color.Green
                                                )
                                        )
                                        Box(modifier = Modifier.width(130.dp)) {
                                            Button(onClick = { selectedPet = true; nom = pets[i].name }, modifier = Modifier.width(130.dp)) {
                                                Text(text = "Info")
                                            }
                                        }
                                    } else if (MascotesAconseguides.getNivell(
                                            pets[i].name,
                                            user
                                        ) == 3
                                    ) {
                                        Row(
                                            horizontalArrangement = Arrangement.Center,
                                            modifier = Modifier.width(130.dp)
                                        ) {
                                            Text(
                                                text = pets[i].name,
                                                fontSize = 30.sp
                                            )
                                        }
                                        Image(
                                            painter = painterResource(id = MascotaImatges.numDrawables[pets[i].img3]),
                                            contentDescription = pets[i].name,
                                            modifier = Modifier
                                                .height(130.dp)
                                                .border(
                                                    width = 5.dp,
                                                    color = Color.Green
                                                )
                                        )
                                        Box(modifier = Modifier.width(130.dp)) {
                                            Button(onClick = { selectedPet = true; nom = pets[i].name }, modifier = Modifier.width(130.dp)) {
                                                Text(text = "Info")
                                            }
                                        }
                                    }
                                } else {
                                    if (MascotesAconseguides.getNivell(
                                            pets[i].name,
                                            user
                                        ) == 1
                                    ) {
                                        Row(
                                            horizontalArrangement = Arrangement.Center,
                                            modifier = Modifier.width(130.dp)
                                        ) {
                                            Text(
                                                text = pets[i].name,
                                                fontSize = 30.sp
                                            )
                                        }
                                        Image(
                                            painter = painterResource(id = MascotaImatges.numDrawables[pets[i].img1]),
                                            contentDescription = pets[i].name,
                                            modifier = Modifier
                                                .height(130.dp)
                                        )
                                        Box(modifier = Modifier.width(130.dp)) {
                                            Button(onClick = { selectedPet = true; nom = pets[i].name }, modifier = Modifier.width(130.dp)) {
                                                Text(text = "Info")
                                            }
                                        }
                                    } else if (MascotesAconseguides.getNivell(
                                            pets[i].name,
                                            user
                                        ) == 2
                                    ) {
                                        Row(
                                            horizontalArrangement = Arrangement.Center,
                                            modifier = Modifier.width(130.dp)
                                        ) {
                                            Text(
                                                text = pets[i].name,
                                                fontSize = 30.sp
                                            )
                                        }
                                        Image(
                                            painter = painterResource(id = MascotaImatges.numDrawables[pets[i].img2]),
                                            contentDescription = pets[i].name,
                                            modifier = Modifier.height(130.dp)
                                        )
                                        Box(modifier = Modifier.width(130.dp)) {
                                            Button(onClick = { selectedPet = true; nom = pets[i].name }, modifier = Modifier.width(130.dp)) {
                                                Text(text = "Info")
                                            }
                                        }
                                    } else if (MascotesAconseguides.getNivell(
                                            pets[i].name,
                                            user
                                        ) == 3
                                    ) {
                                        Row(
                                            horizontalArrangement = Arrangement.Center,
                                            modifier = Modifier.width(130.dp)
                                        ) {
                                            Text(
                                                text = pets[i].name,
                                                fontSize = 30.sp
                                            )
                                        }
                                        Image(
                                            painter = painterResource(id = MascotaImatges.numDrawables[pets[i].img3]),
                                            contentDescription = pets[i].name,
                                            modifier = Modifier.height(130.dp)
                                        )
                                        Box(modifier = Modifier.width(130.dp)) {
                                            Button(onClick = { selectedPet = true; nom = pets[i].name }, modifier = Modifier.width(130.dp)) {
                                                Text(text = "Info")
                                            }
                                        }
                                    }
                                }
                            } else {
                                Spacer(modifier = Modifier.height(30.dp))
                                Image(
                                    painter = painterResource(id = MascotaImatges.numDrawables[pets[i].img1l]),
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
                    }
                    if(selectedPet) {
                        InfoPetWidget (MascotesAconseguides.getMascotaAconseguida(nom, user), onDismiss = {selectedPet = false})
                    }
                }
            }
        }
    }*/
}



