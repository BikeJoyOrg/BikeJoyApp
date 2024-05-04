package com.example.bikejoyapp.ranking.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bikejoyapp.api.ApiService
import com.example.bikejoyapp.users.data.User
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Response
import retrofit2.Retrofit



class RankingViewModel: ViewModel() {
    private val _users = MutableLiveData<List<User>>(emptyList())
    val users: MutableLiveData<List<User>> = _users

    private val json = Json {
        ignoreUnknownKeys = true
    }

    @OptIn(ExperimentalSerializationApi::class)
    private val apiService = Retrofit.Builder()
        .baseUrl("http://nattech.fib.upc.edu:40360/")
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()
        .create(ApiService::class.java)

    init {
        getStoreData()
    }

    fun getStoreData() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val response: Response<List<User>> = apiService.getUsers()
                    if (response.isSuccessful) {
                        _users.postValue(response.body())
                        println("Correct loading data Users: ")
                    } else {
                        println("Error loading data Users: ${response.errorBody()}")
                    }
                } catch (e: Exception) {
                    println("Error loading data Users: $e")
                }
            }
        }
    }

}