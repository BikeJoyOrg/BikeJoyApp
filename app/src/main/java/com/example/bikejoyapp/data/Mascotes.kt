package com.example.bikejoyapp.data

object Mascotes {
    val mascotes = listOf(
        Mascota("Horcycle", 0, 1, 0,1,0,1,0,1, 1.1f, 1.1f, 1.1f),
        Mascota("P", 0, 1, 0,1,0,1,0,1, 1.1f, 1.1f, 1.1f),
        Mascota("Torroc", 0, 1, 0,1,0,1,0,1, 1.1f, 1.1f, 1.1f),
        Mascota("Thundra", 0, 1, 0,1,0,1,0,1, 1.1f, 1.1f, 1.1f),
        Mascota("Dragosaur", 0, 1, 0,1,0,1,0,1, 1.1f, 1.1f, 1.1f),
        Mascota("Tubarao", 0, 1, 0,1,0,1,0,1, 1.1f, 1.1f, 1.1f),
        Mascota("Subzedragon", 0, 1, 0,1,0,1,0,1, 1.1f, 1.1f, 1.1f),
        Mascota("Diamoth", 0, 1, 0,1,0,1,0,1, 1.1f, 1.1f, 1.1f),
        Mascota("Galaxagonal", 0, 1, 0,1,0,1,0,1, 1.1f, 1.1f, 1.1f),
        Mascota("Josep10", 0, 1, 0,1,0,1,0,1, 1.1f, 1.1f, 1.1f)
    )
    fun getMascotaPorNombre(nombre: String): Mascota? {
        for (mascota in mascotes) {
            if (mascota.name == nombre) {
                return mascota
            }
        }
        return null
    }
}