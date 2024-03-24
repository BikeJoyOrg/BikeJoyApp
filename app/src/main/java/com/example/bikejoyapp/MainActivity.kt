package com.example.bikejoyapp

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.bikejoyapp.data.MyAppRoute
import com.example.bikejoyapp.viewmodel.EstacionsViewModel
import android.Manifest
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.bikejoyapp.data.EstacioBicing
import com.example.bikejoyapp.ui.EstacioBicingWidget
import com.example.bikejoyapp.viewmodel.MainViewModel
import com.example.bikejoyapp.viewmodel.NavigationCommand
import com.example.bikejoyapp.viewmodel.NavigationViewModel


class MainActivity : ComponentActivity() {

    private val CODIGO_PERMISO_FINE_LOCATION = 101
    private var isPermisos = false
    private fun verificarPermisos() {
        val permiso = ContextCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (permiso == PackageManager.PERMISSION_GRANTED) {
            isPermisos = true
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                CODIGO_PERMISO_FINE_LOCATION
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        verificarPermisos()
        val stationViewModel: EstacionsViewModel by viewModels()
        val mainViewModel: MainViewModel by viewModels()
        val navigationViewModel: NavigationViewModel by viewModels()
        setContent {
            BikeJoyAppTheme {
                val navController = rememberNavController()

                //Observa Flow de comandos de navegación
                LaunchedEffect(Unit) {
                    mainViewModel.navigationCommands.collect { command ->
                        when (command) {
                            is NavigationCommand.ToDestination -> navController.navigate(command.destination.route)
                            is NavigationCommand.ToDynamicDestination -> navController.navigate(command.destination)
                            is NavigationCommand.Back -> navController.popBackStack()
                        }
                    }
                }

                MyAppContent(
                    navController = navController,
                    stationViewModel = stationViewModel,
                    mainViewModel = mainViewModel,
                    navigationViewModel = navigationViewModel
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
    stationViewModel: EstacionsViewModel,
    mainViewModel: MainViewModel,
    navigationViewModel: NavigationViewModel
) {
    val isBottomBarVisible by mainViewModel.isBottomBarVisible.collectAsState()
    val isTopBarVisible by mainViewModel.isTopBarVisible.collectAsState()
    val currentRoute =
        rememberNavController().currentBackStackEntryAsState().value?.destination?.route
    val selectedStation = remember { mutableStateOf<EstacioBicing?>(null) }

    Scaffold(
        topBar = {
            if (isTopBarVisible) {
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
                        IconButton(onClick = { mainViewModel.navigateTo(MyAppRoute.Account) }) {
                            Icon(Icons.Default.AccountCircle, contentDescription = "Account")
                        }
                    })
            }
        },
        bottomBar = {
            if (isBottomBarVisible) {
                MyAppBottomNavigation(
                    navController = navController, currentRoute = currentRoute, mainViewModel = mainViewModel
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            NavHost(
                navController = navController, startDestination = MyAppRoute.Home.route
            ) {
                composable(MyAppRoute.Map.route) {
                    MapScreen(stationViewModel, mainViewModel, navigationViewModel)
                }
                composable(MyAppRoute.Routes.route) {
                    RoutesScreen()
                }
                composable(MyAppRoute.Home.route) {
                    HomeScreen(stationViewModel, innerPadding)
                }
                composable(MyAppRoute.Social.route) {
                    SocialScreen()
                }
                composable(MyAppRoute.Shop.route) {
                    ShopScreen()
                }
                composable(MyAppRoute.Account.route) {
                    AccountScreen()
                }
                composable(
                    route = MyAppRoute.Station.route,
                    arguments = listOf(navArgument("stationId") { type = NavType.StringType })
                ) { backStackEntry ->
                    EstacioBicingWidget(navController = navController, stationViewModel = stationViewModel, mainViewModel = mainViewModel)
                }
            }
        }
    }
}

@Composable
fun MyAppBottomNavigation(
    navController: NavHostController,
    currentRoute: String?,
    mainViewModel: MainViewModel
) {
    NavigationBar(modifier = Modifier.fillMaxWidth()) {
        TOP_LEVEL_DESTINATIONS.forEach { destination ->
            NavigationBarItem(
                selected = currentRoute == destination.route.route,
                onClick = { mainViewModel.navigateTo(destination.route) },
                icon = {
                    Icon(
                        imageVector = destination.selectedIcon,
                        contentDescription = stringResource(id = destination.iconTextId)
                    )
                }
            )
        }
    }
}



