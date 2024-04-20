package com.example.bikejoyapp.data

import com.google.android.gms.maps.model.LatLng


data class BikeLane(
    val id: String,
    val latLng: List<LatLng>
)