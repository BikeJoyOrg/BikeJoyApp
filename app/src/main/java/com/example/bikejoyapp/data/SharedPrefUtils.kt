package com.example.bikejoyapp.data

import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object SharedPrefUtils {
    private const val PREF_NAME = "user"
    private val _token = MutableLiveData<String?>()
    val token: LiveData<String?> = _token

    init {
        _token.postValue(getToken())
    }

    fun getToken(): String? {
        val sharedPref = MyApplication.instance.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        println("Tokenactualitzat: ${sharedPref.getString("token", null)}")
        return sharedPref.getString("token", null)
    }

    fun setToken(token: String?) {
        val sharedPref = MyApplication.instance.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        sharedPref.edit() {
            putString("token", token)
            apply()
        }
        _token.postValue(token)
    }

    fun removeToken() {
        val sharedPref = MyApplication.instance.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        sharedPref.edit() {
            remove("token")
            apply()
        }
        _token.postValue(null)
    }
}