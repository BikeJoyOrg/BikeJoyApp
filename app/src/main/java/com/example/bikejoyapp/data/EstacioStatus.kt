package com.example.bikejoyapp.data
import kotlinx.serialization.Serializable

@Serializable
data class StationStatusResponse(
    val state: StationStatus
)

@Serializable
data class StationStatus(
    val station_id: Int,
    val mechanical: Int,
    val ebike: Int,
    val num_docks_available: Int
)