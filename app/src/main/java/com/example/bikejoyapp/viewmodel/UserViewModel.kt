package com.example.bikejoyapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bikejoyapp.data.User
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.bikejoyapp.api.ApiService
import com.example.bikejoyapp.data.LoginResponse
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Response

class UserViewModel : ViewModel() {
    var status: String = ""
    var token: String? = null
    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    private val json = Json {
        ignoreUnknownKeys = true
    }

    @OptIn(ExperimentalSerializationApi::class)
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://nattech.fib.upc.edu:40360/")
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()
        .create(ApiRetrofit::class.java)

    fun login(username: String, password: String) {
        runBlocking {
            val response: Response<LoginResponse> = retrofit.login(username, password)
            //if (response.isSuccessful) {
                status = response.body()?.status ?: ""
                token = response.body()?.token
                _user.postValue(response.body()?.user)
            //}
            //println("error loggin in: ${response.errorBody()?.string()}")
        }
    }

    fun register(username: String, email: String, password1: String, password2: String) {
        runBlocking {
            val response = retrofit.register(username, email, password1, password2)
            status = response.body()?.status as String
        }
    }

    fun logout(token: String?) {
        runBlocking {
            val response = retrofit.logout(token)
            status = if (response.body()?.status as String == "success logout") {
                "success logout"
            } else response.body()?.errors as String
        }
    }
}