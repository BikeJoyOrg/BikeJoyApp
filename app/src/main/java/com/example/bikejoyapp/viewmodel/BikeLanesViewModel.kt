package com.example.bikejoyapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bikejoyapp.data.BikeLane
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class BikeLanesViewModel: ViewModel() {
    private val _bikeLanes = MutableLiveData<List<BikeLane>>(emptyList())
    val bikeLanes: LiveData<List<BikeLane>> = _bikeLanes
    /*private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading*/

    data class ServerLatLng(
        val id: Int,
        val latitude: Double,
        val longitude: Double
    )

    data class ServerBikeLane(
        val id: String,
        val lat_lngs: List<ServerLatLng>
    )

    data class BikeLaneResponse(
        val bikelanes: List<ServerBikeLane>
    )

    interface ApiService {
        @GET("bikelanes")
        suspend fun getBikeLanes(): Response<BikeLaneResponse>
    }

    private val apiService = Retrofit.Builder()
        .baseUrl("http://nattech.fib.upc.edu:40360/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)

    init {
        getBikeLaneData()
    }

    private fun getBikeLaneData() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val response: Response<BikeLaneResponse> = apiService.getBikeLanes()
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
                        println("Error fetching data: ${response.errorBody()}")
                    }
                } catch (e: Exception) {
                    println("Error fetching data: $e")
                }
            }
        }
    }
}