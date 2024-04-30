package com.example.bikejoyapp.api

import com.example.bikejoyapp.data.ItemPurchasedResponse
import com.example.bikejoyapp.data.ItemResponse
import com.example.bikejoyapp.data.StationResponse
import com.example.bikejoyapp.data.StationStatusResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @GET("items")
    suspend fun getItems(): Response<ItemResponse>

    @GET("stations")
    suspend fun getStations(): Response<StationResponse>

    @GET("stations/{id}")
    suspend fun getStationById(
        @Path("id") stationId: String
    ): Response<StationStatusResponse>
    @POST("items/purchase/{id}/")
    suspend fun buyItem(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<Void>

    @GET("user/purchases/{id}")
    suspend fun getPurchasedItems(
        @Path("id") username: String
    ): Response<ItemPurchasedResponse>
}