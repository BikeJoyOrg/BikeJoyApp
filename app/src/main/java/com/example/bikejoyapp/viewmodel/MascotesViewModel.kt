package com.example.bikejoyapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bikejoyapp.api.ApiService
import com.example.bikejoyapp.data.Item
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import retrofit2.Retrofit

private val json = Json {
    ignoreUnknownKeys = true
}


class MascotesViewModel : ViewModel() {
    private val _pets = MutableLiveData<List<Item>>(emptyList())
    val items: MutableLiveData<List<Item>> = _pets

    @OptIn(ExperimentalSerializationApi::class)
    private val apiService = Retrofit.Builder()
        .baseUrl("https://65f1c39c034bdbecc763a229.mockapi.io/bikejoy/v1/")
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()
        .create(ApiService::class.java)
}
