package com.example.bikejoyapp.data

import kotlinx.serialization.Serializable

@Serializable
class ItemPurchased (
    var id: Int,
    var item_title: String,
    var item_purchased_price: Int,
    var date_purchased: String,
)

@Serializable
data class ItemPurchasedResponse(val purchased_items: List<ItemPurchased>)