package com.example.bikejoyapp

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.bikejoyapp.data.MyAppRoute
import com.example.bikejoyapp.data.MyAppTopLevelDestination

class MyAppNavigationActions(private val navController: NavHostController) {
    fun navigateTo(destination: MyAppTopLevelDestination) {
        navController.navigate(destination.route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
        }
    }
}


val TOP_LEVEL_DESTINATIONS = listOf(
    MyAppTopLevelDestination(
        route = MyAppRoute.MAP,
        selectedIcon = Icons.Default.Place,
        iconTextId = R.string.map
    ),
    MyAppTopLevelDestination(
        route = MyAppRoute.ROUTES,
        selectedIcon = Icons.Default.Send,
        iconTextId = R.string.routes
    ),
    MyAppTopLevelDestination(
        route = MyAppRoute.HOME,
        selectedIcon = Icons.Default.Home,
        iconTextId = R.string.home
    ),
    MyAppTopLevelDestination(
        route = MyAppRoute.SOCIAL,
        selectedIcon = Icons.Default.Person,
        iconTextId = R.string.social
    ),
    MyAppTopLevelDestination(
        route = MyAppRoute.SHOP,
        selectedIcon = Icons.Default.ShoppingCart,
        iconTextId = R.string.shop
    ),
)

