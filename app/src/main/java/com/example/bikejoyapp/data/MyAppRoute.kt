package com.example.bikejoyapp.data

sealed class MyAppRoute(val route: String) {
    data object Map : MyAppRoute("map")
    data object Routes : MyAppRoute("routes")
    data object Home : MyAppRoute("home")
    data object Social : MyAppRoute("social")
    data object Shop : MyAppRoute("shop")
    data object Account : MyAppRoute("account")
    data object Station : MyAppRoute("station/{stationId}") {
        fun createRoute(stationId: String) = "station/$stationId"
    }
}