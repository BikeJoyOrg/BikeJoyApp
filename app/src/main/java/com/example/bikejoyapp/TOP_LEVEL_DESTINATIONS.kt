package com.example.bikejoyapp

import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.core.content.ContextCompat
import com.example.bikejoyapp.data.MyAppRoute
import com.example.bikejoyapp.data.MyAppTopLevelDestination


val TOP_LEVEL_DESTINATIONS = listOf(
    MyAppTopLevelDestination(
        route = MyAppRoute.Map,
        selectedIcon = Icons.Filled.Place,
        unselectedIcon = Icons.Outlined.Place,
        iconTextId = R.string.map
    ),
    MyAppTopLevelDestination(
        route = MyAppRoute.Routes,
        selectedIcon = Icons.AutoMirrored.Filled.Send,
        unselectedIcon = Icons.AutoMirrored.Outlined.Send,
        iconTextId = R.string.routes,
    ),
    MyAppTopLevelDestination(
        route = MyAppRoute.Home,
        selectedIcon = Icons.Filled.Home,
        iconTextId = R.string.home,
        unselectedIcon = Icons.Outlined.Home
    ),
    MyAppTopLevelDestination(
        route = MyAppRoute.Social,
        selectedImageResource = R.drawable.baseline_people_24,
        iconTextId = R.string.social,
        unselectedImageResource = R.drawable.outline_people_outline_24
    ),
    MyAppTopLevelDestination(
        route = MyAppRoute.Shop,
        selectedIcon = Icons.Filled.ShoppingCart,
        iconTextId = R.string.shop,
        unselectedIcon = Icons.Outlined.ShoppingCart
    ),
)
