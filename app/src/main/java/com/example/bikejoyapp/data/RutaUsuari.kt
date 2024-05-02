package com.example.bikejoyapp.data

import kotlinx.serialization.Serializable


@Serializable
data class RutaUsuari(
    val RuteId: Int?,
    val RuteName: String?,
    val RuteDescription: String?,
    val RuteDistance: Double,
    val RuteTime: Int,
    val RuteRating:Int,
    val PuntIniciLat: Float,
    val PuntIniciLong: Float,
    val creador: Int?,
)
