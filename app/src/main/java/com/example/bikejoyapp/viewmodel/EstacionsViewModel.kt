package com.example.bikejoyapp.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bikejoyapp.api.ApiService
import com.example.bikejoyapp.data.EstacioBicing
import com.example.bikejoyapp.data.StationResponse
import com.example.bikejoyapp.data.StationStatus
import com.example.bikejoyapp.data.StationStatusResponse
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class EstacionsViewModel : ViewModel() {
    private val _estacions = MutableLiveData<List<EstacioBicing>>(emptyList())
    val estacions: LiveData<List<EstacioBicing>> = _estacions
    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading
    private val stationStatusCache = mutableMapOf<String, Pair<StationStatus?, LocalDateTime>>()


    private val json = Json {
        ignoreUnknownKeys = true
    }

    @OptIn(ExperimentalSerializationApi::class)
    private val apiService = Retrofit.Builder()
        .baseUrl("http://nattech.fib.upc.edu:40360/")
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()
        .create(ApiService::class.java)

    init {
        getStationData()
    }

    private fun getStationData() {
        viewModelScope.launch {
            _loading.value = true
            withContext(Dispatchers.IO) {
                try {
                    val response: Response<StationResponse> = apiService.getStations()
                    if (response.isSuccessful) {
                        _estacions.postValue(response.body()?.stations ?: emptyList())
                        println("Correct loading data: ")
                    } else {
                        val errorBody = response.errorBody()?.string()
                        println("Error fetching data: ${response.errorBody()}")
                    }
                } catch (e: Exception) {
                    println("Error fetching data: $e")
                }
            }
            _loading.value = false
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getStationById(stationId: String): LiveData<Pair<StationStatus?, String?>> {
        val cached = stationStatusCache[stationId]
        val now = LocalDateTime.now()

        if (cached != null && ChronoUnit.MINUTES.between(cached.second, now) < 2) {
            return MutableLiveData(Pair(cached.first, getStationAddress(stationId)))
        }

        val resultLiveData = MutableLiveData<Pair<StationStatus?, String?>>()
        viewModelScope.launch {
            _loading.value = true
            try {
                val response: Response<StationStatusResponse> = apiService.getStationById(stationId)
                if (response.isSuccessful) {
                    val stationStatus = response.body()?.state
                    val address = getStationAddress(stationId)
                    stationStatusCache[stationId] = Pair(stationStatus, LocalDateTime.now())
                    resultLiveData.postValue(Pair(stationStatus, address))
                } else {
                    Log.e(
                        "ViewModel",
                        "Error fetching station status: ${response.errorBody()?.string()}"
                    )
                }
            } catch (e: Exception) {
                Log.e("ViewModel", "Exception when fetching station status", e)
            }
            _loading.value = false
        }
        return resultLiveData
    }

    private fun getStationAddress(stationId: String): String? {
        val stations = _estacions.value ?: return null
        val station = stations.find { it.station_id == stationId.toInt() } ?: return null
        return station.address
    }
}


