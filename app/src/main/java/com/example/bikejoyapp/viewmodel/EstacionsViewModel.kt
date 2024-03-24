package com.example.bikejoyapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bikejoyapp.data.EstacioBicing
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class EstacionsViewModel : ViewModel() {
    private val _estacions = MutableLiveData<List<EstacioBicing>>(emptyList())
    val estacions: LiveData<List<EstacioBicing>> = _estacions
    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading


    //no funciona si server tancat
    private val apiService = Retrofit.Builder()
        .baseUrl("http://nattech.fib.upc.edu:40360/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)

    init {
        getStationData()
    }

    private fun getStationData() {
        viewModelScope.launch {
            _loading.postValue(true)
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
            _loading.postValue(false)
        }
    }

    fun getStationById(stationId: String): LiveData<EstacioBicing?> {
        val stationLiveData = MutableLiveData<EstacioBicing?>()
        estacions.observeForever { stations ->
            val foundStation = stations.find { it.station_id == stationId.toInt() }
            stationLiveData.value = foundStation
        }
        return stationLiveData
    }
}

interface ApiService {
    @GET("stations/a3")
    suspend fun getStations(): Response<StationResponse>
}

data class StationResponse(val stations: List<EstacioBicing>)
