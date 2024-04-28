package com.example.bikejoyapp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object LoggedUser {
    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> = _user

    fun setLoggedUser(loggedUser: User?) {
        _user.value = loggedUser
    }

    fun getLoggedUser(): User? {
        return _user.value
    }

    fun isLoggedIn(): Boolean {
        return _user.value != null
    }

    fun clear() {
        _user.value = null
    }
}