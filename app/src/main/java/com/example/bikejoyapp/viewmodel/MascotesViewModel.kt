package com.example.bikejoyapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bikejoyapp.api.ApiService
import com.example.bikejoyapp.data.LoggedUser
import com.example.bikejoyapp.data.Mascota
import com.example.bikejoyapp.data.MascotaAconseguida
import com.example.bikejoyapp.data.SharedPrefUtils
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Response
import retrofit2.Retrofit



class MascotesViewModel: ViewModel() {
    private val _pets = MutableLiveData<List<Mascota>>(emptyList())
    val pets: MutableLiveData<List<Mascota>> = _pets
    private val _petsAconseguides = MutableLiveData<List<MascotaAconseguida>>(emptyList())
    val petsAconseguides: MutableLiveData<List<MascotaAconseguida>> = _petsAconseguides

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
        getStoreData()
        getMascotesAconseguidesUser()
    }

    fun getStoreData() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val response: Response<List<Mascota>> = apiService.getPets()
                    if (response.isSuccessful) {
                        _pets.postValue(response.body())
                        println("Correct loading data Mascotes: ")
                    } else {
                        println("Error loading data Mascotes: ${response.errorBody()}")
                    }
                } catch (e: Exception) {
                    println("Error loading data Mascotes: $e")
                }
            }
        }
    }

    fun getMascotesAconseguidesUser() {
        viewModelScope.launch {
            val token = SharedPrefUtils.getToken()
            if (token != null) {
                val response = apiService.getPetsAconseguidesUsuari("Token $token")
                if (response.isSuccessful) {
                    _petsAconseguides.postValue(response.body())
                    println("Correct loading data Mascotes Aconseguides: ")
                } else {
                    println("Error loading data Mascotes Aconseguides: ${response.errorBody()}")
                }
            }
        }
    }

    fun getPetByName(name: String): Mascota? {
        return _pets.value?.find { it.name == name }
    }

    fun unlockMascota(name: String) {
        viewModelScope.launch {
            val token = SharedPrefUtils.getToken()
            val user = LoggedUser.getLoggedUser()
            if (token != null && user != null) {
                val response = apiService.createMascotaAconseguida(name, "Token $token")
                if (response.isSuccessful) {
                    println("Mascota obtenida correctament")
                    LoggedUser.setLoggedUser(user)
                } else {
                    println("Error al obtindre mascota with status code: ${response.code()}")
                    response.errorBody()?.let {
                        println("Error body: ${it.string()}")
                    }
                }
            }
        }
    }

    fun equiparMascota(name: String) {
        viewModelScope.launch {
            val token = SharedPrefUtils.getToken()
            val response = apiService.equiparMascota(name, "Token $token")
            if (response.isSuccessful) {
                println("Mascota equipada correctament")
                getMascotesAconseguidesUser()
            } else {
                println("Error al equipar mascota with status code: ${response.code()}")
                response.errorBody()?.let {
                    println("Error body: ${it.string()}")
                }
            }
        }
    }

    fun lvlUpMascota(name: String) {
        viewModelScope.launch {
            val token = SharedPrefUtils.getToken()
            val user = LoggedUser.getLoggedUser()
            if (token != null && user != null) {
                val response = apiService.lvlUp(name, "Token $token")
                if (response.isSuccessful) {
                    println("Mascota nivelada correctament")
                } else {
                    println("Error al nivelar mascota with status code: ${response.code()}")
                    response.errorBody()?.let {
                        println("Error body: ${it.string()}")
                    }
                }
            }
        }
    }

    fun getBonusMascota(): FloatArray {
        val pet = _petsAconseguides.value?.find { it.equipada }
        val mult =  pet?.nivell?.toFloat() ?: 0f
        val bonus1 = pet?.let { getPetByName(it.nomMascota)?.bonus1 } ?: 0f
        val bonus2 = pet?.let { getPetByName(it.nomMascota)?.bonus2 } ?: 0f
        val bonus3 = pet?.let { getPetByName(it.nomMascota)?.bonus3 } ?: 0f

        return floatArrayOf(bonus1*mult+1f, bonus2*mult+1f, bonus3*mult+1f)
    }

    fun getBonusMascotaByName(name: String): FloatArray {
        val pet = _petsAconseguides.value?.find { it.nomMascota == name}
        val mult =  pet?.nivell?.toFloat() ?: 0f
        val bonus1 = pet?.let { getPetByName(it.nomMascota)?.bonus1 } ?: 0f
        val bonus2 = pet?.let { getPetByName(it.nomMascota)?.bonus2 } ?: 0f
        val bonus3 = pet?.let { getPetByName(it.nomMascota)?.bonus3 } ?: 0f

        return floatArrayOf(bonus1*mult+1f, bonus2*mult+1f, bonus3*mult+1f)
    }
}