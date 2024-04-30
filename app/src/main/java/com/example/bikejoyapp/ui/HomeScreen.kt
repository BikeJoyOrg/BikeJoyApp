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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
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
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.CenterHorizontally)
                )

                println("Purchased items: $purchasedItemsMap")

                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.size(500.dp, 420.dp)
                ) {
                    purchasedItemsMap.forEach { (month, purchasesPerMonth) ->
                        stickyHeader {
                            val dfs = DateFormatSymbols(Locale("es", "ES"))
                            val monthName = dfs.months[month - 1].replaceFirstChar {
                                if (it.isLowerCase()) it.titlecase(
                                    Locale("es", "ES")
                                ) else it.toString()
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = monthName, style = MaterialTheme.typography.titleMedium)
                        }
                        items(purchasesPerMonth.size) {
                            PurchaseEntry(purchasesPerMonth[it])
                        }
                    }
                }
                Button(
                    modifier = Modifier.padding(16.dp),
                    onClick = {
                        showPopup.value = false
                    }
                ) {
                    Text("Cerrar")
                }
            }
        }
    }
}

@Composable
fun PurchaseEntry(item: ItemPurchased) {
    Card(
        modifier = Modifier
            .fillMaxWidth().padding(0.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = item.item_title)
            Text(text = "${item.item_purchased_price}€")
        }
    }
}