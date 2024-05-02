package com.example.bikejoyapp.data

import kotlinx.serialization.Serializable

@Serializable
data class PuntsInterRuta(
    val PuntName: String,
    val PuntOrder: Int,
    val PuntLat: Float,
    val PuntLong: Float,
    val RuteId: Int?
)
