package com.example.bikejoyapp.users.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object LoggedUser {
    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> = _user

    fun setLoggedUser(loggedUser: User?) {
        _user.postValue(loggedUser)
        println("user posted")
    }

    fun getLoggedUser(): User? {
        return _user.value
    }

    fun isLoggedIn(): Boolean {
        return _user.value != null
    }

    fun clear() {
        _user.postValue(null)
    }
}