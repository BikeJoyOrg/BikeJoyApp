package com.example.bikejoyapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bikejoyapp.data.EstacioBicing
import com.example.bikejoyapp.data.Item
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class StoreViewModel: ViewModel() {
    private val _items = MutableLiveData<List<Item>>(emptyList())
    val items: MutableLiveData<List<Item>> = _items

    private val apiService = Retrofit.Builder()
        .baseUrl("https://65f1c39c034bdbecc763a229.mockapi.io/bikejoy/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)
}