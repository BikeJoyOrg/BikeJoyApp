package com.example.bikejoyapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.bikejoyapp.data.EstacioBicing
import androidx.compose.ui.graphics.Color
import com.example.bikejoyapp.ui.components.EstacioBicingWidget
import com.example.bikejoyapp.viewmodel.EstacionsViewModel
import kotlinx.coroutines.launch
import com.example.bikejoyapp.UserState
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.material3.Card
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import com.example.bikejoyapp.data.MyAppRoute
import com.example.bikejoyapp.data.SharedPrefUtils
import com.example.bikejoyapp.viewmodel.MainViewModel
import kotlinx.coroutines.runBlocking

@Composable
fun HomeScreen(userState: UserState, mainViewModel: MainViewModel) {
    var status by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    var token by remember { mutableStateOf(SharedPrefUtils.getToken()) }


    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Home")

        if(status != "success logout") {
            Button(
                modifier = Modifier.padding(20.dp),
                onClick = {
                    coroutineScope.launch {
                        status = userState.logout()
                    }
                }
            ) {
                Text("Logout")
            }
        }
        Text(text = "Status: $status")
    }
}