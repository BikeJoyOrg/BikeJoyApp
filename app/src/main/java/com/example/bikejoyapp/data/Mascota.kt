package com.example.bikejoyapp.data

import kotlinx.serialization.Serializable

@Serializable
class Mascota(
    val name: String,
    val imgEgg: String,
    val imgEggl: String,
    val img1: String,
    val img1l: String,
    val img2: String,
    val img2l: String,
    val img3: String,
    val img3l: String,
    val bonus1: Float,
    val bonus2: Float,
    val bonus3: Float
)