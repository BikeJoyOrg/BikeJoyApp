package com.example.bikejoyapp.viewmodel

import android.app.Application
import android.util.Log
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
import com.example.bikejoyapp.data.CompletedRoute
import com.example.bikejoyapp.data.LoggedUser
import com.example.bikejoyapp.data.LoginResponse
import com.example.bikejoyapp.data.SharedPrefUtils
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import org.json.JSONObject
import retrofit2.Response

class UserViewModel : ViewModel() {

    private val json = Json {
        ignoreUnknownKeys = true
    }

    private val _completedRoutes = MutableLiveData<List<CompletedRoute>>()
    val completedRoutes: LiveData<List<CompletedRoute>> = _completedRoutes


    @OptIn(ExperimentalSerializationApi::class)
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://nattech.fib.upc.edu:40360/")
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()
        .create(ApiRetrofit::class.java)

    suspend fun login(username: String, password: String): String {
        var result = ""

        try {
            val response: Response<LoginResponse> = retrofit.login(username, password)

            if (response.isSuccessful) {
                val responseBody = response.body()
                val token = responseBody?.token
                val user = responseBody?.user

                LoggedUser.setLoggedUser(user)
                SharedPrefUtils.setToken(token)

                result = "Success"
            } else {

                val errorBody = response.errorBody()!!.string()
                val jsonObject = JSONObject(errorBody)

                result = jsonObject.getString("error")
                println("Error: $result")
            }
        } catch (e: Exception) {
            println("Error: ${e.message}")
        }

        return result
    }

    suspend fun register(username: String, email: String, password1: String, password2: String): String {
        var result = ""
        try {
            val response = retrofit.register(username, email, password1, password2)
            if (response.isSuccessful) {
                result = "Success"
            } else {
                val errorBody = response.errorBody()!!.string()
                val jsonObject = JSONObject(errorBody)
                result = jsonObject.getString("error")
                println("Error: $result")
            }
        }
        catch (e: Exception) {
            println("Error: ${e.message}")
        }
        return result
    }

    suspend fun logout(token: String?): String {
        var result: String
        val response = retrofit.logout("Token $token")
        if (response.isSuccessful) {
            LoggedUser.clear()
            SharedPrefUtils.removeToken()
            result = "Success"
        } else {
            val errorBody = response.errorBody()!!.string()
            val jsonObject = JSONObject(errorBody)
            result = jsonObject.getString("error")
            if (result == "Invalid token") {
                SharedPrefUtils.removeToken()
            }
        }
        return result
    }

    fun getProfile(token: String?) {
        val response = retrofit.getProfile("Token $token")
        if(response.isSuccessful) {
            LoggedUser.setLoggedUser(response.body())
        } else {
            val errorBody = response.errorBody()!!.string()
            val jsonObject = JSONObject(errorBody)
            val result = jsonObject.getString("error")
            if (result == "Invalid token") {
                SharedPrefUtils.removeToken()
            }
        }
    }

    fun getCompletedRoutes() {
        viewModelScope.launch {
            try {
                val token = SharedPrefUtils.getToken()
                if (token != null) {
                    val response = retrofit.getCompletedRoutes("Token $token")
                    if (response.isSuccessful && response.body() != null) {
                        _completedRoutes.postValue(response.body())
                    } else {
                        Log.e("API Error", "Failed with response: ${response.errorBody()?.string()}")
                    }
                }
            } catch (e: Exception) {
                Log.e("API Exception", "Error occurred: ${e.message}")
            }
        }
    }
}