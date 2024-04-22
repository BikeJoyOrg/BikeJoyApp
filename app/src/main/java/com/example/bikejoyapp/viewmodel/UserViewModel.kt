package com.example.bikejoyapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class UserViewModel : ViewModel() {
    var status: String = ""
    fun login(username: String, password: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://nattech.fib.upc.edu:40360/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        runBlocking {
            val response = retrofit.create(ApiRetrofit::class.java).login(username, password)
            status = response.body()?.get("status") as String
        }
    }

    fun register(username: String, email: String, password1: String, password2: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://nattech.fib.upc.edu:40360/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        runBlocking {
            val response = retrofit.create(ApiRetrofit::class.java).register(username, email, password1, password2)
            status = response.body()?.get("status") as String
        }
    }

    fun logout() {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://nattech.fib.upc.edu:40360/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        viewModelScope.launch {
            val response = retrofit.create(ApiRetrofit::class.java).logout()
            status = response.body()?.get("status") as String
        }
    }
}