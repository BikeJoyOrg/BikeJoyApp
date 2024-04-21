package com.example.bikejoyapp.ui

import AchievementViewModel
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.bikejoyapp.R
import com.example.bikejoyapp.data.Achievement
import com.example.bikejoyapp.data.AchievementsIcons.achievementsIcons
import com.example.bikejoyapp.data.Level

val achievements: MutableState<Map<String, Achievement>> = mutableStateOf(
    mapOf(
        "Aventurero" to Achievement(
            name = "Aventurero",
            actualValue = 15,
            levels = arrayOf(
                Level(1, "Viaja un total de 20 km", 20, 50, false, false),
                Level(2, "Viaja un total de 60 km", 60, 200, false, false),
                Level(3, "Viaja un total de 150 km", 150, 200, false, false)
            )
        ),
        "Creador" to Achievement(
            name = "Creador",
            actualValue = 10,
            levels = arrayOf(
                Level(1, "Crea un total de 10 rutas", 10, 200, true, false),
                Level(2, "Crea un total de 25 rutas", 25, 200, false, false),
                Level(3, "Crea un total de 50 rutas", 50, 200, false, false)
            )
        ),
        "Explorador" to Achievement(
            name = "Explorador",
            actualValue = 0,
            levels = arrayOf(
                Level(1, "Explora un total del 15% del mapa", 15, 200, true, true),
                Level(2, "Explora un total del 50% del mapa", 50, 200, true, true),
                Level(3, "Explora un total del 100% del mapa", 100, 200, false, false)
            )
        ),
        "Entusiasta" to Achievement(
            name = "Entusiasta",
            actualValue = 50,
            levels = arrayOf(
                Level(1, "Completa un total de 10 rutas", 10, 200, true, true),
                Level(2, "Completa un total de 25 rutas", 25, 200, true, true),
                Level(3, "Completa un total de 50 rutas", 50, 200, true, true)
            )
        ),
        "Apasionado" to Achievement(
            name = "Apasionado",
            actualValue = 0,
            levels = arrayOf(
                Level(1, "Visita un total de 10 estaciones", 10, 200, false, false),
                Level(2, "Visita un total de 25 estaciones", 25, 200, false, false),
                Level(3, "Visita un total de 50 estaciones", 50, 200, false, false)
            )
        ),
        "Sociable" to Achievement(
            name = "Sociable",
            actualValue = 0,
            levels = arrayOf(
                Level(1, "Haz 10 amigos", 10, 200, false, false),
                Level(2, "Haz 25 amigos", 25, 200, false, false),
                Level(3, "Haz 50 amigos", 50, 200, false, false)
            )
        ),
        "Crítico" to Achievement(
            name = "Crítico",
            actualValue = 0,
            levels = arrayOf(
                Level(1, "Comenta en 5 rutas que hayas completado", 5, 200, false, false),
                Level(2, "Comenta en 15 rutas que hayas completado", 15, 200, false, false),
                Level(3, "Comenta en 30 rutas que hayas completado", 30, 200, false, false)
            )
        ),
        "Navegante" to Achievement(
            name = "Navegante",
            actualValue = 0,
            levels = arrayOf(
                Level(1, "Completa una navegación 20 veces", 20, 200, false, false),
                Level(2, "Completa una navegación 40 veces", 40, 200, false, false),
                Level(3, "Completa una navegación 70 veces", 70, 200, false, false)
            )
        ),
        "Derrochador" to Achievement(
            name = "Derrochador",
            actualValue = 0,
            levels = arrayOf(
                Level(1, "Gasta 100 monedas en la tienda", 100, 200, false, false),
                Level(2, "Gasta 400 monedas en la tienda", 400, 200, false, false),
                Level(3, "Gasta 1000 monedas en la tienda", 1000, 200, false, false)
            )
        ),
        "Criador" to Achievement(
            name = "Criador",
            actualValue = 0,
            levels = arrayOf(
                Level(1, "Sube 1 mascota a su nivel máximo", 1, 200, false, false),
                Level(2, "Sube 4 mascotas a su nivel máximo", 4, 200, false, false),
                Level(3, "Sube 9 mascotas a su nivel máximo", 9, 200, false, false)
            )
        ),
        "Ecologista" to Achievement(
            name = "Ecologista",
            actualValue = 0,
            levels = arrayOf(
                Level(1, "Estalvia un total de 50 de c02", 50, 200, false, false),
                Level(2, "Estalvia un total de 100 de c02", 100, 200, false, false),
                Level(3, "Estalvia un total de 200 de c02", 200, 200, false, false)
            )
        ),
        // Agrega más logros aquí
    )
)

@Composable
fun AchievementScreen(achievementViewModel: AchievementViewModel) {
    //val achievements by achievementViewModel.achievements.observeAsState(emptyList())
    //AchievementList(achievements)
}
/*
@Composable
fun AchievementScreen(achievementsLiveData: LiveData<Map<String, Achievement>>) {
    val achievements by achievementsLiveData.observeAsState(initial = emptyMap())
    AchievementList(achievements = achievements.values.toList())
}*/

