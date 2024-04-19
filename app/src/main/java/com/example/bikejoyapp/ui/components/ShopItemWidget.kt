package com.example.bikejoyapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.bikejoyapp.R
import com.example.bikejoyapp.viewmodel.EstacionsViewModel
import com.example.bikejoyapp.viewmodel.MainViewModel
import com.example.bikejoyapp.viewmodel.ShopViewModel

@Composable
fun ShopItemWidget(
    navController: NavController,
    mainViewModel: MainViewModel,
    shopViewModel: ShopViewModel
) {
    val itemId = remember { mutableStateOf<String?>(null) }
    LaunchedEffect(true) {
        itemId.value = navController.currentBackStackEntry?.arguments?.getString("itemId")
    }
    val item = itemId.let { it.value?.let { it1 -> shopViewModel.getItemById(it1) } }
    val imageResId = when (item?.item_picture_id) {
        0 -> R.drawable.item_0
        1 -> R.drawable.item_1
        2 -> R.drawable.item_2
        3 -> R.drawable.item_3
        else -> R.drawable.item_default
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(top = 16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = item?.title ?: "titol no disponible",
                color = Color.Black,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center),
                textAlign = TextAlign.Center
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        Image(
            painter = painterResource(imageResId),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = item?.description ?: "descripció no disponible",
            color = Color.Black,
            fontSize = 18.sp,
            fontWeight = FontWeight.W500,
            modifier = Modifier.padding(horizontal = 16.dp),
            textAlign = TextAlign.Center
        )
    }
}