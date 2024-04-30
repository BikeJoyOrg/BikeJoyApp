package com.example.bikejoyapp.data

import kotlinx.serialization.Serializable

@Serializable
data class MascotaAconseguida (
    val id: Int,
    val nivell: Int,
    val equipada: Boolean,
    val nomMascota: String,
    val nicknameUsuari: Int
)