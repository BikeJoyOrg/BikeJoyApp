package com.example.bikejoyapp

import com.example.bikejoyapp.profile.viewmodel.AchievementViewModel
import com.example.bikejoyapp.routes.viewmodel.GravarRutaViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
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
import com.example.bikejoyapp.profile.ui.HomeScreen
import com.example.bikejoyapp.map.ui.MapScreen
import com.example.bikejoyapp.routes.data.RoutesScreen
import com.example.bikejoyapp.shop.ui.ShopScreen
import com.example.bikejoyapp.users.ui.LoginRequiredScreen
import com.example.bikejoyapp.theme.BikeJoyAppTheme
import com.example.bikejoyapp.map.viewmodel.EstacionsViewModel
import android.Manifest
import android.content.pm.PackageManager
import com.example.bikejoyapp.profile.ui.AchievementScreen
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import com.example.bikejoyapp.routes.ui.GravarRutaScreen
import com.example.bikejoyapp.users.ui.LoginScreen
import com.example.bikejoyapp.profile.ui.PetScreen
import com.example.bikejoyapp.users.ui.RegisterScreen
import com.example.bikejoyapp.map.viewmodel.BikeLanesViewModel
import com.example.bikejoyapp.routes.ui.RouteDetailScreen
import com.example.bikejoyapp.map.viewmodel.NavigationViewModel
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import com.example.bikejoyapp.routes.viewmodel.RoutesViewModel
import com.example.bikejoyapp.users.viewmodel.UserViewModel
import com.example.bikejoyapp.shop.viewmodel.ShopViewModel
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.livedata.observeAsState
import com.example.bikejoyapp.calendar.ui.CalendarScreen
import com.example.bikejoyapp.calendar.viewmodel.CalendarViewModel
import com.example.bikejoyapp.users.data.LoggedUser
import com.example.bikejoyapp.profile.ui.ProfileScreen
import com.example.bikejoyapp.ranking.ui.RankingScreen
import com.example.bikejoyapp.map.ui.EstacioBicingWidget
import com.example.bikejoyapp.profile.ui.SpecificAchievementWidget
import com.example.bikejoyapp.profile.viewmodel.PerfilViewModel
import com.example.bikejoyapp.profile.viewmodel.MascotesViewModel
import com.example.bikejoyapp.ranking.viewmodel.RankingViewModel
import com.example.bikejoyapp.utils.MainViewModel
import com.example.bikejoyapp.utils.MyAppRoute
import com.example.bikejoyapp.utils.NavigationCommand
import com.example.bikejoyapp.utils.SharedPrefUtils
import com.example.bikejoyapp.utils.TOP_LEVEL_DESTINATIONS


class MainActivity : ComponentActivity() {
    private lateinit var placesClient: PlacesClient
    private lateinit var startDestination: String

