package com.example.bikejoyapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bikejoyapp.data.Achievement
import com.example.bikejoyapp.data.AchievementResponse
import com.example.bikejoyapp.data.BikeLane
import com.example.bikejoyapp.data.BikeLaneResponse
import com.example.bikejoyapp.data.Item
import com.example.bikejoyapp.data.Level
import com.google.android.gms.maps.model.LatLng
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

class AchievementViewModel : ViewModel() {
    private val _achievements = MutableLiveData<List<Achievement>>()
    val achievements: LiveData<List<Achievement>> = _achievements


    interface ApiService {
        @GET("achievements")
        suspend fun getAchievements(): Response<AchievementResponse>




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

    private val json = Json {
        ignoreUnknownKeys = true
    }

    @OptIn(ExperimentalSerializationApi::class)
    private val apiService = Retrofit.Builder()
        .baseUrl("http://nattech.fib.upc.edu:40360/")
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()
        .create(ApiService::class.java)

    init {
        getAchievementsData()
    }

    private fun getAchievementsData() {
/*
        _achievements.value = listOf(
            Achievement(
                name = "Aventurero",
                currentValue = 15,
                levels = arrayOf(
                    Level(1, "Viaja un total de 20 km", 20, 50, 1000, isAchieved = false, isRedeemed = false),
                    Level(2, "Viaja un total de 60 km", 60, 200, 1000, isAchieved = false, isRedeemed = false),
                    Level(3, "Viaja un total de 150 km", 150, 200, 1000, isAchieved = false, isRedeemed = false)
                )
            ),
            Achievement(
                name = "Creador",
                currentValue = 10,
                levels = arrayOf(
                    Level(1, "Crea un total de 10 rutas", 10, 50, 1000, isAchieved = true, isRedeemed = false),
                    Level(2, "Crea un total de 25 rutas", 25, 100, 1000, isAchieved = false, isRedeemed = false),
                    Level(3, "Crea un total de 50 rutas", 50, 150, 1000, isAchieved = false, isRedeemed = false)
                )
            ),
            Achievement(
                name = "Explorador",
                currentValue = 0,
                levels = arrayOf(
                    Level(1, "Explora un total del 15% del mapa", 15, 200, 1000, isAchieved = true, isRedeemed = true),
                    Level(2, "Explora un total del 50% del mapa", 50, 200, 1000, isAchieved = true, isRedeemed = true),
                    Level(3, "Explora un total del 100% del mapa", 100, 200, 1000, isAchieved = false, isRedeemed = false)
                )
            ),
            Achievement(
                name = "Entusiasta",
                currentValue = 50,
                levels = arrayOf(
                    Level(1, "Completa un total de 10 rutas", 10, 200, 1000, isAchieved = true, isRedeemed = true),
                    Level(2, "Completa un total de 25 rutas", 25, 200, 1000, isAchieved = true, isRedeemed = true),
                    Level(3, "Completa un total de 50 rutas", 50, 200, 1000, isAchieved = true, isRedeemed = true)
                )
            ),
            Achievement(
                name = "Apasionado",
                currentValue = 0,
                levels = arrayOf(
                    Level(1, "Visita un total de 10 estaciones", 10, 200, 1000, isAchieved = false, isRedeemed = false),
                    Level(2, "Visita un total de 25 estaciones", 25, 200, 1000, isAchieved = false, isRedeemed = false),
                    Level(3, "Visita un total de 50 estaciones", 50, 200, 1000, isAchieved = false, isRedeemed = false)
                )
            ),
            Achievement(
                name = "Sociable",
                currentValue = 0,
                levels = arrayOf(
                    Level(1, "Haz 10 amigos", 10, 200, 1000, isAchieved = false, isRedeemed = false),
                    Level(2, "Haz 25 amigos", 25, 200, 1000, isAchieved = false, isRedeemed = false),
                    Level(3, "Haz 50 amigos", 50, 200, 1000, isAchieved = false, isRedeemed = false)
                )
            ),
            Achievement(
                name = "Crítico",
                currentValue = 0,
                levels = arrayOf(
                    Level(1, "Comenta en 5 rutas que hayas completado", 5, 200, 1000, isAchieved = false, isRedeemed = false),
                    Level(2, "Comenta en 15 rutas que hayas completado", 15, 200, 1000, isAchieved = false, isRedeemed = false),
                    Level(3, "Comenta en 30 rutas que hayas completado", 30, 200, 1000, isAchieved = false, isRedeemed = false)
                )
            ),
            Achievement(
                name = "Navegante",
                currentValue = 0,
                levels = arrayOf(
                    Level(1, "Completa una navegación 20 veces", 20, 200, 1000, isAchieved = false, isRedeemed = false),
                    Level(2, "Completa una navegación 40 veces", 40, 200, 1000, isAchieved = false, isRedeemed = false),
                    Level(3, "Completa una navegación 70 veces", 70, 200, 1000, isAchieved = false, isRedeemed = false)
                )
            ),
            Achievement(
                name = "Derrochador",
                currentValue = 0,
                levels = arrayOf(
                    Level(1, "Gasta 100 monedas en la tienda", 100, 200, 1000, isAchieved = false, isRedeemed = false),
                    Level(2, "Gasta 400 monedas en la tienda", 400, 200, 1000, isAchieved = false, isRedeemed = false),
                    Level(3, "Gasta 1000 monedas en la tienda", 1000, 200, 1000, isAchieved = false, isRedeemed = false)
                )
            ),
            Achievement(
                name = "Criador",
                currentValue = 0,
                levels = arrayOf(
                    Level(1, "Sube 1 mascota a su nivel máximo", 1, 200, 1000, isAchieved = false, isRedeemed = false),
                    Level(2, "Sube 4 mascotas a su nivel máximo", 4, 200, 1000, isAchieved = false, isRedeemed = false),
                    Level(3, "Sube 9 mascotas a su nivel máximo", 9, 200, 1000, isAchieved = false, isRedeemed = false)
                )
            ),
            Achievement(
                name = "Ecologista",
                currentValue = 0,
                levels = arrayOf(
                    Level(1, "Estalvia un total de 50 de cO2", 50, 200, 1000, isAchieved = false, isRedeemed = false),
                    Level(2, "Estalvia un total de 100 de cO2", 100, 200, 1000, isAchieved = false, isRedeemed = false),
                    Level(3, "Estalvia un total de 200 de cO2", 200, 200, 1000, isAchieved = false, isRedeemed = false)
                )
            ),
        )
        */

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val response: Response<AchievementResponse> = apiService.getAchievements()
                    if (response.isSuccessful) {
                        val achievements = response.body()?.achievements ?: emptyList()
                        _achievements.postValue(achievements)
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

    fun getAchievementByName(achievementName: String): Achievement? {
        return _achievements.value?.find { it.name == achievementName }
    }

    fun updateAchievement(achievementName: String, value: Int) {
        val achievement = _achievements.value?.find { it.name == achievementName }
       achievement?.let {
            // Si ya ha alcanzado el nivel 3, no hace nada
            if (!it.levels[2].isAchieved) {
                it.currentValue = value
                patchAchievementValue(achievementName, value)
                it.levels.forEach { level ->
                    if (!level.isAchieved && value >= level.valueRequired) {
                        if (level.level == 3) {
                            // Si ya ha alcanzado el nivel 3, el valor se queda en el limite
                            it.currentValue = level.valueRequired
                        }
                        level.isAchieved = true
                    }
                }
                _achievements.value = _achievements.value // Trigger LiveData update
            }
        }

    }

    fun claimReward(achievementName: String, levelIndex: Int) {
        val achievement = _achievements.value?.find { it.name == achievementName }
        achievement?.let { achievement1 ->
            achievement1.levels[levelIndex].isRedeemed = true
            patchAchievementRedeemed(achievementName, levelIndex)
            _achievements.value = _achievements.value // Trigger LiveData update
        }
    }


    private fun patchAchievementValue(achievementName: String, value: Int) {
        viewModelScope.launch {
            val response = apiService.updateAchievementValueStatus(achievementName, value)
            if (response.isSuccessful) {
                println("Achievement value update was successful")
            } else {
                println("Achievement value update failed with status code: ${response.code()}")
                response.errorBody()?.let {
                    println("Error body: ${it.string()}")
                }
            }
        }
    }
    private fun patchAchievementRedeemed(achievementName: String, levelIndex: Int) {
        viewModelScope.launch {
            try {
                //opcio 1 new_value = request.data.get('is_redeemed')
                //val body = hashMapOf("is_redeemed" to true)
                //opcio 2
                val jsonObject = JSONObject()
                jsonObject.put("is_redeemed", true)
                val requestBody =
                    jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())
                //opcio 3 new_value = request.data
                //val is_redeemed = true
                val response = apiService.updateAchievementRedeemedStatus(achievementName, levelIndex+1, requestBody)
                if (response.isSuccessful) {
                    println("Reward claim was successful")
                } else {
                    println("Reward claim failed with status code: ${response.code()}")
                    response.errorBody()?.let {
                        println("Error body: ${it.string()}")
                    }
                }
            } catch (e: java.net.ProtocolException) {
                println("Error: unexpected end of stream: $e")
            } catch (e: Exception) {
                println("Error updating achievement: $e")
            }
        }
    }
}