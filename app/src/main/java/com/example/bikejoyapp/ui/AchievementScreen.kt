package com.example.bikejoyapp.ui

import com.example.bikejoyapp.viewmodel.AchievementViewModel
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bikejoyapp.R
import com.example.bikejoyapp.data.Achievement
import com.example.bikejoyapp.data.AchievementsIcons.achievementsIcons
import android.media.MediaPlayer


@Composable
fun AchievementScreen(achievementViewModel: AchievementViewModel) {
    val achievements by achievementViewModel.achievements.observeAsState(initial = emptyMap())
    AchievementList(
        achievements = achievements.values.toList(),
        onRewardClaimed = { name, levelIndex ->
            achievementViewModel.claimReward(name, levelIndex)
        })
}

@Composable
fun AchievementList(achievements: List<Achievement>, onRewardClaimed: (String, Int) -> Unit) {
    LazyColumn {
        itemsIndexed(achievements) { _, achievement ->
            AchievementItem(achievement) { levelIndex ->
                onRewardClaimed(achievement.name, levelIndex)
            }
        }
    }
}


val Bronze = Color(205, 127, 50)
val Silver = Color(192, 192, 192)
val Gold = Color(255, 223, 0)

@Composable
fun AchievementItem(achievement: Achievement, onRewardClaimed: (Int) -> Unit) {
    val achievementState = remember { mutableStateOf(achievement) }
    var lastAchievedLevel =
        achievement.levels.lastOrNull { it.isAchieved && it.isRedeemed }?.level ?: 0
    val cardColor = when (lastAchievedLevel) {
        1 -> Bronze
        2 -> Silver
        3 -> Gold
        else -> MaterialTheme.colorScheme.surface
    }
    var completed = false
    //Para que se recargue la card al reclamarla
    val reloadTrigger = remember { mutableIntStateOf(0) }
    val reload = reloadTrigger.intValue
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
                    contentDescription = "achievementIcon",
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
                            achievement.currentValue.toFloat() / achievement.levels[lastAchievedLevel].valueRequired.toFloat()
                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier
                                .width(200.dp)
                                .height(13.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .align(Alignment.Center),
                            color = Color(2, 160, 235),
                        )
                        Text(
                            text = "${achievement.currentValue}/${achievement.levels[lastAchievedLevel].valueRequired}",
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
                            .padding(top = 8.dp, start = 6.dp)
                            .height(40.dp)
                            .width(200.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Image(
                            painterResource(id = R.drawable.coins),
                            contentDescription = "coins",
                            modifier = Modifier
                                .size(30.dp),

                            )
                        Text(achievement.levels[lastAchievedLevel].coinReward.toString(),
                        modifier = Modifier.padding(start = 4.dp))
                    }
                    Row(
                        modifier = Modifier
                            .padding(start = 6.dp)
                            .height(40.dp)
                            .width(200.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Image(
                            painterResource(id = R.drawable.level),
                            contentDescription = "xp",
                            modifier = Modifier
                                .size(30.dp),
                            )
                        Text(achievement.levels[lastAchievedLevel].xpReward.toString(),
                            modifier = Modifier.padding(start = 2.dp))
                    }
                    if (achievement.levels[lastAchievedLevel].isAchieved && !achievement.levels[lastAchievedLevel].isRedeemed) {
                        val context = LocalContext.current
                        Button(
                            onClick = {
                                onRewardClaimed(lastAchievedLevel)
                                //Irrellevant
                                achievementState.value = achievementState.value.copy()
                                reloadTrigger.intValue += 1
                                val mediaPlayer = MediaPlayer.create(context, R.raw.game_reward)
                                mediaPlayer.start()
                            },
                            modifier = Modifier

                                .size(
                                    80.dp,
                                    30.dp
                                ),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(0.dp),
                            colors = ButtonDefaults.buttonColors(
                                Color(
                                    40,
                                    210,
                                    10
                                )
                            )
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