    private val navigationViewModel: NavigationViewModel by viewModels {
        NavigationViewModel.Factory(placesClient, this)
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
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        verificarPermisos()
        val stationViewModel: EstacionsViewModel by viewModels()
        val mainViewModel: MainViewModel by viewModels()
        val bikeLanesViewModel: BikeLanesViewModel by viewModels()
        val shopViewModel: ShopViewModel by viewModels()
        val mascotesViewModel: MascotesViewModel by viewModels()
        val userViewModel: UserViewModel by viewModels()
        val perfilViewModel: PerfilViewModel by viewModels()
        val achievementViewModel: AchievementViewModel by viewModels()
        val rankingViewModel: RankingViewModel by viewModels()
        val calendarViewModel: CalendarViewModel by viewModels()
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, getString(R.string.google_maps_key))
        }
        placesClient = Places.createClient(this)

        startDestination =
            if (SharedPrefUtils.getToken() != null) MyAppRoute.Map.route else MyAppRoute.Login.route

        setContent {
            BikeJoyAppTheme {
                val navController = rememberNavController()

                //Observa Flow de comandos de navegación
                LaunchedEffect(Unit) {
                    mainViewModel.navigationCommands.collect { command ->
                        when (command) {
                            is NavigationCommand.ToDestination -> navController.navigate(command.destination.route)
                            is NavigationCommand.ToDynamicDestination -> navController.navigate(
                                command.destination
                            )

                            is NavigationCommand.Back -> navController.popBackStack()
                        }
                    }
                }

                MyAppContent(
                    navController = navController,
                    startDestination = startDestination,
                    stationViewModel = stationViewModel,
                    mainViewModel = mainViewModel,
                    navigationViewModel = navigationViewModel,
                    shopViewModel = shopViewModel,
                    bikeLanesViewModel = bikeLanesViewModel,
                    achievementViewModel = achievementViewModel,
                    userViewModel = userViewModel,
                    mascotesViewModel = mascotesViewModel,
                    perfilViewModel = perfilViewModel,
                    rankingViewModel = rankingViewModel,
                    calendarViewModel = calendarViewModel
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
    startDestination: String,
    navController: NavHostController,
    stationViewModel: EstacionsViewModel,
    mainViewModel: MainViewModel,
    navigationViewModel: NavigationViewModel,
    userViewModel: UserViewModel,
    shopViewModel: ShopViewModel,
    bikeLanesViewModel: BikeLanesViewModel,
    achievementViewModel: AchievementViewModel,
    perfilViewModel: PerfilViewModel,
    mascotesViewModel: MascotesViewModel,
    rankingViewModel: RankingViewModel,
    calendarViewModel: CalendarViewModel
) {
    val isBottomBarVisible by mainViewModel.isBottomBarVisible.collectAsState()
    val isTopBarVisible by mainViewModel.isTopBarVisible.collectAsState()
    val currentRoute =
        navController.currentBackStackEntryAsState().value?.destination?.route

    val user by LoggedUser.user.observeAsState()
    //SharedPrefUtils.removeToken()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            if (isTopBarVisible) {
                Box {
                    CenterAlignedTopAppBar(
                        title = {
                            Text(
                                text = currentRoute ?: "BikeJoy",
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
                            if (currentRoute == MyAppRoute.Station.route ||
                                currentRoute == MyAppRoute.RouteDetail.route ||
                                currentRoute == MyAppRoute.Achievement.route ||
                                currentRoute == MyAppRoute.PurchaseHistory.route ||
                                currentRoute == MyAppRoute.Pet.route
                            ) {
                                IconButton(onClick = { mainViewModel.navigateBack() }) {
                                    Icon(
                                        Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
                                        contentDescription = "Back",
                                        Modifier.size(32.dp)
                                    )
                                }
                            } else {
                                Row {
                                    IconButton(onClick = { mainViewModel.navigateTo(MyAppRoute.Account) }) {
                                        val iconBackground =
                                            if (currentRoute == MyAppRoute.Account.route) MaterialTheme.colorScheme.secondary else Color.Transparent
                                        Box(
                                            modifier = Modifier
                                                .background(
                                                    color = iconBackground,
                                                    shape = CircleShape
                                                )
                                                .padding(8.dp)
                                        ) {
                                            Icon(
                                                painter = painterResource(id = R.drawable.cyclist_boy_icon),
                                                contentDescription = "Account",
                                                Modifier.size(32.dp),
                                                tint = Color.Unspecified
                                            )
                                        }
                                    }
                                    IconButton(onClick = { mainViewModel.navigateTo(MyAppRoute.Calendar) }) {
                                        val iconBackground =
                                            if (currentRoute == MyAppRoute.Calendar.route) MaterialTheme.colorScheme.secondary else Color.Transparent
                                        Box(
                                            modifier = Modifier
                                                .background(
                                                    color = iconBackground,
                                                    shape = CircleShape
                                                )
                                                .padding(8.dp)
                                        ) {
                                            Icon(
                                                painter = painterResource(id = R.drawable.home_icon_color),
                                                contentDescription = "Shop",
                                                Modifier.size(32.dp),
                                                tint = Color.Unspecified
                                            )
                                        }
                                    }
                                }
                            }
                        },
                        actions = {
                            if (user != null) {
                                println("currentRoute: $currentRoute")
                                println("MyAppRoute.Shop.route: ${MyAppRoute.Shop.route}")
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(user?.coins.toString(), fontSize = 20.sp)
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
                navController = navController, startDestination = startDestination
            ) {
                composable(MyAppRoute.Map.route) {
                    MapScreen(
                        stationViewModel,
                        mainViewModel,
                        navigationViewModel,
                        bikeLanesViewModel,
                        mascotesViewModel,
                        userViewModel
                    )
                }
                composable(MyAppRoute.Routes.route) {
                    RoutesScreen(RoutesViewModel(), mainViewModel)
                }
                composable(MyAppRoute.Home.route) {
                    //HomeScreen(userViewModel, mainViewModel, perfilViewModel)
                    if(SharedPrefUtils.getToken() != null) RankingScreen(rankingViewModel)
                    else LoginRequiredScreen(mainViewModel)
                }
                composable(MyAppRoute.PurchaseHistory.route) {
                    HomeScreen(userViewModel, mainViewModel, perfilViewModel)
                }
                composable(MyAppRoute.Social.route) {
                    if(SharedPrefUtils.getToken() != null) {
                        achievementViewModel.getAchievementsProgressData()
                        AchievementScreen(achievementViewModel, mainViewModel)
                    }
                    else LoginRequiredScreen(mainViewModel)
                    //RankingScreen(rankingViewModel)
                }
                composable(MyAppRoute.Shop.route) {
                    ShopScreen(shopViewModel, mainViewModel)
                }
                composable(MyAppRoute.Account.route) {
                    if(SharedPrefUtils.getToken() != null) ProfileScreen(mainViewModel)
                    else LoginRequiredScreen(mainViewModel)
                }
                composable(MyAppRoute.Pet.route) {
                    PetScreen(mascotesViewModel, mainViewModel)
                }
                composable(MyAppRoute.GravarRuta.route) {
                    GravarRutaScreen(GravarRutaViewModel(), mainViewModel)
                }
                composable(
                    route = MyAppRoute.Station.route,
                    arguments = listOf(navArgument("stationId") { type = NavType.StringType })
                ) {
                    EstacioBicingWidget(navController, mainViewModel, stationViewModel)
                }
                composable(MyAppRoute.Login.route) {
                    LoginScreen(userViewModel, mainViewModel)
                }
                composable(MyAppRoute.Calendar.route) {
                    CalendarScreen(calendarViewModel, mainViewModel)
                }
                composable(MyAppRoute.Register.route) {
                    RegisterScreen(userViewModel, mainViewModel)
                }
                composable(route = MyAppRoute.RouteDetail.route) {
                    val rutasCompletadas = userViewModel.completedRoutes.value
                    val rutaCompletada =
                        rutasCompletadas?.find { it.ruta_id == mainViewModel.selectedRoute?.RuteId }
                    mainViewModel.selectedRoute?.let { it1 ->
                        RouteDetailScreen(
                            RoutesViewModel(),
                            mainViewModel,
                            it1,
                            rutaCompletada,
                            navigationViewModel
                        )
                    }
                }
                composable(route = MyAppRoute.Achievement.route) {
                    SpecificAchievementWidget(
                        navController = navController,
                        mainViewModel = mainViewModel,
                        achievementViewModel = achievementViewModel
                    )
                }
                composable(MyAppRoute.LoginRequired.route) {
                    LoginRequiredScreen(mainViewModel)
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
        contentColor = Color.Unspecified,
        tonalElevation = 0.dp
    ) {
        TOP_LEVEL_DESTINATIONS.forEachIndexed { index, destination ->
            NavigationBarItem(
                selected = index == selected,
                onClick = {
                    selected = index
                    mainViewModel.navigateTo(destination.route)
                },
                icon = {
                    val iconBackground =
                        if (index == selected && currentRoute != MyAppRoute.Account.route) MaterialTheme.colorScheme.secondary else Color.Transparent
                    Box(
                        modifier = Modifier
                            .background(color = iconBackground, shape = CircleShape)
                            .padding(8.dp)
                    ) {
                        Icon(
                            painter = painterResource(
                                id = if (index == selected && currentRoute != MyAppRoute.Account.route) destination.selectedIcon else destination.unselectedIcon
                            ),
                            contentDescription = stringResource(id = destination.iconTextId),
                            tint = if (index == selected) Color.Unspecified else MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent,
                    selectedIconColor = Color.Transparent,
                    unselectedIconColor = Color.Transparent,
                    selectedTextColor = Color.Transparent,
                    unselectedTextColor = Color.Transparent
                )
            )
        }
    }
}






