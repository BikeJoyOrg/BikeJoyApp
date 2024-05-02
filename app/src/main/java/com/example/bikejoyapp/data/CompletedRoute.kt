package com.example.bikejoyapp.data
import kotlinx.serialization.Serializable

@Serializable
data class CompletedRoute(
    val ruta_id: Int,
    val rated: Boolean
)
