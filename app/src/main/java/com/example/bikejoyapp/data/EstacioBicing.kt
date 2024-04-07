package com.example.bikejoyapp.data
import kotlinx.serialization.Serializable

@Serializable
data class EstacioBicing(
    val station_id: Int,
    val address: String,
    val lat: Double,
    val lon: Double,
)

@Serializable
data class StationResponse(val stations: List<EstacioBicing>)
