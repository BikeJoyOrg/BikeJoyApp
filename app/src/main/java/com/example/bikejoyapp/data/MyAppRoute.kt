package com.example.bikejoyapp.data

sealed class MyAppRoute(val route: String) {
    object Map : MyAppRoute("map")
    object Routes : MyAppRoute("routes")
    object Home : MyAppRoute("home")
    object Social : MyAppRoute("social")
    object Shop : MyAppRoute("shop")
    object Account : MyAppRoute("account")
}