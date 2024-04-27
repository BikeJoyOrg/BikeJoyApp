package com.example.bikejoyapp.data

import kotlinx.serialization.Serializable

@Serializable
class User(
    var username: String,
    var coins: Int,
    var distance: Int,
    var xp: Int
)

@Serializable
data class LoginResponse(val status: String, val token: String, val user: User)

@Serializable
data class RegisterLogoutResponse(val status: String, val errors: String)

