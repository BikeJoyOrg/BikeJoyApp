package com.example.bikejoyapp.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.material3.Card
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.window.Dialog
import com.example.bikejoyapp.data.ItemPurchased
import com.example.bikejoyapp.data.LoggedUser
import com.example.bikejoyapp.data.MyAppRoute
import com.example.bikejoyapp.data.SharedPrefUtils
import com.example.bikejoyapp.viewmodel.MainViewModel
import com.example.bikejoyapp.viewmodel.PerfilViewModel
import com.example.bikejoyapp.viewmodel.UserViewModel
import kotlinx.coroutines.runBlocking
import java.text.DateFormatSymbols
import java.time.LocalDateTime
import java.time.Month
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(userViewModel: UserViewModel, mainViewModel: MainViewModel, perfilViewModel: PerfilViewModel) {
    val coroutineScope = rememberCoroutineScope()
    val token by SharedPrefUtils.token.observeAsState()
    LaunchedEffect(true) {
        perfilViewModel.getItemsPurchased()
    }
    val purchasedItems = perfilViewModel.purchasedItems.observeAsState()
    val purchasedItemsMap: Map<Int, List<ItemPurchased>> = purchasedItems.value?.groupBy { item ->
        LocalDateTime.parse(item.date_purchased, DateTimeFormatter.ISO_DATE_TIME).monthValue
    } ?: emptyMap()

    val showPopup = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Home")

        if(token != null) {
            Button(
                modifier = Modifier.padding(20.dp),
                onClick = {
                    coroutineScope.launch {
                        val status = userViewModel.logout(SharedPrefUtils.getToken())
                        println("Status: $status")
                    }
                }
            ) {
                Text("Logout")
            }
        }

        // Botón para abrir el popup
        Button(
            modifier = Modifier.padding(20.dp),
            onClick = {
                showPopup.value = true // Muestra el popup
            }
        ) {
            Text("Ver historial de compras")
        }
    }

    // Definición del popup usando un componente Dialog
    if (showPopup.value) {
        Dialog(
            onDismissRequest = {
                showPopup.value = false // Oculta el popup cuando se cierra
            }
        ) {
            Column(
                modifier = Modifier
                    .size(500.dp, 600.dp)
                    .padding(16.dp)
                    .background(Color.White)
                    .border(1.dp, Color.Gray)
            ) {
                Text(
                    text = "Historial de compras",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier
                        .padding(20.dp)
                        .align(Alignment.CenterHorizontally)
                )

                println("Purchased items: $purchasedItemsMap")

                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    modifier = Modifier.size(500.dp, 430.dp)
                ) {
                    purchasedItemsMap.entries.forEachIndexed { index, (month, purchasesPerMonth) ->
                        stickyHeader {
                            val dfs = DateFormatSymbols(Locale("es", "ES"))
                            val monthName = dfs.months[month - 1].replaceFirstChar {
                                if (it.isLowerCase()) it.titlecase(
                                    Locale("es", "ES")
                                ) else it.toString()
                            }
                            if (index != 0) Spacer(modifier = Modifier.height(10.dp))
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 4.dp),
                                shape = RectangleShape,
                                colors = CardDefaults.cardColors(Color.White)
                            ) {
                                Text(text = monthName, style = MaterialTheme.typography.titleMedium)
                            }
                            HorizontalDivider(color = Color.Black, thickness = 1.dp)
                        }
                        items(purchasesPerMonth.size) {
                            PurchaseEntry(purchasesPerMonth[it])
                        }
                    }
                }
                FilledTonalIconButton(
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.CenterHorizontally)
                        .height(40.dp)
                        .width(60.dp),
                    onClick = {
                        showPopup.value = false
                    },
                    shape = CircleShape,
                    colors = IconButtonColors(
                        contentColor = Color.Black,
                        containerColor = Color(129, 195, 222),
                        disabledContentColor = Color.Black,
                        disabledContainerColor = Color(129, 195, 222),
                    )
                ) {
                    Icon(Icons.Filled.Close, contentDescription = "Close")
                }
            }
        }
    }
}

@Composable
fun PurchaseEntry(item: ItemPurchased) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp),
        shape = RectangleShape,
        colors = CardDefaults.cardColors(Color.White)
    ) {
        Row(
            modifier = Modifier
                .padding(6.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = item.item_title)
            Text(text = "${item.item_purchased_price}€")
        }
    }
}