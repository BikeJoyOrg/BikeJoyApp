package com.example.bikejoyapp

import com.example.bikejoyapp.viewmodel.UserViewModel
import kotlinx.coroutines.delay

class UserState(private val userViewModel: UserViewModel) {
    private var status: String = ""

    suspend fun login(username: String, password: String): String {
        userViewModel.login(username, password)
        return userViewModel.status
    }

    suspend fun register(username: String, email: String, password1: String, password2: String): String {
        userViewModel.register(username, email, password1, password2)
        status = userViewModel.status
        return status
    }

    fun logout() {
        status = ""
    }
}