package com.example.bikejoyapp.viewmodel

import com.example.bikejoyapp.data.Comentario
import com.example.bikejoyapp.data.CompletedRoute
import com.example.bikejoyapp.data.LoginResponse
import com.example.bikejoyapp.data.PuntsInterRuta
import com.example.bikejoyapp.data.PuntsVisitats
import com.example.bikejoyapp.data.RouteResponse
import com.example.bikejoyapp.data.RutaCompletada
import com.example.bikejoyapp.data.RutaUsuari
import com.example.bikejoyapp.data.User
import com.example.bikejoyapp.data.UserResponse
import kotlinx.serialization.json.Json
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.PUT
import retrofit2.http.Query

interface ApiRetrofit {
    @GET("/v2/directions/cycling-regular")
    suspend fun getRoute(
        @Query("api_key") key: String,
        @Query("start", encoded = true) start: String,
        @Query("end", encoded = true) end: String
    ):Response<RouteResponse>

    @POST("addruta/")
    suspend fun postRoute(
        @Header("Authorization") token: String,
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
    ):Response<Void>

    @FormUrlEncoded
    @POST("users/login/")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): Response<LoginResponse>

    @FormUrlEncoded
    @POST("users/register/")
    suspend fun register(
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("password1") password1: String,
        @Field("password2") password2: String
    ): Response<Void>

    @POST("users/logout/")
    suspend fun logout(
        @Header("Authorization") token: String?
    ): Response<Void>

    @POST("routes/{rute_id}/completed/")
    suspend fun completedRoute(
        @Header("Authorization") token: String,
        @Path("rute_id") ruteid: Int,
        @Body usuarirutacompletada: RutaCompletada
    ): Response<Void>

    @POST("routes/add_punt_visitat/")
    suspend fun visitedPoint(
        @Header("Authorization") token: String,
        @Body puntsRuta: PuntsVisitats
    ): Response<Void>

    @PUT("users/updateStats/")
    suspend fun updateStats(
        @Header("Authorization") token: String,
        @Body stats: User
    ): Response<Void>


    @GET("users/getUser/")
    suspend fun getProfile(
        @Header("Authorization") token: String,
    ): Response<UserResponse>

    @GET ("routes/completed-routes/")
    suspend fun getCompletedRoutes(
        @Header("Authorization") token: String,
    ): Response<List<CompletedRoute>>
}


