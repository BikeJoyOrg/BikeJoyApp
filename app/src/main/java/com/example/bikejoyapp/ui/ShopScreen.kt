package com.example.bikejoyapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.bikejoyapp.viewmodel.ShopViewModel

@Composable
fun ShopScreen(shopViewModel: ShopViewModel) {
    val items = shopViewModel.items.value
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Shop")
        Spacer(modifier = Modifier.weight(1f))
        Text(text = items?.size.toString())
    }
}