package com.example.bikejoyapp.profile.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bikejoyapp.api.ApiServiceFactory
import com.example.bikejoyapp.shop.data.ItemPurchased
import com.example.bikejoyapp.users.data.LoggedUser
import kotlinx.coroutines.launch


class PerfilViewModel: ViewModel() {
    private val _purchasedItems = MutableLiveData<List<ItemPurchased>>(emptyList())
    val purchasedItems: MutableLiveData<List<ItemPurchased>> = _purchasedItems

    init {
        getItemsPurchased()
    }
    fun getItemsPurchased() {
        viewModelScope.launch {
            val user = LoggedUser.getLoggedUser()
            if (user != null) {
                try {
                    val response = ApiServiceFactory.apiServiceSerializer.getPurchasedItems(
                        username = user.username
                    )
                    if (response.isSuccessful) {
                        _purchasedItems.postValue(response.body()?.purchased_items ?: emptyList())
                        println("Correct loading purchased items: ")
                    } else {
                        println("Error loading purchased items: ${response.errorBody()}")
                    }
                } catch (e: Exception) {
                    println("Error loading purchased items: $e")
                }
            }
        }
    }
}