package com.example.bikejoyapp.calendar.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bikejoyapp.R
import com.example.bikejoyapp.calendar.data.ForecastItem
import com.example.bikejoyapp.api.ApiService
import com.example.bikejoyapp.api.ApiServiceFactory
import kotlinx.coroutines.launch

class CalendarViewModel : ViewModel() {
    private val _forecastData = MutableLiveData<List<ForecastItem>>()
    val forecastData: LiveData<List<ForecastItem>> = _forecastData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    init {
        loadForecast(41.3888, 2.1590)
    }

    fun loadForecast(lat: Double, lon: Double) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = ApiServiceFactory.apiServiceWeather.getFiveDayForecast(lat, lon, "ebe6f7abeee25189cba09410f4ef9b3f")
                if (response.isSuccessful) {
                    _forecastData.value = response.body()?.list ?: emptyList()
                } else {
                    throw Exception("Failed to fetch forecast data: ${response.errorBody()?.string()}")
                }
                _isLoading.value = false
            } catch (e: Exception) {
                Log.e("CalendarViewModel", "Error loading forecast", e)
                _error.value = e.message ?: "An unknown error occurred"
                _isLoading.value = false
            }
        }
    }
}



