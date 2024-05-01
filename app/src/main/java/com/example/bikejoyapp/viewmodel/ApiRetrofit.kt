package com.example.bikejoyapp.viewmodel

import com.example.bikejoyapp.data.Comentario
import com.example.bikejoyapp.data.CompletedRoute
import com.example.bikejoyapp.data.LoginResponse
import com.example.bikejoyapp.data.PuntsInterRuta
import com.example.bikejoyapp.data.PuntsRuta
import com.example.bikejoyapp.data.RouteResponse
import com.example.bikejoyapp.data.RutaUsuari
import com.example.bikejoyapp.data.User
import kotlinx.serialization.json.Json
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
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

    @GET("users/getProfile/")
    fun getProfile(
        @Header("Authorization") token: String?
    ): Response<User>

    @GET ("routes/completed-routes/")
    suspend fun getCompletedRoutes(
        @Header("Authorization") token: String,
    ): Response<List<CompletedRoute>>
}


