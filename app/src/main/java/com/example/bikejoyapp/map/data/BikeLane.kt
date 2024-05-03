package com.example.bikejoyapp.map.data

import com.google.android.gms.maps.model.LatLng
import kotlinx.serialization.Serializable

data class BikeLane(
    val id: String,
    val latLng: List<LatLng>
)

@Serializable
data class ServerLatLng(
    val id: Int,
    val latitude: Double,
    val longitude: Double
)

@Serializable
data class ServerBikeLane(
    val id: String,
    val lat_lngs: List<ServerLatLng>
)

@Serializable
data class BikeLaneResponse(
    val bikelanes: List<ServerBikeLane>
)