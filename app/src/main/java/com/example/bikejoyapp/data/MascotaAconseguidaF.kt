package com.example.bikejoyapp.data

import kotlinx.serialization.Serializable

@Serializable
data class MascotaAconseguidaF (
    var mascota: Mascota,
    val nicknameUsuari: String,
    var nivell: Int,
    var equipada: Boolean
)