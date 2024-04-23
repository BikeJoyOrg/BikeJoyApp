package com.example.bikejoyapp.data
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import kotlinx.serialization.Serializable

@Serializable
data class StationStatus(
    val station_id: Int,
    val mechanical: Int,
    val ebike: Int,
    val num_docks_available: Int
)

@Serializable
data class StationStatusResponse(
    val state: StationStatus
)