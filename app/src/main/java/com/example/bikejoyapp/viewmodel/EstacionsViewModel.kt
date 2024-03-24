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


    private val apiService = Retrofit.Builder()
        .baseUrl("https://65f1c39c034bdbecc763a229.mockapi.io/bikejoy/v1/")
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
                    val response: Response<List<EstacioBicing>> = apiService.getStations()
                    if (response.isSuccessful) {
                        _estacions.postValue(response.body() ?: emptyList())
                        println("Correct loading data: ")
                    } else {
                        println("Error fetching data: ${response.errorBody()}")
                    }
                } catch (e: Exception) {
                    println("Error fetching data: $e")
                }
            }
            _loading.postValue(false)
        }
    }
}

interface ApiService {
    @GET("stations")
    suspend fun getStations(): Response<List<EstacioBicing>>
}
