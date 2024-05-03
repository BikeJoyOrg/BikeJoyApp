package com.example.bikejoyapp.routes.data

import kotlinx.serialization.Serializable

@Serializable
data class PuntsVisitats(
    val PuntName: String,
    val PuntLat: Double,
    val PuntLong: Double
)

data class PuntoIntermedio(val lat: Float, val lng: Float)
