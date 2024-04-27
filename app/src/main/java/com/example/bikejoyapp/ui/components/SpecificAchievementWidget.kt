package com.example.bikejoyapp.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.bikejoyapp.data.MyAppRoute
import com.example.bikejoyapp.viewmodel.AchievementViewModel
import com.example.bikejoyapp.viewmodel.MainViewModel
import com.example.bikejoyapp.ui.AchievementItem
import com.example.bikejoyapp.viewmodel.ShopViewModel
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
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
import androidx.compose.foundation.clickable
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider

@Composable
fun SpecificAchievementWidget(
    navController: NavController,
    mainViewModel: MainViewModel,
    achievementViewModel: AchievementViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val achievementName =
            remember { navController.currentBackStackEntry?.arguments?.getString("achievementName") }
        val achievement = achievementName?.let { achievementViewModel.getAchievementByName(it) }
        AchievementItem(achievement = achievement!!,
            onRewardClaimed = { name, levelIndex ->
                achievementViewModel.claimReward(name, levelIndex)
            },
            onAchievementClicked = { })
        val lastAchievedLevel =
            achievement.levels.lastOrNull { it.isAchieved && it.isRedeemed }?.level ?: 0

        Row(modifier = Modifier.padding(top = 6.dp)) {
            var star: Int
            var coin: Int
            var xp: Int
            for (i in 0..<3) {
                if (lastAchievedLevel > i) {
                    star = R.drawable.star_on
                    coin = R.drawable.coins
                    xp = R.drawable.level
                } else {
                    star =R.drawable.star_off
                    coin = R.drawable.coins_off
                    xp = R.drawable.level_off
                }

                Column() {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = star),
                            contentDescription = null,
                            modifier = Modifier.size(70.dp)
                        )
                        if (i != 2) {
                            HorizontalDivider(
                                color = Color.Gray,
                                thickness = 2.dp,
                                modifier = Modifier
                                    .size(width = 50.dp, height = 0.dp)
                                    .padding(horizontal = 6.dp),
                            )
                        }
                    }
                    Row() {
                        Image(
                            painter = painterResource(id = coin),
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(achievement.levels[minOf(2, i)].coinReward.toString())
                    }
                    Row() {
                        Image(
                            painter = painterResource(id = xp),
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(achievement.levels[minOf(2, i)].xpReward.toString())
                    }
                }
            }
        }


    }
}