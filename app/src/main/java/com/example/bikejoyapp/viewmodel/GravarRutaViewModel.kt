package com.example.bikejoyapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bikejoyapp.data.MyAppRoute
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
import kotlin.math.ceil
import kotlin.math.cos
import kotlin.math.round
import kotlin.math.sin
import kotlin.math.sqrt

open class GravarRutaViewModel : ViewModel(){
    private var start: Boolean = true
    var first: String = ""
    private var second: String = ""
    var ruta: MutableList<LatLng> = mutableListOf()
    var numpunts: MutableList<Int> = mutableListOf()


    private val _pl = MutableLiveData<List<LatLng>>()
    val pl: LiveData<List<LatLng>> = _pl

    private val _posstart = MutableLiveData<LatLng>()
    val posstart: LiveData<LatLng> = _posstart

    private val _referEnable= MutableLiveData<Boolean>()
    val referEnable: LiveData<Boolean> = _referEnable

    private val _desferEnable= MutableLiveData<Boolean>()
    val desferEnable: LiveData<Boolean> = _desferEnable

    private val _guardarEnable= MutableLiveData<Boolean>()
    val guardarEnable: LiveData<Boolean> = _guardarEnable

    private val _showDialog = MutableLiveData<Boolean>()
    val showDialog: LiveData<Boolean> = _showDialog

    private val _nomRuta = MutableLiveData<String>()
    val nomRuta: LiveData<String> = _nomRuta

    private val _descRuta = MutableLiveData<String>()
    val descRuta: LiveData<String> = _descRuta
    open fun onselected(s: String) {
        if (start) {
            start = false
            _posstart.value = LatLng(s.split(",")[1].toDouble(), s.split(",")[0].toDouble())
            //_posstart.postValue(LatLng(s.split(",")[1].toDouble(), s.split(",")[0].toDouble()))
            first = s;
            _referEnable.postValue(first != "")
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
        var npunts = ruta.size
        if(npunts > 0) ruta.removeAt(npunts - 1)
        routeResponse?.features?.first()?.geometry?.coordinates?.forEach {
            ruta.add(LatLng(it[1], it[0]))
        }
        npunts = ruta.size - npunts
        numpunts.add(npunts)
        val polylineaux = mutableListOf<LatLng>()
        polylineaux.addAll(ruta)

        Log.d("aris","rutaCREACIO")
        ruta.forEach { coordenada ->
            Log.d("aris","Latitud: ${coordenada.latitude}, Longitud: ${coordenada.longitude}")
        }
        numpunts.forEach { coordenada ->
            Log.d("aris","NumPunts: ${coordenada}")
        }
        val dist = totalDistance(ruta)
        Log.d("aris",dist.toString())
        Log.d("aris",timebicycle(dist).toString())
        _referEnable.postValue(first != "")
        _desferEnable.postValue(ruta.size > 1)
        _guardarEnable.postValue(ruta.size > 1)
        _pl.postValue(polylineaux.toList())

    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.openrouteservice.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun guardarRuta(mainViewModel: MainViewModel){

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

                if (_nomRuta.value == null || _nomRuta.value == "" || _nomRuta.value == "Ruta sense nom") {
                    Log.d("aris", _nomRuta.value.toString())
                    _nomRuta.postValue("Ruta sense nom")
                }
                else {
                    val rutaUsuari = RutaUsuari(null,_nomRuta.value,_descRuta.value,dist,timebicycle(dist),0,ruta.first().latitude,ruta.first().longitude)

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
                    mainViewModel.navigateTo(MyAppRoute.Routes)
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
        return round(distance/VELOCITAT_MITJANA_BICICLETA).toInt()
    }

    val KILOMETERS_TO_METERS = 1000.0
    val VELOCITAT_MITJANA_BICICLETA = (12*1000)/60 // 12 km/h en m/min

    fun Desfer(){
        if(numpunts.size > 0) {
            val rutavella = ruta.size - numpunts[numpunts.size-1]
            var polylineaux = mutableListOf<LatLng>()
            if(rutavella <= 0){
                polylineaux.add(ruta.first())
                ruta = ruta.subList(0,1)
            }
            else {
                polylineaux = ruta.subList(0, rutavella)
                ruta = ruta.subList(0, rutavella)
            }
            numpunts.removeAt(numpunts.size-1)
            Log.d("aris","desfer")
            ruta.forEach { coordenada ->
                Log.d("aris","Latitud: ${coordenada.latitude}, Longitud: ${coordenada.longitude}")
            }
            numpunts.forEach { coordenada ->
                Log.d("aris","NumPunts: ${coordenada}")
            }
            first = ruta[ruta.size-1].longitude.toString() + "," + ruta[ruta.size-1].latitude.toString()
            _referEnable.postValue(first != "")
            _desferEnable.postValue(ruta.size > 1)
            _guardarEnable.postValue(ruta.size > 1)
            _pl.postValue(polylineaux.toList())
        }
    }
    fun ReferInici(){
        start = true
        first = ""
        second = ""
        ruta.clear()
        _referEnable.postValue(first != "")
        _desferEnable.postValue(ruta.size > 1)
        _guardarEnable.postValue(ruta.size > 1)
        _posstart.postValue(LatLng(0.0,0.0))
        _pl.postValue(ruta.toList())
    }

    fun assignaNom(nom: String) {
        _nomRuta.value = nom
    }
    fun assignaDesc(desc: String) {
        _descRuta.value = desc
    }

    fun dialogGuardarRuta() {
        _showDialog.postValue(true)
    }
}