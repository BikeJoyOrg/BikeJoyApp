package com.example.bikejoyapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bikejoyapp.data.PuntsInterRuta
import com.example.bikejoyapp.data.RouteResponse
import com.example.bikejoyapp.data.RutaUsuari
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Math.toRadians
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class GravarRutaViewModel : ViewModel(){
    private var start: Boolean = true
    private var first: String = ""
    private var second: String = ""
    private var ruta: MutableList<LatLng> = mutableListOf()
    var polyline: MutableList<LatLng> = mutableListOf()

    private val _pl = MutableLiveData<List<LatLng>>()
    val pl: LiveData<List<LatLng>> = _pl

    private val _posstart = MutableLiveData<LatLng>()
    val posstart: LiveData<LatLng> = _posstart

    fun onselected(s: String) {
        if (start) {
            start = false
            _posstart.value = LatLng(s.split(",")[1].toDouble(), s.split(",")[0].toDouble())
            //_posstart.postValue(LatLng(s.split(",")[1].toDouble(), s.split(",")[0].toDouble()))
            first = s;
        } else {
            second = s
            createRute()
        }

    }


    private fun createRute(){
        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit().create(ApiRetrofit::class.java)
                .getRoute("5b3ce3597851110001cf6248ffd3a14535d041289931632bc575ecbf", first, second)
            if(call.isSuccessful){
                first = second
                drawRoute(call.body())
                Log.d("aris", "aviam si va")
            }
            else{
                Log.d("aris", "malo")
            }
        }
    }

    private fun drawRoute(routeResponse: RouteResponse?) {
        polyline.clear()
        routeResponse?.features?.first()?.geometry?.coordinates?.forEach {
            polyline.add(LatLng(it[1], it[0]))
        }

        Log.d("aris","PolylineCREACIO")
        polyline.forEach { coordenada ->
            Log.d("aris","Latitud: ${coordenada.latitude}, Longitud: ${coordenada.longitude}")
        }

        if(ruta.size > 0) ruta.removeAt(ruta.size - 1)

        ruta.addAll(polyline)
        val polylineaux = mutableListOf<LatLng>()
        polylineaux.addAll(ruta)

        Log.d("aris","rutaCREACIO")
        ruta.forEach { coordenada ->
            Log.d("aris","Latitud: ${coordenada.latitude}, Longitud: ${coordenada.longitude}")
        }
/*
        Log.d("aris","pl")
        _pl.value?.forEach { coordenada ->
            Log.d("aris","Latitud: ${coordenada.latitude}, Longitud: ${coordenada.longitude}")
        }*/
        _pl.postValue(polylineaux.toList())

    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.openrouteservice.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun guardarRuta(){

        CoroutineScope(Dispatchers.IO).launch {
            try {
                /*
                val retrofit = Retrofit.Builder()
                    .baseUrl("https://65faaa103909a9a65b1b14c0.mockapi.io/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()*/
                val retrofit = Retrofit.Builder()
                    .baseUrl("http://nattech.fib.upc.edu:40360/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                val dist = totalDistance(ruta)
                Log.d("aris", "entro2")

                val posStartLat = posstart.value?.latitude ?: 0.0
                val posStartLng = posstart.value?.longitude ?: 0.0

                val rutaUsuari = RutaUsuari(null,"prova3", null,dist,timebicycle(dist),0,posStartLat,posStartLng)
                val call = retrofit.create(ApiRetrofit::class.java).postRoute(rutaUsuari)
                // Resto del código para manejar la respuesta

            Log.d("aris", call.isSuccessful.toString())
            if (call.isSuccessful) {
                val id_ruta = call.body()?.RuteId
                Log.d("aris", "Ruta guardada "+id_ruta)
                var i = 0
                ruta.forEach{
                    val puntsInterRuta = PuntsInterRuta("",i,it.latitude.toFloat(),it.longitude.toFloat(),id_ruta)
                    ++i
/*
                    val retrofit2 = Retrofit.Builder()
                        .baseUrl("https://65faaa103909a9a65b1b14c0.mockapi.io/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()*/
                    val retrofit2 = Retrofit.Builder()
                        .baseUrl("http://nattech.fib.upc.edu:40360/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                    val call2 = retrofit2.create(ApiRetrofit::class.java).postPuntsInter(puntsInterRuta)
                    if (call2.isSuccessful){
                        Log.d("aris", "Pun")
                    }
                }
            } else {
                Log.d("aris", "Error al guardar la ruta")
            }
            } catch (e: Exception) {
                Log.e("aris", "Error en la llamada API: ${e.message}")
            }
        }
    }
    fun totalDistance(points: List<LatLng>): Double {
        if (points.isEmpty()) {
            return 0.0
        }
        var totalDistance = 0.0
        for (i in 0 until points.size - 1) {
            totalDistance += haversineDistance(points[i], points[i + 1])
        }
        return totalDistance
    }
    fun haversineDistance(point1: LatLng, point2: LatLng): Double {
        val R = 6372.8  // Earth radius in kilometers
        val dLat = toRadians(point2.latitude - point1.latitude)
        val dLon = toRadians(point2.longitude - point1.longitude)
        val lat1 = toRadians(point1.latitude)
        val lat2 = toRadians(point2.latitude)
        val a = sin(dLat / 2) * sin(dLat / 2) + sin(dLon / 2) * sin(dLon / 2) * cos(lat1) * cos(lat2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return 2 * R * KILOMETERS_TO_METERS * c  // Convert to meters
    }
    fun timebicycle(distance: Double): Int {
        return ((VELOCITAT_MITJANA_BICICLETA*KILOMETERS_TO_METERS)/(60*distance)).toInt()
    }

    val KILOMETERS_TO_METERS = 1000.0
    val VELOCITAT_MITJANA_BICICLETA = 12


    fun Desfer(){

        val rutavella = ruta.size - polyline.size
        var polylineaux = mutableListOf<LatLng>()
        if(rutavella <= 0){
            polylineaux.add(ruta.first())
            ruta = ruta.subList(0,1)
        }
        else{
            polylineaux = ruta.subList(0,rutavella+1)
            ruta = ruta.subList(0,rutavella+1)
        }

        first = ruta[ruta.size-1].longitude.toString() + "," + ruta[ruta.size-1].latitude.toString()
        _pl.postValue(polylineaux.toList())
    }

    fun ReferInici(){
        start = true
        first = ""
        second = ""
        ruta.clear()
        polyline.clear()
        _posstart.postValue(LatLng(0.0,0.0))
        _pl.postValue(polyline.toList())
    }
}
