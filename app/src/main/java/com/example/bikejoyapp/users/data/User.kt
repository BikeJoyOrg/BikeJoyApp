package com.example.bikejoyapp.users.data

import kotlinx.serialization.Serializable

@Serializable
class User(
    var username: String,
    var coins: Int,
    var distance: Int,
    var xp: Int,
    var monthlyDistance: Int,
    var weeklyDistance: Int,
    var dailyDistance: Int,
)

@Serializable
data class LoginResponse(val token: String, val user: User)

@Serializable
data class UserResponse(val user: User)

