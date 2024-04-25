package com.example.bikejoyapp.data

import kotlinx.serialization.Serializable

@Serializable
class Item (
    var id: Int,
    var title: String,
    var description: String,
    var stock_number: Int,
    var real_price: Int,
    var game_currency_price: Int,
    var image: String,
)

@Serializable
data class ItemResponse(val items: List<Item>)