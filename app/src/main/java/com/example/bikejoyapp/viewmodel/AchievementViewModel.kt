package com.example.bikejoyapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bikejoyapp.data.Achievement
import com.example.bikejoyapp.data.AchievementProgress
import com.example.bikejoyapp.data.AchievementProgressResponse
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
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

class AchievementViewModel : ViewModel() {
    private val _achievements = MutableLiveData<List<Achievement>>()
    val achievements: LiveData<List<Achievement>> = _achievements
    private val _achievementsProgress = MutableLiveData<Map<String, AchievementProgress>>()

    interface ApiService {
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
        getAchievementsProgressData()
    }

    private fun getAchievementsData() {
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

    private fun getAchievementsProgressData() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    //val token: SharedPrefUtils.getToken()
                    val response: Response<AchievementProgressResponse> =
                        apiService.getAchievementsProgress("Token $token")
                    if (response.isSuccessful) {
                        val achievementsProgressList =
                            response.body()?.achievementsProgress ?: emptyList()
                        val achievementsProgressMap: Map<String, AchievementProgress> =
                            achievementsProgressList.associateBy { it.achievement }
                        _achievementsProgress.postValue(achievementsProgressMap)
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

    /*
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

     */

    fun claimReward(achievementName: String, levelIndex: Int) {
        val achievement = _achievements.value?.find { it.name == achievementName }
        achievement?.let { achievement1 ->
            //achievement1.levels[levelIndex].isRedeemed = true
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
                val response = apiService.updateAchievementRedeemedStatus(
                    achievementName,
                    levelIndex + 1,
                    requestBody
                )
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