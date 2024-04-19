package com.example.bikejoyapp

import com.example.bikejoyapp.viewmodel.GravarRutaViewModel
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.bikejoyapp.data.MyAppRoute
import com.example.bikejoyapp.ui.components.EstacioBicingWidget
import com.example.bikejoyapp.ui.HomeScreen
import com.example.bikejoyapp.ui.MapScreen
import com.example.bikejoyapp.ui.RoutesScreen
import com.example.bikejoyapp.ui.ShopScreen
import com.example.bikejoyapp.ui.SocialScreen
import com.example.bikejoyapp.ui.theme.BikeJoyAppTheme
import com.example.bikejoyapp.viewmodel.EstacionsViewModel
import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color.rgb
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import com.example.bikejoyapp.ui.GravarRutaScreen
import com.example.bikejoyapp.ui.PetScreen
import com.example.bikejoyapp.ui.components.ShopItemWidget
import com.example.bikejoyapp.viewmodel.MainViewModel
import com.example.bikejoyapp.viewmodel.NavigationCommand
import com.example.bikejoyapp.viewmodel.NavigationViewModel
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import com.example.bikejoyapp.viewmodel.RoutesViewModel
import com.example.bikejoyapp.viewmodel.ShopViewModel


class MainActivity : ComponentActivity() {
    private lateinit var placesClient: PlacesClient

    private val navigationViewModel: NavigationViewModel by viewModels {
        NavigationViewModel.Factory(placesClient)
    }
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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        verificarPermisos()
        val stationViewModel: EstacionsViewModel by viewModels()
        val mainViewModel: MainViewModel by viewModels()
        val shopViewModel: ShopViewModel by viewModels()


        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, getString(R.string.google_maps_key))
        }
        placesClient = Places.createClient(this)
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
                    navigationViewModel = navigationViewModel,
                    shopViewModel = shopViewModel
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyAppContent(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    stationViewModel: EstacionsViewModel,
    mainViewModel: MainViewModel,
    navigationViewModel: NavigationViewModel,
    shopViewModel: ShopViewModel
) {
    val isBottomBarVisible by mainViewModel.isBottomBarVisible.collectAsState()
    val isTopBarVisible by mainViewModel.isTopBarVisible.collectAsState()
    val currentRoute =
        navController.currentBackStackEntryAsState().value?.destination?.route

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            if (isTopBarVisible) {
                Box {
                    CenterAlignedTopAppBar(
                        title = {
                            Text(
                                text = stringResource(id = R.string.app_name),
                                style = MaterialTheme.typography.titleMedium
                            )
                        },
                        colors = topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            navigationIconContentColor = Color.White,
                            titleContentColor = Color.White,
                            actionIconContentColor = Color.White
                        ),
                        navigationIcon = {
                            if (currentRoute == MyAppRoute.Item.route || currentRoute == MyAppRoute.Station.route) {
                                IconButton(onClick = { mainViewModel.navigateBack() }) {
                                    Icon(Icons.AutoMirrored.Rounded.KeyboardArrowLeft, contentDescription = "Back", Modifier.size(32.dp))
                                }
                            }
                            else {
                                IconButton(onClick = { mainViewModel.navigateTo(MyAppRoute.Account) }) {
                                    Icon(Icons.Default.AccountCircle, contentDescription = "Account", Modifier.size(32.dp))
                                }
                            }
                        },
                        actions = {
                            println("currentRoute: $currentRoute")
                            println("MyAppRoute.Shop.route: ${MyAppRoute.Shop.route}")
                            if (currentRoute == MyAppRoute.Shop.route || currentRoute == MyAppRoute.Item.route) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("1000 ", fontSize = 20.sp)
                                    Icon(
                                        painter = painterResource(id = R.drawable.dollar_minimalistic_svgrepo_com),
                                        contentDescription = "Localized description",
                                        modifier = Modifier.size(32.dp),
                                        tint = Color(0xFFD4AF37)
                                    )
                                }
                            }
                        },
                    )
                    HorizontalDivider(
                        modifier = Modifier.align(Alignment.BottomStart),
                        thickness = 3.dp,
                        color = Color.White
                    )
                }
            }
        },
        bottomBar = {
            if (isBottomBarVisible) {
                Box {
                    MyAppBottomNavigation(
                        currentRoute = currentRoute, mainViewModel = mainViewModel
                    )
                    HorizontalDivider(
                        modifier = Modifier.align(Alignment.TopStart),
                        thickness = 3.dp,
                        color = Color.White
                    )
                }
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
                    RoutesScreen(RoutesViewModel(), mainViewModel)
                }
                composable(MyAppRoute.Home.route) {
                    HomeScreen()
                }
                composable(MyAppRoute.Social.route) {
                    SocialScreen()
                }
                composable(MyAppRoute.Shop.route) {
                    ShopScreen(shopViewModel, mainViewModel)
                }
                composable(MyAppRoute.Account.route) {
                    PetScreen()
                }
                composable(MyAppRoute.GravarRuta.route) {
                    GravarRutaScreen(GravarRutaViewModel())
                }
                composable(
                    route = MyAppRoute.Station.route,
                    arguments = listOf(navArgument("stationId") { type = NavType.StringType })
                ) {
                    EstacioBicingWidget(navController, mainViewModel, stationViewModel)
                }
                composable(
                    route = MyAppRoute.Item.route,
                    arguments = listOf(navArgument("itemId") { type = NavType.StringType })
                ) {
                    ShopItemWidget(navController, mainViewModel, shopViewModel)
                }
            }
        }
    }
}

@Composable
fun MyAppBottomNavigation(
    currentRoute: String?,
    mainViewModel: MainViewModel
) {
    var selected by remember { mutableStateOf(2) }
    NavigationBar(
        modifier = Modifier.fillMaxWidth(),
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = Color.White
    ) {
        TOP_LEVEL_DESTINATIONS.forEachIndexed { index, destination ->
            NavigationBarItem(
                selected = index == selected,
                onClick =
                {
                    selected = index
                    mainViewModel.navigateTo(destination.route)
                },
                icon = {
                    if (destination.selectedIcon != null) {
                        Icon(
                            imageVector = if (index == selected) destination.selectedIcon!! else destination.unselectedIcon!!,
                            contentDescription = stringResource(id = destination.iconTextId),
                            tint = Color.White
                        )
                    }
                    else {
                        Icon(
                            painter = painterResource(
                                if (index == selected) destination.selectedImageResource!! else destination.unselectedImageResource!!
                            ),
                            contentDescription = stringResource(id = destination.iconTextId),
                            tint = Color.White
                        )
                    }
                },
            )
        }
    }
}






