package com.example.bikejoyapp.data

data class PuntsRuta(
    val PuntId: Int?,
    val PuntName: String,
    val PuntLat: Double,
    val PuntLong: Double
)

data class PuntoIntermedio(val lat: Float, val lng: Float)
