package com.example.bikejoyapp.data

import kotlinx.serialization.Serializable


@Serializable
data class EstacioBicing(
    val station_id: Int,
    val lat: Double,
    val lon: Double,
    val address: String,
    val last_updated: Int,
    val mechanical: Int,
    val ebike: Int,
    val num_docks_available: Int
)
