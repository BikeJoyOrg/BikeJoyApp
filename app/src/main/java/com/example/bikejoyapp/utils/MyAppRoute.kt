package com.example.bikejoyapp.utils

sealed class MyAppRoute(val route: String) {
    data object Map : MyAppRoute("map")
    data object Routes : MyAppRoute("routes")
    data object Home : MyAppRoute("home")
    data object Social : MyAppRoute("social")
    data object Shop : MyAppRoute("shop")
    data object Account : MyAppRoute("account")
    data object GravarRuta : MyAppRoute("GravarRuta")
    data object Station : MyAppRoute("station/{stationId}") {
        fun createRoute(stationId: String) = "station/$stationId"
    }
    data object Login : MyAppRoute("login")
    data object Register : MyAppRoute("register")
    data object RouteDetail : MyAppRoute("RouteDetail")

    data object Achievement : MyAppRoute("achievement/{achievementName}") {
        fun createRoute(achievementName: String) = "achievement/$achievementName"
    }

    data object PurchaseHistory : MyAppRoute("purchaseHistory") {
        fun createRoute() = "purchaseHistory"
    }

    data object Pet : MyAppRoute("pet") {
        fun createRoute() = "pet"
    }
    data object LoginRequired : MyAppRoute("LoginRequired")
}