@Preview
@Composable
fun AchievementList() {
    LazyColumn {
        itemsIndexed(achievements.value.values.toList()) { index, achievement ->
            AchievementItem(achievement) { levelIndex ->
                claimReward(achievement.name, levelIndex)
            }
        }
    }
}


val Bronze = Color(205, 127, 50)
val Silver = Color(192, 192, 192)
val Gold = Color(255, 223, 0)

@Composable
fun AchievementItem(achievement: Achievement, onRewardClaimed: (Int) -> Unit) {
    var lastAchievedLevel =
        achievement.levels.lastOrNull { it.isAchieved && it.isRedeemed }?.level ?: 0
    val cardColor = when (lastAchievedLevel) {
        1 -> Bronze
        2 -> Silver
        3 -> Gold
        else -> MaterialTheme.colorScheme.surface
    }
    var completed = false

    OutlinedCard(
        colors = CardDefaults.cardColors(
            containerColor = cardColor,
        ),
        border = BorderStroke(2.dp, Color.Black),
        modifier = Modifier
            .fillMaxWidth()
            .size(width = 240.dp, height = 130.dp)
            .padding(bottom = 2.dp, start = 2.dp, end = 2.dp, top = 2.dp),
    ) {
        Row() {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(start = 8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(
                        id = achievementsIcons[achievement.name] ?: R.drawable.coins
                    ),
                    contentDescription = "coins",
                    modifier = Modifier.size(70.dp)
                )
            }
            Column() {
                var textName: String = achievement.name
                for (i in 0..<lastAchievedLevel) {
                    textName += " ★"
                }
                if (lastAchievedLevel == 3) {
                    lastAchievedLevel = 2
                    completed = true
                }
                Row(
                    modifier = Modifier
                        .padding(start = 12.dp, top = 12.dp)
                ) {
                    Text(
                        text = textName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                    )
                }
                Row(
                    modifier = Modifier
                        .padding(top = 12.dp, start = 12.dp)
                        .height(40.dp)
                        .width(200.dp)
                ) {
                    Text(
                        text = achievement.levels[lastAchievedLevel].description,
                        fontSize = 12.sp
                    )
                }
                Row() {
                    Box(
                        modifier = Modifier
                            .padding(top = 8.dp, bottom = 0.dp, start = 12.dp, end = 12.dp)
                    ) {
                        val progress =
                            achievement.actualValue.toFloat() / achievement.levels[lastAchievedLevel].valueRequired.toFloat()
                        LinearProgressIndicator(
                            progress = progress,
                            color = Color(2, 160, 235),
                            modifier = Modifier
                                .width(200.dp)
                                .height(13.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .align(Alignment.Center)
                        )
                        Text(
                            text = "${achievement.actualValue}/${achievement.levels[lastAchievedLevel].valueRequired}",
                            fontSize = 10.sp,
                            modifier = Modifier
                                .align(Alignment.Center)
                        )
                    }
                }
            }
            Column(Modifier.fillMaxSize()) {
                if (!completed) {
                    Row(
                        modifier = Modifier
                            .padding(top = 12.dp, start = 6.dp)
                            .height(40.dp)
                            .width(200.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Image(
                            painterResource(id = R.drawable.coins),
                            contentDescription = "coins",
                            modifier = Modifier
                                .size(40.dp),

                            )
                        Text(achievement.levels[lastAchievedLevel].recompense.toString())


                    }
                    if (achievement.levels[lastAchievedLevel].isAchieved && !achievement.levels[lastAchievedLevel].isRedeemed) {

                        Button(
                            onClick = { onRewardClaimed(lastAchievedLevel) },
                            modifier = Modifier
                                .padding(top = 24.dp)
                                .size(
                                    80.dp,
                                    30.dp
                                ), // Ajusta estos valores para cambiar el tamaño del botón
                            shape = RoundedCornerShape(8.dp), // Ajusta este valor para cambiar el radio de las esquinas redondeadas
                            contentPadding = PaddingValues(0.dp),
                            colors = ButtonDefaults.buttonColors(
                                Color(
                                    40,
                                    210,
                                    10
                                )
                            )// Ajusta este valor para cambiar el radio de las esquinas redondeadas
                        ) {
                            Text("Reclamar")
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.validation),
                            contentDescription = "Validation",
                            modifier = Modifier.size(70.dp)
                        )
                    }
                }
            }
        }
    }
}

fun claimReward(achievementName: String, levelIndex: Int) {
    val updatedAchievements = achievements.value.toMutableMap()
    updatedAchievements[achievementName]?.let { achievement ->
        achievement.levels[levelIndex].isRedeemed = true
    }
    achievements.value = updatedAchievements
}