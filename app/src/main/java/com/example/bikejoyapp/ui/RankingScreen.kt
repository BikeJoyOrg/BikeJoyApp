package com.example.bikejoyapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.CardColors
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Button
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.TextField
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment

data class User2(
    val name: String,
    val kilometersDaily: Int,
    val kilometersWeekly: Int,
    val kilometersMonthly: Int,
    val kilometersTotal: Int,
    val routesDaily: Int, // Nuevo atributo
    val routesWeekly: Int, // Nuevo atributo
    val routesMonthly: Int, // Nuevo atributo
    val routesTotal: Int, // Nuevo atributo
    var friends: List<String> = listOf()
)
val users = listOf(
    User2("Usuario1", 24, 93, 3, 90, 93, 5, 65, 56),
    User2("Usuario2", 46, 13, 36, 21, 35, 84, 37, 76),
    User2("Usuario3", 61, 13, 66, 58, 78, 95, 5, 69),
    User2("Usuario4", 75, 79, 47, 32, 19, 51, 54, 26),
    User2("Usuario5", 56, 18, 60, 84, 15, 35, 63, 56),
    User2("Usuario6", 6, 29, 44, 68, 60, 25, 54, 12),
    User2("Usuario7", 70, 76, 79, 96, 84, 10, 21, 70),
    User2("Usuario8", 77, 36, 61, 77, 54, 35, 39, 99),
    User2("Usuario9", 79, 22, 78, 58, 14, 38, 88, 37),
    User2("Usuario10", 88, 17, 14, 94, 98, 24, 19, 64),
    User2("Usuario11", 44, 75, 65, 21, 26, 23, 69, 68),
    User2("Usuario12", 71, 38, 63, 13, 59, 42, 52, 2),
    User2("Usuario13", 6, 70, 51, 64, 28, 35, 70, 17),
    User2("Usuario14", 90, 32, 49, 21, 18, 97, 78, 71),
    User2("Usuario15", 78, 32, 93, 21, 81, 54, 71, 2),
    User2("Usuario16", 65, 95, 38, 91, 2, 47, 13, 68),
    User2("Usuario17", 75, 84, 89, 51, 93, 58, 2, 44),
    User2("Usuario18", 49, 77, 16, 66, 73, 32, 66, 51),
    User2("Usuario19", 9, 85, 12, 62, 1, 93, 61, 47),
    User2("Usuario20", 6, 95, 16, 11, 6, 38, 31, 30),
    User2("Usuario21", 88, 20, 62, 81, 98, 77, 22, 18),
    User2("Usuario22", 52, 52, 28, 22, 14, 89, 57, 64),
    User2("Usuario23", 84, 65, 85, 63, 58, 83, 42, 82),
    User2("Usuario24", 21, 23, 47, 38, 45, 76, 56, 92),
    User2("Usuario25", 20, 98, 56, 92, 14, 71, 50, 24),
    User2("Usuario26", 82, 74, 76, 92, 45, 67, 34, 47),
    User2("Usuario27", 14, 72, 54, 98, 68, 78, 6, 28),
    User2("Usuario28", 40, 63, 90, 22, 1, 3, 55, 30),
    User2("Usuario29", 40, 96, 45, 39, 84, 78, 51, 90),
    User2("Usuario30", 74, 76, 44, 31, 27, 55, 87, 48),
    User2("Usuario31", 34, 77, 76, 49, 14, 96, 2, 100),
    User2("Usuario32", 90, 32, 43, 28, 30, 28, 96, 34),
    User2("Usuario33", 17, 21, 44, 85, 2, 2, 47, 46),
    User2("Usuario34", 1, 38, 89, 16, 83, 54, 44, 41),
    User2("Usuario35", 15, 42, 5, 29, 79, 62, 73, 70),
    User2("Usuario36", 60, 48, 5, 57, 19, 64, 74, 96),
    User2("Usuario37", 93, 86, 77, 90, 13, 72, 40, 80),
    User2("Usuario38", 87, 97, 93, 44, 86, 8, 80, 96),
    User2("Usuario39", 51, 35, 23, 9, 96, 86, 76, 21),
    User2("Usuario40", 19, 29, 29, 97, 51, 44, 14, 57),
    User2("Usuario41", 14, 26, 27, 70, 2, 51, 51, 21),
    User2("Usuario42", 80, 95, 78, 28, 83, 96, 46, 87),
    User2("Usuario43", 1, 29, 17, 11, 49, 89, 60, 53),
    User2("Usuario44", 13, 83, 13, 82, 97, 27, 88, 89),
    User2("Usuario45", 15, 71, 70, 73, 36, 17, 94, 81),
    User2("Usuario46", 45, 83, 83, 40, 76, 9, 10, 91),
    User2("Usuario47", 22, 60, 0, 10, 47, 83, 84, 82),
    User2("Usuario48", 1, 33, 27, 67, 38, 78, 46, 9),
    User2("Usuario49", 83, 89, 84, 99, 19, 24, 31, 5),
    User2("Usuario50", 87, 72, 66, 84, 32, 75, 58, 83),
    User2("Usuario51", 24, 31, 41, 13, 84, 65, 100, 67),
    User2("Usuario52", 91, 85, 39, 60, 85, 20, 15, 7),
    User2("Usuario53", 84, 39, 23, 92, 14, 65, 76, 20),
    User2("Usuario54", 71, 64, 10, 65, 82, 62, 44, 43),
    User2("Usuario55", 88, 41, 6, 50, 36, 70, 95, 98),
    User2("Usuario56", 4, 3, 96, 73, 58, 81, 19, 14),
    User2("Usuario57", 93, 5, 11, 45, 20, 59, 38, 98),
    User2("Usuario58", 84, 57, 32, 34, 51, 85, 19, 58),
    User2("Usuario59", 51, 27, 85, 38, 14, 25, 19, 21),
    User2("Usuario60", 57, 34, 67, 1, 99, 1, 20, 27),
    User2("Usuario61", 18, 10, 52, 11, 54, 76, 15, 36),
    User2("Usuario62", 83, 33, 79, 0, 49, 15, 55, 66),
    User2("Usuario63", 18, 20, 16, 48, 8, 64, 49, 89),
    User2("Usuario64", 27, 33, 46, 14, 75, 36, 59, 2),
    User2("Usuario65", 35, 35, 29, 44, 7, 61, 91, 37),
    User2("Usuario66", 55, 88, 72, 7, 75, 47, 74, 22),
    User2("Usuario67", 50, 93, 26, 97, 63, 48, 48, 96),
    User2("Usuario68", 90, 62, 76, 71, 79, 56, 61, 39),
    User2("Usuario69", 18, 94, 81, 39, 96, 23, 12, 70),
    User2("Usuario70", 11, 1, 41, 98, 42, 91, 49, 49),
    User2("Usuario71", 22, 4, 37, 35, 80, 67, 76, 34),
    User2("Usuario72", 15, 61, 37, 20, 93, 86, 21, 41),
    User2("Usuario73", 99, 1, 5, 37, 58, 13, 51, 19),
    User2("Usuario74", 52, 34, 91, 29, 19, 29, 79, 78),
    User2("Usuario75", 68, 72, 99, 11, 30, 3, 91, 73),
    User2("Usuario76", 85, 71, 65, 59, 45, 11, 28, 80),
    User2("Usuario77", 11, 57, 94, 84, 94, 83, 82, 57),
    User2("Usuario78", 24, 48, 74, 85, 0, 81, 9, 36),
    User2("Usuario79", 26, 52, 9, 20, 52, 46, 17, 33),
    User2("Usuario80", 28, 15, 86, 74, 63, 22, 5, 34),
    User2("Usuario81", 55, 13, 25, 69, 63, 63, 46, 71),
    User2("Usuario82", 53, 9, 21, 33, 96, 69, 29, 56),
    User2("Usuario83", 84, 10, 19, 33, 2, 47, 24, 39),
    User2("Usuario84", 82, 81, 37, 76, 39, 59, 91, 2),
    User2("Usuario85", 22, 3, 25, 98, 65, 74, 73, 23),
    User2("Usuario86", 20, 91, 94, 94, 67, 72, 16, 2),
    User2("Usuario87", 78, 27, 53, 23, 73, 26, 9, 44),
    User2("Usuario88", 26, 50, 1, 16, 82, 59, 54, 69),
    User2("Usuario89", 71, 23, 78, 32, 63, 82, 2, 82),
    User2("Usuario90", 59, 62, 39, 78, 41, 12, 98, 86),
    User2("Usuario91", 50, 96, 40, 37, 40, 72, 91, 32),
    User2("Usuario92", 3, 82, 51, 81, 42, 62, 68, 46),
    User2("Usuario93", 28, 13, 75, 20, 57, 46, 41, 49),
    User2("Usuario94", 64, 61, 88, 81, 55, 98, 34, 33),
    User2("Usuario95", 54, 11, 44, 17, 34, 65, 97, 12),
    User2("Usuario96", 57, 6, 56, 6, 41, 36, 54, 36),
    User2("Usuario97", 3, 63, 76, 21, 72, 36, 37, 42),
    User2("Usuario98", 36, 67, 23, 74, 68, 6, 18, 84),
    User2("Usuario99", 12, 73, 79, 0, 60, 65, 93, 67),
    User2("Usuario100", 31, 19, 10, 34, 96, 36, 19, 61)
)
@Composable
fun RankingScreen() {
    val currentUser = users.first()

    currentUser.friends = listOf("Usuario2", "Usuario3", "Usuario4")

    val friends = users.filter { it.name in currentUser.friends || it.name == currentUser.name }

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
            RankingList(friends, currentUser, selectedOption, showKilometers)
        }
    }
}

