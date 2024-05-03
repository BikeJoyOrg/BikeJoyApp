package com.example.bikejoyapp.map.data
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
    override fun getPosition(): LatLng = LatLng(lat, lon)
    override fun getTitle(): String = address
    override fun getSnippet(): String ?= null
    override fun getZIndex(): Float ?= null

}

@Serializable
data class StationResponse(val stations: List<EstacioBicing>)
