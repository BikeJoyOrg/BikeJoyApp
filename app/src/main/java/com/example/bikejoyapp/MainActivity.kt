package com.example.bikejoyapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.bikejoyapp.ui.AccountScreen
import com.example.bikejoyapp.ui.HomeScreen
import com.example.bikejoyapp.ui.MapScreen
import com.example.bikejoyapp.ui.SocialScreen
import com.example.bikejoyapp.ui.ShopScreen
import com.example.bikejoyapp.ui.RoutesScreen
import com.example.bikejoyapp.ui.theme.BikeJoyAppTheme

import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.ui.unit.dp
import com.example.bikejoyapp.data.MyAppRoute
import com.example.bikejoyapp.data.MyAppTopLevelDestination



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BikeJoyAppTheme {
                val navController = rememberNavController()
                val navigateAction = remember(navController) {
                    MyAppNavigationActions(navController)
                }
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val selectedDestination = navBackStackEntry?.destination?.route ?: MyAppRoute.HOME

                MyAppContent(
                    navController = navController,
                    selectedDestination = selectedDestination,
                    navigateTopLevelDestination = navigateAction::navigateTo
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyAppContent(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    selectedDestination: String,
    navigateTopLevelDestination: (MyAppTopLevelDestination) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(text = stringResource(id = R.string.app_name))
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(MyAppRoute.ACCOUNT) }) {
                        Icon(Icons.Default.AccountCircle, contentDescription = "Account")
                    }
                }
            )
        },
        bottomBar = {
            MyAppBottomNavigation(
                selectedDestination = selectedDestination,
                navigateTopLevelDestination = navigateTopLevelDestination
            )
        }
    ) { innerPadding ->
        Column(modifier = modifier.padding(innerPadding).fillMaxSize()) {
            NavHost(
                navController = navController,
                startDestination = MyAppRoute.HOME
            ) {
                composable(MyAppRoute.MAP) {
                    MapScreen()
                }
                composable(MyAppRoute.ROUTES) {
                    RoutesScreen()
                }
                composable(MyAppRoute.HOME) {
                    HomeScreen()
                }
                composable(MyAppRoute.SOCIAL) {
                    SocialScreen()
                }
                composable(MyAppRoute.SHOP) {
                    ShopScreen()
                }
                composable(MyAppRoute.ACCOUNT) {
                    AccountScreen()
                }
            }
        }
    }
}

@Composable
fun MyAppBottomNavigation(
    selectedDestination: String,
    navigateTopLevelDestination: (MyAppTopLevelDestination) -> Unit
) {
    NavigationBar(modifier = Modifier.fillMaxWidth()) {
        TOP_LEVEL_DESTINATIONS.forEach { destinations ->
            NavigationBarItem(
                selected = selectedDestination == destinations.route,
                onClick = { navigateTopLevelDestination(destinations) },
                icon = {
                    Icon(
                        imageVector = destinations.selectedIcon,
                        contentDescription = stringResource(id = destinations.iconTextId)
                    )
                }
            )
        }
    }
}




