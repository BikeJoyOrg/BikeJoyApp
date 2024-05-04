package com.example.bikejoyapp.ranking.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Card
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.TextField
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.livedata.observeAsState
import com.example.bikejoyapp.ranking.viewmodel.RankingViewModel
import com.example.bikejoyapp.users.data.LoggedUser
import com.example.bikejoyapp.users.data.User

@Composable
fun RankingScreen(rankingViewModel: RankingViewModel) {
    val users by rankingViewModel.users.observeAsState(emptyList())
    val currentUser = LoggedUser.getLoggedUser()

    //currentUser.friends = listOf("Usuario2", "Usuario3", "Usuario4")

    //val friends = users.filter { it.name in currentUser.friends || it.name == currentUser.name }

    var selectedTabIndex by remember { mutableStateOf(0) }
    var selectedOption by remember { mutableStateOf("Diario") } // Nuevo estado para rastrear la selección del usuario
    val options = listOf("Diario", "Semanal", "Mensual", "Total")

    // Nuevo estado para rastrear si se deben mostrar los kilómetros o las rutas
    var showKilometers by remember { mutableStateOf(true) }
    val showOptions = listOf("Kilómetros", "Rutas")
    var selectedShowOption by remember { mutableStateOf(showOptions.first()) }

    Column(modifier = Modifier.fillMaxHeight()) {
        Row(Modifier.fillMaxWidth()) {
            Box(modifier = Modifier.weight(1f)) {
                var expanded by remember { mutableStateOf(false) }
                TextField(
                    value = selectedShowOption,
                    onValueChange = { selectedShowOption = it },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { expanded = true }) {
                            Icon(Icons.Filled.ArrowDropDown, contentDescription = "Desplegar menú")
                        }
                    }
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    showOptions.forEach { option ->
                        DropdownMenuItem(onClick = {
                            selectedShowOption = option
                            showKilometers = option == "Kilómetros"
                            expanded = false
                        }, text = { Text(text = option) })
                    }
                }
            }

            Box(modifier = Modifier.weight(1f)) {
                var expanded by remember { mutableStateOf(false) }
                TextField(
                    value = selectedOption,
                    onValueChange = { selectedOption = it },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { expanded = true }) {
                            Icon(Icons.Filled.ArrowDropDown, contentDescription = "Desplegar menú")
                        }
                    }
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    options.forEach { option ->
                        DropdownMenuItem(onClick = {
                            selectedOption = option
                            expanded = false
                        }, text = { Text(text = option) })
                    }
                }
            }
        }

        TabRow(selectedTabIndex = selectedTabIndex) {
            Tab(selected = selectedTabIndex == 0, onClick = { selectedTabIndex = 0 }) {
                Text(text = "Global", fontSize = 24.sp)
            }
            Tab(selected = selectedTabIndex == 1, onClick = { selectedTabIndex = 1 }) {
                Text(text = "Amigos", fontSize = 24.sp)
            }
        }

        if (selectedTabIndex == 0) {
            RankingList(users, currentUser, selectedOption, showKilometers)
        } else {
            RankingList(users, currentUser, selectedOption, showKilometers)
        }
    }
}

@Composable
fun RankingList(users: List<User>, currentUser: User?, selectedOption: String, showKilometers: Boolean) {
    val sortedUsers = when (selectedOption) {
        "Diario" -> if (showKilometers) users.sortedByDescending { it.dailyDistance } else users.sortedByDescending { it.dailyCompletedRoutes }
        "Semanal" -> if (showKilometers) users.sortedByDescending { it.weeklyDistance } else users.sortedByDescending { it.weeklyCompletedRoutes }
        "Mensual" -> if (showKilometers) users.sortedByDescending { it.monthlyDistance } else users.sortedByDescending { it.monthlyCompletedRoutes }
        else -> if (showKilometers) users.sortedByDescending { it.distance } else users.sortedByDescending { it.completed_routes }
    }
    val currentUserPosition = sortedUsers.indexOfFirst { it.username == currentUser?.username } + 1
    val topUsers = sortedUsers.take(100).toMutableList()

    if (currentUserPosition > 100 && currentUser != null) {
        topUsers.add(currentUser)
    }

    Column(modifier = Modifier.fillMaxHeight()) {
        LazyColumn(modifier = Modifier.weight(1f)) {
            itemsIndexed(topUsers) { index, user ->
                val position = if (user.username == currentUser?.username) currentUserPosition else index + 1
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(2.dp),
                    shape = RoundedCornerShape(0.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(if (user.username == currentUser?.username) Color.Gray else Color.White)
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "$position. ${user.username}")
                        Text(text = "${when (selectedOption) {
                            "Diario" -> if (showKilometers) user.dailyDistance else user.dailyCompletedRoutes
                            "Semanal" -> if (showKilometers) user.weeklyDistance else user.weeklyCompletedRoutes
                            "Mensual" -> if (showKilometers) user.monthlyDistance else user.monthlyCompletedRoutes
                            else -> if (showKilometers) user.distance else user.completed_routes
                        }} ${if (showKilometers) "km" else "rutas"}")
                    }
                }
            }
        }
        val user = currentUser
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(0.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.LightGray)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "$currentUserPosition. ${user?.username}")
                Text(text = "${when (selectedOption) {
                    "Diario" -> if (showKilometers) user?.dailyDistance else user?.dailyCompletedRoutes
                    "Semanal" -> if (showKilometers) user?.weeklyDistance else user?.weeklyCompletedRoutes
                    "Mensual" -> if (showKilometers) user?.monthlyDistance else user?.monthlyCompletedRoutes
                    else -> if (showKilometers) user?.distance else user?.completed_routes
                }} ${if (showKilometers) "km" else "rutas"}")
            }
        }
    }
}