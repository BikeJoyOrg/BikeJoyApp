package com.example.bikejoyapp.profile.ui

import android.graphics.Color.rgb
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.material3.Card
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.bikejoyapp.R
import com.example.bikejoyapp.profile.viewmodel.PerfilViewModel
import com.example.bikejoyapp.shop.data.ItemPurchased
import com.example.bikejoyapp.utils.SharedPrefUtils
import com.example.bikejoyapp.utils.MainViewModel
import com.example.bikejoyapp.users.viewmodel.UserViewModel
import java.text.DateFormatSymbols
import java.time.LocalDateTime
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
            },
            properties = DialogProperties(
                usePlatformDefaultWidth = false
            )
        ) {
            Card(
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .padding(16.dp),
                colors = CardDefaults.cardColors(Color(rgb(20, 36, 53)))
            ) {
                Column {
                    Text(
                        text = "Historial de compras",
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier
                            .padding(20.dp)
                            .align(Alignment.CenterHorizontally),
                        color = Color(rgb(85, 149, 180))
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
                                        .fillMaxWidth(),
                                    shape = RectangleShape,
                                    colors = CardDefaults.cardColors(Color(rgb(20, 36, 53)))
                                ) {
                                    Text(
                                        text = monthName,
                                        style = MaterialTheme.typography.titleSmall,
                                        color = Color(rgb(85, 149, 180)),
                                        modifier = Modifier.padding(start = 8.dp, bottom = 2.dp)
                                    )
                                }
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
                            containerColor = Color(rgb(85, 149, 180)),
                            disabledContentColor = Color.Black,
                            disabledContainerColor = Color(rgb(85, 149, 180)),
                        )
                    ) {
                        Icon(Icons.Filled.Close, contentDescription = "Close")
                    }
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
            .padding(5.dp),
        shape = RectangleShape,
        colors = CardDefaults.cardColors(Color(rgb(68, 84, 104)))
    ) {
        Row(
            modifier = Modifier
                .padding(6.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = item.item_title,
                style = MaterialTheme.typography.bodyLarge,
                color = Color(rgb(213, 221, 227))
            )
            Row (
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.item_purchased_price.toString(),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    color = Color(rgb(213, 221, 227))
                )
                Spacer(modifier = Modifier.size(4.dp))
                Icon(
                    painter = painterResource(id = R.drawable.dollar_minimalistic_svgrepo_com),
                    contentDescription = "coin",
                    modifier = Modifier.size(28.dp),
                    tint = Color(0xFFD4AF37)
                )
            }
        }
    }
}