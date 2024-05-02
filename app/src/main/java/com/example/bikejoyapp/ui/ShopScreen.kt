package com.example.bikejoyapp.ui

import android.content.Context
import android.graphics.Color.rgb
import android.graphics.drawable.BitmapDrawable
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.palette.graphics.Palette
import coil.Coil
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.bikejoyapp.R
import com.example.bikejoyapp.data.Item
import com.example.bikejoyapp.viewmodel.ShopViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

suspend fun getPaletteFromURL(context: Context, url: String): Palette {
    return withContext(Dispatchers.IO) {
        val request = ImageRequest.Builder(context)
            .data(url)
            .allowHardware(false)
            .build()
        val result = (Coil.imageLoader(context).execute(request).drawable as? BitmapDrawable)?.bitmap
        result?.let { Palette.from(it).generate() }!!
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopScreen(shopViewModel: ShopViewModel) {
    LaunchedEffect(Unit) {
        shopViewModel.getStoreData()
    }
    var showSnackbar by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    var snackbarMessage by remember { mutableStateOf("") }

    val items = shopViewModel.items.value ?: emptyList()
    val showDialog = remember { mutableStateOf(false) }
    val selectedItem = remember { mutableStateOf<Item?>(null) }
    val pullToRefreshState = rememberPullToRefreshState()
    if (pullToRefreshState.isRefreshing) {
        LaunchedEffect(Unit) {
            shopViewModel.getStoreData()
            delay(1500)
            pullToRefreshState.endRefresh()
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(pullToRefreshState.nestedScrollConnection))
    {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(top = 12.dp, start = 4.dp, end = 4.dp, bottom = 4.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(items.size) { index ->
                ItemCard(index = index, items = items) { item ->
                    selectedItem.value = item
                    showDialog.value = true
                }
            }
        }
        PullToRefreshContainer(
            modifier = Modifier.align(Alignment.TopCenter),
            state = pullToRefreshState,
        )
        if (showSnackbar) {
            Snackbar(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(6.dp),
                containerColor = Color.White,
                contentColor = Color.Black,
                shape = RoundedCornerShape(10.dp),
            ) {
                Text(snackbarMessage)
            }
        }
    }
    if (showDialog.value) {
        selectedItem.value?.let {
            ItemDialog(
                item = it,
                onDismiss = { showDialog.value = false },
                onBuy = {
                    coroutineScope.launch {
                        snackbarMessage = shopViewModel.buyItem(it.id)
                        showSnackbar = true
                        delay(2000)
                        showSnackbar = false

                    }
                    showDialog.value = false
                }
            )
        }
    }
}

@Composable
fun ItemDialog(item: Item, onDismiss: () -> Unit, onBuy: () -> Unit) {
    var dominantColor by remember { mutableStateOf(Color.White) }
    var lightColor by remember { mutableStateOf(Color.White) }
    val context = LocalContext.current

    LaunchedEffect(item.image) {
        lightColor = Color(
            getPaletteFromURL(
                context,
                item.image ?: "https://pes-bikejoy.s3.amazonaws.com/items/default.jpg"
            ).lightMutedSwatch?.rgb ?: Color.White.toArgb())
        dominantColor = Color(
            getPaletteFromURL(
                context,
                item.image ?: "https://pes-bikejoy.s3.amazonaws.com/items/default.jpg"
            ).mutedSwatch?.rgb ?: Color.White.toArgb())
    }
    Dialog(onDismissRequest = onDismiss) {
        OutlinedCard(
            modifier = Modifier
                .fillMaxWidth()
                .height(550.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardColors(
                containerColor = lightColor,
                contentColor = Color.Black,
                disabledContainerColor = lightColor,
                disabledContentColor = Color.Black,
            ),
            elevation = CardDefaults.cardElevation(6.dp),
            border = BorderStroke(6.dp, lightColor)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier.padding(top = 16.dp),
                    text = item.title,
                    color = Color.Black,
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center
                )
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(item.image ?: "https://pes-bikejoy.s3.amazonaws.com/items/default.jpg")
                        .crossfade(true)
                        .build(),
                    contentDescription = "Item Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = item.description,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
                ElevatedButton(
                    onClick = onBuy,
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.CenterHorizontally)
                        .height(45.dp)
                        .width(100.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = dominantColor,
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(30.dp)
                ) {
                    Row (
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = item.game_currency_price.toString(),
                            style = MaterialTheme.typography.labelMedium,
                        )
                        Spacer(modifier = Modifier.size(4.dp))
                        Icon(
                            painter = painterResource(id = R.drawable.dollar_minimalistic_svgrepo_com),
                            contentDescription = "coin",
                            modifier = Modifier.size(32.dp),
                            tint = Color(0xFFD4AF37)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ItemCard(index: Int, items: List<Item>, onItemClick: (Item) -> Unit) {
    val item = items[index]
    OutlinedCard(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth()
            .height(200.dp)
            .clickable { onItemClick(item) },
        elevation = CardDefaults.cardElevation(6.dp),
        border = BorderStroke(2.dp, Color.Black),
        colors = CardColors(
            containerColor = Color(rgb(240, 234, 220)),
            contentColor = Color.Black,
            disabledContainerColor = Color(rgb(240, 234, 220)),
            disabledContentColor = Color.Black,
        ),
        shape = RoundedCornerShape(20.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                OutlinedCard(
                    modifier = Modifier
                        .padding(8.dp, 8.dp, 8.dp, bottom = 0.dp)
                        .fillMaxWidth()
                        .height(150.dp),
                    border = BorderStroke(1.dp, Color.Gray)
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(
                                item.image
                                    ?: "https://pes-bikejoy.s3.amazonaws.com/items/default.jpg"
                            )
                            .crossfade(true)
                            .build(),
                        contentDescription = "Item Image",
                        modifier = Modifier
                            .height(150.dp)
                            .fillMaxWidth(),
                        contentScale = ContentScale.Crop
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Text(
                        text = item.game_currency_price.toString(),
                        style = MaterialTheme.typography.labelMedium,
                    )
                    Spacer(modifier = Modifier.size(4.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.dollar_minimalistic_svgrepo_com),
                        contentDescription = "coin",
                        modifier = Modifier.size(32.dp),
                        tint = Color(0xFFD4AF37)
                    )
                }
            }
            if (item.stock_number < 20) {
                Card(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(14.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardColors(
                        containerColor = Color.Red,
                        contentColor = Color.White,
                        disabledContainerColor = Color.Red,
                        disabledContentColor = Color.White,
                    ),
                ) {
                    Text(
                        text = "${item.stock_number} restants",
                        modifier = Modifier.padding(4.dp),
                        color = Color.White,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}
