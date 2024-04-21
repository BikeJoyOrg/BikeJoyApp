package com.example.bikejoyapp.data

import kotlinx.serialization.Serializable

data class Level(
    val level: Int,
    val description: String,
    val valueRequired: Int,
    var isAchieved: Boolean = false,
    var isRedeemed: Boolean = false
)

data class Achievement (
    val name: String,
    var actualValue: Int,
    val levels: Array<Level>
)