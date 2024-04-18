package com.example.bikejoyapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bikejoyapp.api.ApiService
import com.example.bikejoyapp.data.Item
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Response
import retrofit2.Retrofit

private val json = Json {
    ignoreUnknownKeys = true
}

class ShopViewModel: ViewModel() {
    private val _items = MutableLiveData<List<Item>>(emptyList())
    val items: MutableLiveData<List<Item>> = _items

    @OptIn(ExperimentalSerializationApi::class)
    private val apiService = Retrofit.Builder()
        .baseUrl("https://65f1c39c034bdbecc763a229.mockapi.io/bikejoy/v1/")
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()
        .create(ApiService::class.java)

    init {
        getStoreData()
    }

    private fun getStoreData() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val response: Response<List<Item>> = apiService.getItems()
                    if (response.isSuccessful) {
                        _items.postValue(response.body())
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

    fun getItemById(itemId: String): Item? {
        return _items.value?.find { it.id == itemId }
    }
}