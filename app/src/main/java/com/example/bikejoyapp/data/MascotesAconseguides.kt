package com.example.bikejoyapp.data

import com.example.bikejoyapp.data.Mascotes.mascotes

object MascotesAconseguides {
    val mascotesAconseguides = listOf(
        MascotaAconseguida(mascotes[0],"a",1,true),
        MascotaAconseguida(mascotes[1],"a",1,false),
        MascotaAconseguida(mascotes[2],"a",1,false),
        MascotaAconseguida(mascotes[3],"a",1,false),
        MascotaAconseguida(mascotes[0],"b",0,true),
        MascotaAconseguida(mascotes[5],"b",1,false),
        MascotaAconseguida(mascotes[2],"b",3,false),
        MascotaAconseguida(mascotes[6],"b",2,false),
    )
    fun teMascota(nombreM: String, nombreU: String): Boolean {
        for (mascotaAconseguida in mascotesAconseguides) {
            if (mascotaAconseguida.mascota.name == nombreM &&  mascotaAconseguida.nicknameUsuari == nombreU) {
                return true
            }
        }
        return false
    }
    fun estaEquipat(nombreM: String, nombreU: String): Boolean {
        for (mascotaAconseguida in mascotesAconseguides) {
            if (mascotaAconseguida.mascota.name == nombreM &&  mascotaAconseguida.nicknameUsuari == nombreU) {
                return mascotaAconseguida.equipada
            }
        }
        return false
    }
    fun equipar(nombreM: String, nombreU: String) {
        for (mascotaAconseguida in mascotesAconseguides) {
            if (mascotaAconseguida.mascota.name == nombreM &&  mascotaAconseguida.nicknameUsuari == nombreU) {
                mascotaAconseguida.equipada = true
            }
            else mascotaAconseguida.equipada = false
        }
    }
    fun getNivell(nombreM: String, nombreU: String): Int {
        for (mascotaAconseguida in mascotesAconseguides) {
            if (mascotaAconseguida.mascota.name == nombreM &&  mascotaAconseguida.nicknameUsuari == nombreU) {
                return mascotaAconseguida.nivell
            }
        }
        return -1
    }
    fun getMascotaAconseguida(nombreM: String, nombreU: String): MascotaAconseguida {
        for (mascotaAconseguida in mascotesAconseguides) {
            if (mascotaAconseguida.mascota.name == nombreM && mascotaAconseguida.nicknameUsuari == nombreU) {
                return mascotaAconseguida
            }
        }
        return MascotaAconseguida(Mascota("Josep1","a", "a", "a","a","a","a","a","a",1.1f, 1.1f, 1.1f),"a",1,true)
    }
}