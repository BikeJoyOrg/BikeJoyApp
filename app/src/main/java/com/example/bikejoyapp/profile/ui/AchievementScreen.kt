package com.example.bikejoyapp.profile.ui

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
import com.example.bikejoyapp.profile.data.Achievement
import com.example.bikejoyapp.profile.data.AchievementsIcons.achievementsIcons
import android.media.MediaPlayer
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.ui.unit.Dp
import com.example.bikejoyapp.profile.data.AchievementProgress
import com.example.bikejoyapp.utils.MyAppRoute
import com.example.bikejoyapp.profile.viewmodel.AchievementViewModel
import com.example.bikejoyapp.theme.Bronze
import com.example.bikejoyapp.theme.Gold
import com.example.bikejoyapp.theme.Silver
import com.example.bikejoyapp.utils.MainViewModel

@Composable
fun AchievementScreen(achievementViewModel: AchievementViewModel, mainViewModel: MainViewModel) {
    val achievements by achievementViewModel.achievements.observeAsState(emptyList())
    val achievementsProgress by achievementViewModel.achievementsProgress.observeAsState(emptyMap())
    AchievementList(
        achievements = achievements,
        achievementsProgress = achievementsProgress,
        onRewardClaimed = { name, levelIndex ->
            achievementViewModel.claimReward(name, levelIndex)
        },
        onAchievementClicked = { name ->
            val route = MyAppRoute.Achievement.createRoute(name)
            mainViewModel.navigateToDynamic(route)
        })
}

@Composable
fun AchievementList(
    achievements: List<Achievement>,
    achievementsProgress: Map<String, AchievementProgress>,
    onRewardClaimed: (String, Int) -> Unit,
    onAchievementClicked: (String) -> Unit
) {
    LazyColumn {
        itemsIndexed(achievements) { _, achievement ->
            achievementsProgress[achievement.name]?.let {
                AchievementItem(achievement,
                    it,onRewardClaimed, onAchievementClicked)
            }
        }
    }
}

@Composable
fun AchievementItem(
    achievement: Achievement,
    achievementProgress: AchievementProgress,
    onRewardClaimed: (String, Int) -> Unit,
    onAchievementClicked: (String) -> Unit
) {
    val achievementState = remember { mutableStateOf(achievement) }
    var lastAchievedLevel = achievementProgress.lastAchievedLevel
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
            .padding(bottom = 2.dp, start = 2.dp, end = 2.dp, top = 2.dp)
            .clickable {
                onAchievementClicked(achievement.name)
            },
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
                for (i in 0..<achievementProgress.lastAchievedLevel) {
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
                            achievementProgress.currentValue.toFloat() / achievement.levels[lastAchievedLevel].valueRequired.toFloat()
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
                            text = "${achievementProgress.currentValue}/${achievement.levels[lastAchievedLevel].valueRequired}",
                            fontSize = 10.sp,
                            modifier = Modifier
                                .align(Alignment.Center)
                        )
                    }
                }
            }
            Column(
                Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally
            ) {
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
                        Text(
                            achievement.levels[lastAchievedLevel].coinReward.toString(),
                            modifier = Modifier.padding(start = 4.dp)
                        )
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
                        Text(
                            achievement.levels[lastAchievedLevel].xpReward.toString(),
                            modifier = Modifier.padding(start = 2.dp)
                        )
                    }
                    if (achievementProgress.isAchieved && !achievementProgress.isRedeemed) {
                        val context = LocalContext.current
                        val transition = rememberInfiniteTransition(label = "")
                        val widthAnimated by transition.animateValue(
                            initialValue = 70.dp,
                            targetValue = 80.dp,
                            typeConverter = Dp.VectorConverter,
                            animationSpec = infiniteRepeatable(
                                animation = tween(500), repeatMode = RepeatMode.Reverse
                            ), label = ""
                        )
                        val heightAnimated by transition.animateValue(
                            initialValue = 25.dp,
                            targetValue = 30.dp,
                            typeConverter = Dp.VectorConverter,
                            animationSpec = infiniteRepeatable(
                                animation = tween(500), repeatMode = RepeatMode.Reverse
                            ), label = ""
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Button(
                                onClick = {
                                    onRewardClaimed(achievement.name, lastAchievedLevel)
                                    //Irrellevanta
                                    achievementState.value = achievementState.value.copy()
                                    reloadTrigger.intValue += 1
                                    val mediaPlayer = MediaPlayer.create(context, R.raw.game_reward)
                                    mediaPlayer.start()
                                },
                                modifier = Modifier

                                    .size(
                                        widthAnimated,
                                        heightAnimated
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