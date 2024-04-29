package com.example.bikejoyapp.ui.components

import android.graphics.Color.rgb
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.Group
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.bikejoyapp.R
import com.example.bikejoyapp.data.LoggedUser
import com.example.bikejoyapp.data.MyAppRoute
import com.example.bikejoyapp.viewmodel.MainViewModel
import com.example.bikejoyapp.viewmodel.ShopViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun ShopItemWidget(
    navController: NavController,
    mainViewModel: MainViewModel,
    shopViewModel: ShopViewModel
) {
    shopViewModel.getStoreData()
    val coroutineScope = rememberCoroutineScope()
    val itemId = remember { mutableStateOf<String?>(null) }
    LaunchedEffect(true) { itemId.value = navController.currentBackStackEntry?.arguments?.getString("itemId") }
    val item = itemId.let { it.value?.let { it1 -> shopViewModel.getItemById(it1.toInt()) } }


    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = item?.title ?: "titol no disponible",
            color = Color.Black,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(item?.image ?: "https://pes-bikejoy.s3.amazonaws.com/items/default.jpg")
                .crossfade(true)
                .build(),
            contentDescription = "Item Image",
            modifier = Modifier.fillMaxWidth().height(250.dp),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = item?.description ?: "descripció no disponible",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(horizontal = 16.dp),
            textAlign = TextAlign.Center
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Preu:",
                style = MaterialTheme.typography.labelMedium,
                textAlign = TextAlign.Center
            )
            Row (
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item?.game_currency_price.toString(),
                    style = MaterialTheme.typography.labelMedium,
                    textAlign = TextAlign.Center
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
        Spacer(modifier = Modifier.weight(1f)) // This will push the button to the bottom
        Column(
            modifier = Modifier
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ElevatedButton(
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 2.dp,
                    pressedElevation = 8.dp,
                    disabledElevation = 0.dp,
                    hoveredElevation = 4.dp,
                    focusedElevation = 4.dp
                ),
                onClick = {
                    coroutineScope.launch {
                        if (LoggedUser.isLoggedIn()) {
                            shopViewModel.buyItem(item?.id ?: 0)
                        }
                        shopViewModel.getStoreData()
                        mainViewModel.navigateToDynamic("Shop")
                    }
                },
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 16.dp)
                    .clip(RoundedCornerShape(50)),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color(rgb(235, 235, 235)),
                        contentColor = Color.Black
                    ),
                contentPadding = PaddingValues(
                    horizontal = 24.dp,
                    vertical = 10.dp
                ),
            ) {
                Text(
                    text = "Comprar",
                    style = MaterialTheme.typography.labelMedium,
                )
            }
        }
    }
}