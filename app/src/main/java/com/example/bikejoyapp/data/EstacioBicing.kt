package com.example.bikejoyapp.data
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import kotlinx.serialization.Serializable

@Serializable
data class EstacioBicing(
    val station_id: Int,
    val address: String,
    val lat: Double,
    val lon: Double,
): ClusterItem {
    override fun getPosition(): LatLng = position
    override fun getTitle(): String = title
    override fun getSnippet(): String ?= null
}

@Serializable
data class StationResponse(val stations: List<EstacioBicing>)
