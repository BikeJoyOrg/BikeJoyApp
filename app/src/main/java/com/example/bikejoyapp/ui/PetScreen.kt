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
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalContext
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
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.bikejoyapp.data.MascotaAconseguida
import org.mockito.AdditionalMatchers.and
import org.mockito.internal.matchers.Null


var petG: Mascota = Mascota("G","G","G","G","G","G","G","G","G",1.0f,1.0f,1.0f)
var petA: MascotaAconseguida = MascotaAconseguida(0,0,false,"A",0)

@Composable
fun PetScreen(mascotesViewModel: MascotesViewModel, mainViewModel: MainViewModel) {
    val pets = mascotesViewModel.pets.value ?: emptyList()
    val petsAconseguides = mascotesViewModel.petsAconseguides.value ?: emptyList()
    if (petsAconseguides.isEmpty()) {
        println("vacia")
    } else {
        for (mascotaAconseguida in petsAconseguides) {
            println(mascotaAconseguida.nomMascota)
        }
    }
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
            val mascotaBuscada = petsAconseguides.find { it.nomMascota == pet.name }

            Card(
                modifier = Modifier.fillMaxWidth()
                    .width(230.dp)
                    .padding(10.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                ),
                onClick = {
                    if (mascotaBuscada != null) {
                        if (mascotaBuscada.nivell != 0) { selectedPet = true; petA = mascotaBuscada; petG = pet }
                    }
                }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (mascotaBuscada != null) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(when (mascotaBuscada.nivell) {
                                    0 -> pet.imgEgg
                                    1 -> pet.img1
                                    2 -> pet.img2
                                    else -> pet.img3
                                })
                                .build(),
                            contentDescription = pet.name,
                            modifier = Modifier
                                .height(130.dp)
                        )
                    }
                    else {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(pet.imgEggl)
                                .build(),
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
                    if (mascotaBuscada != null) {
                        if (mascotaBuscada.nivell != 0) {
                            if (mascotaBuscada.equipada) {
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
                        } else if (mascotaBuscada.nivell == 0) {
                            Text(
                                text = "Egg",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    else {
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
        InfoPetWidget(
            petA,
            petG,
            mascotesViewModel,
            onDismiss = { selectedPet = false })
    }
}



