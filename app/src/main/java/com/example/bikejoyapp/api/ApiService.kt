package com.example.bikejoyapp.api

import com.example.bikejoyapp.data.Item
import com.example.bikejoyapp.data.ItemResponse
import com.example.bikejoyapp.data.Mascota
import com.example.bikejoyapp.data.StationResponse
import com.example.bikejoyapp.data.StationStatusResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("items")
    suspend fun getItems(): Response<List<Item>>

    @GET("stations")
    suspend fun getStations(): Response<StationResponse>

    @GET("stations/{id}")
    suspend fun getStationById(@Path("id") stationId: String): Response<StationStatusResponse>

    @GET("pets/getMascotas/")
    suspend fun getPets(): Response<List<Mascota>>

    @GET("pets/getMascota/{name}/")
    suspend fun getPet(@Path("name") nom: String): Response<Mascota>
}