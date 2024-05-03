package com.example.bikejoyapp.profile.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.bikejoyapp.profile.viewmodel.AchievementViewModel
import com.example.bikejoyapp.utils.MainViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.bikejoyapp.R
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
        val achievementsProgress by achievementViewModel.achievementsProgress.observeAsState(emptyMap())
        val achievementProgress = achievementsProgress[achievementName]
        AchievementItem(achievement = achievement!!,
            achievementProgress = achievementProgress!!,
            onRewardClaimed = { name, levelIndex ->
                achievementViewModel.claimReward(name, levelIndex)
            },
            onAchievementClicked = { })
        val lastAchievedLevel = achievementProgress.lastAchievedLevel

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