@Composable
fun RankingList(users: List<User2>, currentUser: User2, selectedOption: String, showKilometers: Boolean) {
    val sortedUsers = when (selectedOption) {
        "Diario" -> if (showKilometers) users.sortedByDescending { it.kilometersDaily } else users.sortedByDescending { it.routesDaily }
        "Semanal" -> if (showKilometers) users.sortedByDescending { it.kilometersWeekly } else users.sortedByDescending { it.routesWeekly }
        "Mensual" -> if (showKilometers) users.sortedByDescending { it.kilometersMonthly } else users.sortedByDescending { it.routesMonthly }
        else -> if (showKilometers) users.sortedByDescending { it.kilometersTotal } else users.sortedByDescending { it.routesTotal }
    }
    val topUsers = sortedUsers.take(100).toMutableList()
    val currentUserPosition = sortedUsers.indexOf(currentUser) + 1

    if (!topUsers.contains(currentUser)) {
        topUsers.add(currentUser)
    }

    Column(modifier = Modifier.fillMaxHeight()) {
        LazyColumn(modifier = Modifier.weight(1f)) {
            itemsIndexed(topUsers) { index, user ->
                val position = if (user.name == currentUser.name) currentUserPosition else index + 1
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(2.dp),
                    shape = RoundedCornerShape(0.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(if (user.name == currentUser.name) Color.Gray else Color.White)
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "$position. ${user.name}")
                        Text(text = "${when (selectedOption) {
                            "Diario" -> if (showKilometers) user.kilometersDaily else user.routesDaily
                            "Semanal" -> if (showKilometers) user.kilometersWeekly else user.routesWeekly
                            "Mensual" -> if (showKilometers) user.kilometersMonthly else user.routesMonthly
                            else -> if (showKilometers) user.kilometersTotal else user.routesTotal
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
                Text(text = "$currentUserPosition. ${user.name}")
                Text(text = "${when (selectedOption) {
                    "Diario" -> if (showKilometers) user.kilometersDaily else user.routesDaily
                    "Semanal" -> if (showKilometers) user.kilometersWeekly else user.routesWeekly
                    "Mensual" -> if (showKilometers) user.kilometersMonthly else user.routesMonthly
                    else -> if (showKilometers) user.kilometersTotal else user.routesTotal
                }} ${if (showKilometers) "km" else "rutas"}")
            }
        }
    }
}