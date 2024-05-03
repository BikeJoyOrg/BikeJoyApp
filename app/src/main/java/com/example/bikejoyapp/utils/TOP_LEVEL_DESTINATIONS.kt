package com.example.bikejoyapp.utils

import com.example.bikejoyapp.R


val TOP_LEVEL_DESTINATIONS = listOf(
    MyAppTopLevelDestination(
        route = MyAppRoute.Home,
        selectedIcon = R.drawable.stats_icon_color,
        iconTextId = R.string.home,
        unselectedIcon = R.drawable.stats_icon_outlined
    ),
    MyAppTopLevelDestination(
        route = MyAppRoute.Routes,
        selectedIcon = R.drawable.bycicle_filled,
        unselectedIcon = R.drawable.bycicle_outlined,
        iconTextId = R.string.routes,
    ),
    MyAppTopLevelDestination(
        route = MyAppRoute.Map,
        selectedIcon = R.drawable.map_icon_color,
        unselectedIcon = R.drawable.map_icon_outlined,
        iconTextId = R.string.map
    ),
    MyAppTopLevelDestination(
        route = MyAppRoute.Shop,
        selectedIcon = R.drawable.shopping_cart_icon_color,
        iconTextId = R.string.shop,
        unselectedIcon = R.drawable.shopping_cart_icon_outlined
    ),
    MyAppTopLevelDestination(
        route = MyAppRoute.Social,
        selectedIcon = R.drawable.trophy_icon_color,
        iconTextId = R.string.social,
        unselectedIcon = R.drawable.trophy_icon_outlined
    ),

)
