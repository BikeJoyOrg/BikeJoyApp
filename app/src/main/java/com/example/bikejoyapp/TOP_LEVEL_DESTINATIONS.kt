package com.example.bikejoyapp

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.ShoppingCart
import com.example.bikejoyapp.data.MyAppRoute
import com.example.bikejoyapp.data.MyAppTopLevelDestination


val TOP_LEVEL_DESTINATIONS = listOf(
    MyAppTopLevelDestination(
        route = MyAppRoute.Map,
        selectedIcon = Icons.Default.Place,
        iconTextId = R.string.map
    ),
    MyAppTopLevelDestination(
        route = MyAppRoute.Routes,
        selectedIcon = Icons.AutoMirrored.Filled.Send,
        iconTextId = R.string.routes
    ),
    MyAppTopLevelDestination(
        route = MyAppRoute.Home,
        selectedIcon = Icons.Default.Home,
        iconTextId = R.string.home
    ),
    MyAppTopLevelDestination(
        route = MyAppRoute.Social,
        selectedIcon = Icons.Default.Person,
        iconTextId = R.string.social
    ),
    MyAppTopLevelDestination(
        route = MyAppRoute.Shop,
        selectedIcon = Icons.Default.ShoppingCart,
        iconTextId = R.string.shop
    ),

)

