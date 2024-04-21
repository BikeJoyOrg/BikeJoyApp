package com.example.bikejoyapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bikejoyapp.R
import com.example.bikejoyapp.data.Item
import com.example.bikejoyapp.data.MyAppRoute
import com.example.bikejoyapp.viewmodel.MainViewModel
import com.example.bikejoyapp.viewmodel.ShopViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopScreen(shopViewModel: ShopViewModel, mainViewModel: MainViewModel) {
    LaunchedEffect(Unit) {
        shopViewModel.getStoreData()
    }

    val items = shopViewModel.items.value ?: emptyList()
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
            contentPadding = PaddingValues(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            //if (!pullToRefreshState.isRefreshing) {
                items(items.size) { index ->
                    ItemCard(index = index, items = items) {
                        val route = MyAppRoute.Item.createRoute(it.id.toString())
                        mainViewModel.navigateToDynamic(route)
                    }
                }
            //}
        }
        PullToRefreshContainer(
            modifier = Modifier.align(Alignment.TopCenter),
            state = pullToRefreshState,
        )
    }
}

@Composable
fun ItemCard(index: Int, items: List<Item>, onItemClick: (Item) -> Unit) {
    val item = items[index]
    val imageResId = when (item.item_picture_id) {
        1 -> R.drawable.item_1
        2 -> R.drawable.item_2
        3 -> R.drawable.item_3
        4 -> R.drawable.item_4
        else -> R.drawable.item_default
    }
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { onItemClick(item) },
        elevation = CardDefaults.cardElevation(6.dp),
    ) {
        Box(modifier = Modifier.height(160.dp)) {
            Image(
                painter = painterResource(imageResId),
                contentDescription = "Item Image",
                contentScale = ContentScale.Crop
            )
            Box (modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black
                        ),
                        startY = 250f
                    )
                )
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                Text(
                    text = "${item.game_currency_price} C",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
