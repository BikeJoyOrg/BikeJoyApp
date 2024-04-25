package com.example.bikejoyapp.data

import kotlinx.serialization.Serializable

@Serializable
class Mascota(
    val name: String,
    val imgEgg: Int,
    val imgEggl: Int,
    val img1: Int,
    val img1l: Int,
    val img2: Int,
    val img2l: Int,
    val img3: Int,
    val img3l: Int,
    val bonus1: Float,
    val bonus2: Float,
    val bonus3: Float
)