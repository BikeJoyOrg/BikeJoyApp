package com.example.bikejoyapp.data


data class Comentario(
    val id: Int,
    val ruta: RutaUsuari,
    val user: User,
    val text: String,
)
data class RatingRequest(val mark: Int)
data class CommentRequest(val text: String)
