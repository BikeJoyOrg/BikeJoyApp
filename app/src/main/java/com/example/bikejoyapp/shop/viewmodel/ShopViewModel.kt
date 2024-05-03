package com.example.bikejoyapp.shop.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bikejoyapp.api.ApiServiceFactory
import com.example.bikejoyapp.users.data.LoggedUser
import com.example.bikejoyapp.utils.SharedPrefUtils
import com.example.bikejoyapp.shop.data.Item
import com.example.bikejoyapp.shop.data.ItemResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response


class ShopViewModel: ViewModel() {
    private val _items = MutableLiveData<List<Item>>(emptyList())
    val items: MutableLiveData<List<Item>> = _items
    init {
        getStoreData()
    }

    fun getStoreData() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val response: Response<ItemResponse> = ApiServiceFactory.apiServiceSerializer.getItems()
                    if (response.isSuccessful) {
                        _items.postValue(response.body()?.items ?: emptyList())
                        println("Correct loading data: ")
                    } else {
                        println("Error loading data: ${response.errorBody()}")
                    }
                } catch (e: Exception) {
                    println("Error loading data: $e")
                }
            }
        }
    }

    fun getItemById(itemId: Int): Item? {
        return _items.value?.find { it.id == itemId }
    }

    suspend fun buyItem(id: Int): String {
        val token = SharedPrefUtils.getToken()
        val user = LoggedUser.getLoggedUser()
        val item = getItemById(id)
        if (token == null || user == null) {
            return "Usuari no autenticat"
        }
        else if (item == null) {
            return "Item no disponible"
        }
        else if (user.coins < item.game_currency_price) {
            return "No tens prous monedes"
        }
        val response = ApiServiceFactory.apiServiceSerializer.buyItem("Token $token", id)
        if (response.isSuccessful) {
            user.coins = (user.coins - item.game_currency_price)
            LoggedUser.setLoggedUser(user)
            return "Compra realitzada correctament"

        } else {
            println("Item purchase failed with status code: ${response.code()}")
            response.errorBody()?.let {
                println("Error body: ${it.string()}")
            }
            return "Error en la compra"
        }
    }
}