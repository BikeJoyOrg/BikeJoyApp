package com.example.bikejoyapp.users.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.bikejoyapp.routes.data.CompletedRoute
import org.json.JSONObject
import retrofit2.Response
import com.example.bikejoyapp.api.ApiServiceFactory
import com.example.bikejoyapp.utils.SharedPrefUtils
import com.example.bikejoyapp.users.data.LoggedUser
import com.example.bikejoyapp.users.data.LoginResponse
import com.example.bikejoyapp.users.data.UserResponse

class UserViewModel : ViewModel() {
    private val _completedRoutes = MutableLiveData<MutableList<CompletedRoute>>()
    val completedRoutes: LiveData<MutableList<CompletedRoute>> = _completedRoutes

    init {
        val token = SharedPrefUtils.getToken()
        if (token != null) {
            getCompletedRoutes()
            viewModelScope.launch { getProfile(token) }
        }
    }

    suspend fun login(username: String, password: String): String {
        var result = ""

        try {
            val response: Response<LoginResponse> = ApiServiceFactory.apiServiceSerializer.login(username, password)

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
            val response = ApiServiceFactory.apiServiceSerializer.register(username, email, password1, password2)
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
        val response = ApiServiceFactory.apiServiceSerializer.logout("Token $token")
        if (response.isSuccessful) {
            LoggedUser.clear()
            SharedPrefUtils.removeToken()
            result = "Success"
        } else {
            val errorBody = response.errorBody()!!.string()
            val jsonObject = JSONObject(errorBody)
            if (jsonObject.has("error")) {
                result = jsonObject.getString("error")
            } else {
                result = "Unknown error"
            }
            SharedPrefUtils.removeToken()
        }
        return result
    }

    suspend fun getProfile(token: String) {
        val response: Response<UserResponse> = ApiServiceFactory.apiServiceSerializer.getProfile("Token $token")
        if(response.isSuccessful) {
            LoggedUser.setLoggedUser(response.body()?.user)
        } else {
            val errorBody = response.errorBody()?.string()
            val jsonObject = errorBody?.let { JSONObject(it) }
            val result = if (jsonObject?.has("error") == true) {
                jsonObject.getString("error")
            } else {
                "Unknown error"
            }
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
                    val response = ApiServiceFactory.apiServiceSerializer.getCompletedRoutes("Token $token")
                    if (response.isSuccessful && response.body() != null) {
                        _completedRoutes.postValue(response.body() as MutableList<CompletedRoute>?)
                    } else {
                        Log.e("API Error", "Failed with response: ${response.errorBody()?.string()}")
                    }
                }
            } catch (e: Exception) {
                Log.e("API Exception", "Error occurred: ${e.message}")
            }
        }
    }
    fun addCompletedRoute(ruteid: Int) {
        _completedRoutes.value?.add(CompletedRoute(ruteid, false))
    }
}