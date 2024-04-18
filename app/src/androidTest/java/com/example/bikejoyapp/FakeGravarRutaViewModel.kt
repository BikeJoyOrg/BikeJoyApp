package com.example.bikejoyapp.ui

import com.example.bikejoyapp.viewmodel.GravarRutaViewModel
import com.google.android.gms.maps.model.LatLng

class FakeGravarRutaViewModel : GravarRutaViewModel() {
    // Sobreescribe la función onselected para que pueda ser llamada desde el hilo principal
    private var start: Boolean = true
    private var second: String = ""
    //var ruta: MutableList<LatLng> = mutableListOf()
    //var numpunts: MutableList<Int> = mutableListOf()
    var referenable: Boolean = false
    /*
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

    }*/
}