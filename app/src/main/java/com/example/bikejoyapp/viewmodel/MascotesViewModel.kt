package com.example.bikejoyapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bikejoyapp.api.ApiService
import com.example.bikejoyapp.data.Mascota
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

class MascotesViewModel: ViewModel() {
    private val _pets = MutableLiveData<List<Mascota>>(emptyList())
    val pets: MutableLiveData<List<Mascota>> = _pets

    @OptIn(ExperimentalSerializationApi::class)
    private val apiService = Retrofit.Builder()
        .baseUrl("http://nattech.fib.upc.edu:40360/")
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
                    val response: Response<List<Mascota>> = apiService.getPets()
                    if (response.isSuccessful) {
                        _pets.postValue(response.body())
                        println("Correct loading data Mascotes: ")
                    } else {
                        println("Error loading data Mascotes: ${response.errorBody()}")
                    }
                } catch (e: Exception) {
                    println("Error loading data Mascotes: $e")
                }
            }
        }
    }

    //fun getItemById(itemId: String): Item? {
    //    return _pets.value?.find { it.id == itemId }
    //}
}