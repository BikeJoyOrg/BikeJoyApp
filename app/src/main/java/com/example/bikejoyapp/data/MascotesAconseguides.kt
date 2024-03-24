package com.example.bikejoyapp.data

object MascotesAconseguides {
    val mascotesAconseguides = listOf(
        MascotaAconseguida("Josep1","a",1,true),
        MascotaAconseguida("Josep2","a",1,false),
        MascotaAconseguida("Josep3","a",1,false),
        MascotaAconseguida("Josep4","a",1,false),
        MascotaAconseguida("Josep1","b",1,true),
        MascotaAconseguida("Josep6","b",1,false),
        MascotaAconseguida("Josep3","b",1,false),
        MascotaAconseguida("Josep7","b",1,false),
    )
    fun teMascota(nombreM: String, nombreU: String): Boolean {
        for (mascotaAconseguida in mascotesAconseguides) {
            if (mascotaAconseguida.nomMascota == nombreM &&  mascotaAconseguida.nicknameUsuari == nombreU) {
                return true
            }
        }
        return false
    }
    fun estaEquipat(nombreM: String, nombreU: String): Boolean {
        for (mascotaAconseguida in mascotesAconseguides) {
            if (mascotaAconseguida.nomMascota == nombreM &&  mascotaAconseguida.nicknameUsuari == nombreU) {
                return mascotaAconseguida.equipada
            }
        }
        return false
    }
    fun equipar(nombreM: String, nombreU: String) {
        for (mascotaAconseguida in mascotesAconseguides) {
            if (mascotaAconseguida.nomMascota == nombreM &&  mascotaAconseguida.nicknameUsuari == nombreU) {
                mascotaAconseguida.equipada = true
            }
            else mascotaAconseguida.equipada = false
        }
    }
    fun getNivell(nombreM: String, nombreU: String): Int {
        for (mascotaAconseguida in mascotesAconseguides) {
            if (mascotaAconseguida.nomMascota == nombreM &&  mascotaAconseguida.nicknameUsuari == nombreU) {
                return mascotaAconseguida.nivell
            }
        }
        return -1
    }
    fun getMascotaAconseguida(nombreM: String, nombreU: String): MascotaAconseguida {
        for (mascotaAconseguida in mascotesAconseguides) {
            if (mascotaAconseguida.nomMascota == nombreM && mascotaAconseguida.nicknameUsuari == nombreU) {
                return mascotaAconseguida
            }
        }
        return MascotaAconseguida("Josep","a",1,true)
    }
}