package com.example.bikejoyapp.data

import androidx.compose.ui.graphics.vector.ImageVector

data class MyAppTopLevelDestination(
    val route: MyAppRoute,
    val selectedIcon: ImageVector? = null,
    val iconTextId: Int,
    val unselectedIcon: ImageVector? = null,
    val selectedImageResource: Int? = null,
    val unselectedImageResource: Int? = null
)


