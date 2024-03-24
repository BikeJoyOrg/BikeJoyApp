package com.example.bikejoyapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bikejoyapp.data.MyAppRoute
import com.example.bikejoyapp.data.MyAppTopLevelDestination
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class NavigationViewModel : ViewModel() {
    private val _isNavigating = MutableStateFlow(false)
    val isNavigating: StateFlow<Boolean> = _isNavigating

    private val _navigationTime = MutableStateFlow(0) // Tiempo en segundos
    val navigationTime: StateFlow<Int> = _navigationTime

    private val _navigationKm = MutableStateFlow(0.0)
    val navigationKm: MutableStateFlow<Double> = _navigationKm

    fun startNavigation() {
        _isNavigating.value = true
        _navigationTime.value = 0
        viewModelScope.launch {
            while (_isNavigating.value) {
                delay(1000) //
                _navigationTime.value += 1
            }
        }
    }
    fun stopNavigation() {
        _isNavigating.value = false
    }
}