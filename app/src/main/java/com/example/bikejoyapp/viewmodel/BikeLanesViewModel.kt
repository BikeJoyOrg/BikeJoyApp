package com.example.bikejoyapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bikejoyapp.api.ApiServiceFactory
import com.example.bikejoyapp.data.BikeLane
import com.example.bikejoyapp.data.BikeLaneResponse
import com.google.android.gms.maps.model.LatLng
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

class BikeLanesViewModel: ViewModel() {
    private val _bikeLanes = MutableLiveData<List<BikeLane>>(emptyList())
    val bikeLanes: LiveData<List<BikeLane>> = _bikeLanes

    init {
        getBikeLaneData()
    }

    private fun getBikeLaneData() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val response: Response<BikeLaneResponse> = ApiServiceFactory.apiServiceSerializer.getBikeLanes()
                    if (response.isSuccessful) {
                        val serverBikeLanes = response.body()?.bikelanes ?: emptyList()
                        val bikeLanes = serverBikeLanes.map { serverBikeLane ->
                            BikeLane(
                                id = serverBikeLane.id,
                                latLng = serverBikeLane.lat_lngs.map { serverLatLng ->
                                    LatLng(serverLatLng.latitude, serverLatLng.longitude)
                                }
                            )
                        }
                        _bikeLanes.postValue(bikeLanes)
                        println("Correct loading data: ")
                    } else {
                        val errorBody = response.errorBody()?.string()
                        println("Error fetching data: $errorBody")
                    }
                } catch (e: Exception) {
                    println("Error fetching data: $e")
                }
            }
        }
    }
}