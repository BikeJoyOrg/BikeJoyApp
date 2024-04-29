package com.example.bikejoyapp.data

object Mascotes {
    val mascotes = listOf(
        Mascota("Horcycle", "a", "a", "a","a","a","a","a","a", 1.1f, 1.1f, 1.1f),
        Mascota("P", "a", "a", "a","a","a","a","a","a", 1.1f, 1.1f, 1.1f),
        Mascota("Torroc", "a", "a", "a","a","a","a","a","a", 1.1f, 1.1f, 1.1f),
        Mascota("Thundra", "a", "a", "a","a","a","a","a","a", 1.1f, 1.1f, 1.1f),
        Mascota("Dragosaur", "a", "a", "a","a","a","a","a","a", 1.1f, 1.1f, 1.1f),
        Mascota("Tubarao", "a", "a", "a","a","a","a","a","a", 1.1f, 1.1f, 1.1f),
        Mascota("Subzedragon", "a", "a", "a","a","a","a","a","a", 1.1f, 1.1f, 1.1f),
        Mascota("Diamoth", "a", "a", "a","a","a","a","a","a", 1.1f, 1.1f, 1.1f),
        Mascota("Galaxagonal", "a", "a", "a","a","a","a","a","a", 1.1f, 1.1f, 1.1f),
        Mascota("Josep10", "a", "a", "a","a","a","a","a","a", 1.1f, 1.1f, 1.1f)
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