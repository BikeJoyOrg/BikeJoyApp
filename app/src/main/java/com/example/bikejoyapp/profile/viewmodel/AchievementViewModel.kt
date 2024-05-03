package com.example.bikejoyapp.profile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bikejoyapp.api.ApiServiceFactory
import com.example.bikejoyapp.profile.data.Achievement
import com.example.bikejoyapp.profile.data.AchievementProgress
import com.example.bikejoyapp.profile.data.AchievementProgressResponse
import com.example.bikejoyapp.profile.data.AchievementResponse
import com.example.bikejoyapp.utils.SharedPrefUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Response

class AchievementViewModel : ViewModel() {
    private val _achievements = MutableLiveData<List<Achievement>>()
    val achievements: LiveData<List<Achievement>> = _achievements
    private val _achievementsProgress = MutableLiveData<Map<String, AchievementProgress>>()
    val achievementsProgress: LiveData<Map<String, AchievementProgress>> = _achievementsProgress

    init {
        getAchievementsData()
        getAchievementsProgressData()
    }

    private fun getAchievementsData() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val response: Response<AchievementResponse> = ApiServiceFactory.apiServiceSerializer.getAchievements()
                    if (response.isSuccessful) {
                        val achievements = response.body()?.achievements ?: emptyList()
                        _achievements.postValue(achievements)
                        println("Correct loading data: Achievements")
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

    fun getAchievementsProgressData() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val token = SharedPrefUtils.getToken()
                    val response: Response<AchievementProgressResponse> =
                        ApiServiceFactory.apiServiceSerializer.getAchievementsProgress("Token $token")
                    if (response.isSuccessful) {
                        val achievementsProgressList =
                            response.body()?.achievementsProgress ?: emptyList()
                        val achievementsProgressMap: Map<String, AchievementProgress> =
                            achievementsProgressList.associateBy { it.achievement }
                        _achievementsProgress.postValue(achievementsProgressMap)
                        println("Correct loading data: AchievementsProgress")
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
            val response = ApiServiceFactory.apiServiceSerializer.updateAchievementValueStatus(achievementName, value)
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
                val response = ApiServiceFactory.apiServiceSerializer.updateAchievementRedeemedStatus(
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