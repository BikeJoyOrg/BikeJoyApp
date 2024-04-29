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
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.tooling.preview.Preview


@Preview
@Composable
fun ProfileScreen() {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Box(modifier = Modifier
                .padding(top = 16.dp)
                .size(100.dp)) {
                Canvas(modifier = Modifier.matchParentSize()) {
                    val purple = Color(206, 194, 242)
                    drawCircle(color = purple)
                }
                Image(
                    painter = painterResource(id = R.drawable.avatar_death),
                    contentDescription = "Profile picture",
                    modifier = Modifier
                        .size(70.dp)
                        .align(Alignment.Center) // Esto hace que la imagen sea redonda
                )
            }
        }
        val username = "Username"
        Text(
            text = username,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 16.dp), // Agrega padding superior
            fontSize = 24.sp // Aumenta el tamaño de la letra
        )
        val level = "LEVEL X"
        Text(level, modifier = Modifier.align(Alignment.CenterHorizontally))
    }
}