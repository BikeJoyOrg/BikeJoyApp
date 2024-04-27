package com.example.bikejoyapp

import android.content.Context
import com.example.bikejoyapp.viewmodel.UserViewModel
import com.example.bikejoyapp.data.SharedPrefUtils
import androidx.core.content.edit
import kotlinx.coroutines.runBlocking

class UserState(private val userViewModel: UserViewModel) {
    private var status: String = ""

    suspend fun login(username: String, password: String): String {
        userViewModel.login(username, password)
        status = userViewModel.status
        SharedPrefUtils.setToken(userViewModel.token)
        return status
    }

    suspend fun register(username: String, email: String, password1: String, password2: String): String {
        userViewModel.register(username, email, password1, password2)
        status = userViewModel.status
        return status
    }

    suspend fun logout(): String {
        userViewModel.logout(SharedPrefUtils.getToken())
        status = userViewModel.status
        println("Status: $status")
        if(status == "success logout") {
            SharedPrefUtils.removeToken()
        }
        else if (status == "Invalid token") {
            SharedPrefUtils.removeToken()
        }
        return status
    }

    fun getStatus(): String {
        return status
    }
}