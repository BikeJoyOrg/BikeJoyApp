package com.example.bikejoyapp.viewmodel

interface LocationListener {
    fun onLocationReceived(latitude: Double, longitude: Double)
    fun onLocationError(error: String)
}