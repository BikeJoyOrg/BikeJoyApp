package com.example.bikejoyapp.data

import kotlinx.serialization.Serializable

@Serializable
data class RutaCompletada(
    val userId: Int?,
    val routeId: Int,
    val data: String?,
    val temps: Int,
)