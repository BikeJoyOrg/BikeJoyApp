package com.example.bikejoyapp.data

import kotlinx.serialization.Serializable

@Serializable
class Item (
    var id: String,
    var stock_number: Int,
    var real_price: Int,
    var game_currency_price: Int,
    var item_picture_id: Int,
)

@Serializable
data class ItemResponse(val items: List<Item>)