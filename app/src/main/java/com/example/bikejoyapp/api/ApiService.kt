package com.example.bikejoyapp.api

import com.example.bikejoyapp.data.AchievementProgressResponse
import com.example.bikejoyapp.data.AchievementResponse
import com.example.bikejoyapp.data.BikeLaneResponse
import com.example.bikejoyapp.data.Comentario
import com.example.bikejoyapp.data.CommentRequest
import com.example.bikejoyapp.data.CompletedRoute
import com.example.bikejoyapp.data.ItemPurchasedResponse
import com.example.bikejoyapp.data.ItemResponse
import com.example.bikejoyapp.data.LoginResponse
import com.example.bikejoyapp.data.Mascota
import com.example.bikejoyapp.data.MascotaAconseguida
import com.example.bikejoyapp.data.PuntoIntermedio
import com.example.bikejoyapp.data.PuntsInterRuta
import com.example.bikejoyapp.data.PuntsVisitats
import com.example.bikejoyapp.data.RatingRequest
import com.example.bikejoyapp.data.RouteResponse
import com.example.bikejoyapp.data.RutaCompletada
import com.example.bikejoyapp.data.RutaUsuari
import com.example.bikejoyapp.data.StationResponse
import com.example.bikejoyapp.data.StationStatusResponse
import com.example.bikejoyapp.data.User
import com.example.bikejoyapp.data.UserResponse
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("items")
    suspend fun getItems(): Response<ItemResponse>

    @GET("stations")
    suspend fun getStations(): Response<StationResponse>

    @GET("stations/{id}")
    suspend fun getStationById(
        @Path("id") stationId: String
    ): Response<StationStatusResponse>

    @POST("items/{id}/purchase/")
    suspend fun buyItem(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<Void>

    @GET("user/{id}/purchases")
    suspend fun getPurchasedItems(
        @Path("id") username: String
    ): Response<ItemPurchasedResponse>

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

    @GET("rutes/")
    suspend fun searchRoutes(
        @Query("query") query: String?,
        @Query("distance") distance: Float?,
        @Query("duration") duration: Int?,
        @Query("nombreZona") nombreZona: String,
    ): Response<List<RutaUsuari>>


    @GET("routes/{ruteId}/puntos-intermedios/")
    suspend fun getPuntosIntermedios(
        @Path("ruteId") ruteId: Int
    ): Response<List<PuntoIntermedio>>

    @POST("routes/{ruteId}/rank/")
    suspend fun submitRating(
        @Header("Authorization") token: String,
        @Path("ruteId") ruteId: Int,
        @Body ratingData: RatingRequest
    ): Response<Void>

    @POST("routes/{ruteId}/comment/")
    suspend fun addComment(
        @Header("Authorization") token: String,
        @Path("ruteId") ruteId: Int,
        @Body commentData: CommentRequest
    ): Response<Void>

    @GET ("routes/{ruteId}/comments/")
    suspend fun getComments(
        @Path("ruteId") ruteId: Int
    ): Response<List<Comentario>>

    @GET ("routes/{ruteId}/average-rating/")
    suspend fun getAverageRating(
        @Path("ruteId") ruteId: Int
    ): Response<Int>

    @GET("bikelanes")
    suspend fun getBikeLanes(): Response<BikeLaneResponse>

    @GET("achievements")
    suspend fun getAchievements(): Response<AchievementResponse>

    @GET("achievementsProgress")
    suspend fun getAchievementsProgress(
        @Header("Authorization") token: String
    ): Response<AchievementProgressResponse>


    @PATCH("achievements/{achievementName}/update_value/")
    suspend fun updateAchievementValueStatus(
        @Path("achievementName") achievementName: String,
        @Body current_value: Int
        //@Body body: RedeemBody
    ): Response<Unit>

    @PATCH("achievements/{achievementName}/levels/{levelIndex}/update_achieved/")
    suspend fun updateAchievementAchievedStatus(
        @Path("achievementName") achievementName: String,
        @Path("levelIndex") levelIndex: Int,
        @Body is_achieved: Boolean
        //@Body body: RedeemBody
    ): Response<Unit>

    @PATCH("achievements/{achievementName}/levels/{levelIndex}/update_redeemed/")
    suspend fun updateAchievementRedeemedStatus(
        @Path("achievementName") achievementName: String,
        @Path("levelIndex") levelIndex: Int,
        @Body is_redeemed: RequestBody
        //@Body body: HashMap<String, Boolean>
    ): Response<Unit>
}