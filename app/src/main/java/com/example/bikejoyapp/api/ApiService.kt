package com.example.bikejoyapp.api

import com.example.bikejoyapp.data.Item
import com.example.bikejoyapp.data.ItemResponse
import com.example.bikejoyapp.data.Mascota
import com.example.bikejoyapp.data.MascotaAconseguida
import com.example.bikejoyapp.data.StationResponse
import com.example.bikejoyapp.data.StationStatusResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
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

    @GET("pets/getMascotas/")
    suspend fun getPets(): Response<List<Mascota>>

    @GET("pets/getMascota/{name}/")
    suspend fun getPet(@Path("name") nom: String): Response<Mascota>

    @GET("pets/getMascotasAconseguidesUsuari/")
    suspend fun getPetsAconseguidesUsuari(@Header("Authorization") token: String): Response<List<MascotaAconseguida>>

    @PATCH("pets/equiparMascota/{name}/")
    suspend fun equiparMascota(@Path("name") nom: String, @Header("Authorization") token: String,): Response<Unit>

    @PATCH("pets/lvlUp/{name}/")
    suspend fun lvlUp(@Path("name") nom: String, @Header("Authorization") token: String,): Response<Unit>

    @POST("pets/createMascotaAconseguida/{name}/")
    suspend fun createMascotaAconseguida(@Path("name", ) nom: String, @Header("Authorization") token: String,): Response<MascotaAconseguida>

}