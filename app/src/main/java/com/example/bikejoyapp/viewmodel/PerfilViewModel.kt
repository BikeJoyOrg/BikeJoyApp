package com.example.bikejoyapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bikejoyapp.api.ApiService
import com.example.bikejoyapp.data.ItemPurchased
import com.example.bikejoyapp.data.LoggedUser
import com.example.bikejoyapp.data.SharedPrefUtils
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit


class PerfilViewModel: ViewModel() {
    private val _purchasedItems = MutableLiveData<List<ItemPurchased>>(emptyList())
    val purchasedItems: MutableLiveData<List<ItemPurchased>> = _purchasedItems

    init {
        getItemsPurchased()
    }

    @OptIn(ExperimentalSerializationApi::class)
    private val apiService = Retrofit.Builder()
        .baseUrl("http://nattech.fib.upc.edu:40360/")
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()
        .create(ApiService::class.java)

    fun getItemsPurchased() {
        viewModelScope.launch {
            val user = LoggedUser.getLoggedUser()
            if (user != null) {
                try {
                    val response = apiService.getPurchasedItems(
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