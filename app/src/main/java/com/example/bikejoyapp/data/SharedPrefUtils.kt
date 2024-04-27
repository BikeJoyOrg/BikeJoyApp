package com.example.bikejoyapp.data

import android.content.Context
import androidx.core.content.edit

object SharedPrefUtils {
    private const val PREF_NAME = "user"

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
    }

    fun removeToken() {
        val sharedPref = MyApplication.instance.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        sharedPref.edit() {
            remove("token")
            apply()
        }
    }
}