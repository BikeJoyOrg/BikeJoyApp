package com.example.bikejoyapp.data


data class Comentario(
    val id: Int,
    val ruta: Int,
    val user: Int,
    val text: String,
)
data class RatingRequest(val mark: Int)
data class CommentRequest(val text: String)
