package com.example.bikejoyapp.data

import kotlinx.serialization.Serializable


@Serializable
data class EstacioBicing(
    val stationId: Int,
    val name: String,
    val lat: Double,
    val lon: Double,
    val numBicManuals: Int,
    val numBicElectriques: Int,
    val numAnclatges: Int,
    val address: String
)
