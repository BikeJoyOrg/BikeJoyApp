package com.example.bikejoyapp.data

object Mascotes {
    val mascotes = listOf(
        Mascota(0,1,0,1,0,1,"Josep1", "% extra coins"),
        Mascota(0,1,0,1,0,1,"Josep2", "% extra coins"),
        Mascota(0,1,0,1,0,1,"Josep3", "% extra coins"),
        Mascota(0,1,0,1,0,1,"Josep4", "% extra coins"),
        Mascota(0,1,0,1,0,1,"Josep5", "% extra coins"),
        Mascota(0,1,0,1,0,1,"Josep6", "% extra coins"),
        Mascota(0,1,0,1,0,1,"Josep7", "% extra coins"),
        Mascota(0,1,0,1,0,1,"Josep8", "% extra coins"),
        Mascota(0,1,0,1,0,1,"Josep9", "% extra coins"),
        Mascota(0,1,0,1,0,1,"Josep10", "% extra coins"),
        Mascota(0,1,0,1,0,1,"Josep11", "% extra coins"),
        Mascota(0,1,0,1,0,1,"Josep12", "% extra coins"),
        Mascota(0,1,0,1,0,1,"Josep13", "% extra coins"),
        Mascota(0,1,0,1,0,1,"Josep14", "% extra coins"),
        Mascota(0,1,0,1,0,1,"Josep15", "% extra coins"),
        Mascota(0,1,0,1,0,1,"Josep16", "% extra coins"),
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