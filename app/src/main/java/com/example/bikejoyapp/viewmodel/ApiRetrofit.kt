package com.example.bikejoyapp.viewmodel

import com.example.bikejoyapp.data.PuntsInterRuta
import com.example.bikejoyapp.data.PuntsRuta
import com.example.bikejoyapp.data.RouteResponse
import com.example.bikejoyapp.data.RutaUsuari
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiRetrofit {
    @GET("/v2/directions/cycling-regular")
    suspend fun getRoute(
        @Query("api_key") key: String,
        @Query("start", encoded = true) start: String,
        @Query("end", encoded = true) end: String
    ):Response<RouteResponse>

    @POST("rutes/")
    suspend fun postRoute(
        @Body rutaUsuari: RutaUsuari
    ):Response<RutaUsuari>
/*
    @POST("punts/")
    suspend fun postPunts(
        @Body puntsRuta: PuntsRuta
    ):Response<PuntsRuta>
    */
    @POST("puntsInterRuta/")
    suspend fun postPuntsInter(
        @Body puntsInterRuta: PuntsInterRuta
    ):Response<PuntsInterRuta>
}


