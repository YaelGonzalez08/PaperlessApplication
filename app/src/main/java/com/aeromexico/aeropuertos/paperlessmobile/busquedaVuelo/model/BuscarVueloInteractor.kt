package com.aeromexico.aeropuertos.paperlessmobile.busquedaVuelo.model

import androidx.lifecycle.MutableLiveData
import com.aeromexico.aeropuertos.paperlessmobile.BuildConfig
import com.aeromexico.aeropuertos.paperlessmobile.PaperlessApplication
import com.aeromexico.aeropuertos.paperlessmobile.common.database.entities.FlightEntity
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.Constants
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject

class BuscarVueloInteractor {

    var fligResponse = MutableLiveData<MutableList<FlightEntity>>()
    fun buscarVuelo( request: RequestBuscarVuelo){
        val url = BuildConfig.PAPERLESS_URL_API + Constants.VUELOS + Constants.BUSCAR_VUELOS
        var flightList = mutableListOf<FlightEntity>()
        var jsonObject= JSONObject( Gson().toJson(request))
        val jsonObjectRequest =  JsonObjectRequest(Request.Method.POST, url, jsonObject, { response ->
            print(response.toString())
            val status = response.optInt("Code")
            if(status == 200){
                val jsonList = response.optJSONObject("Result")?.getJSONArray("Vuelos")?.toString()
                if(jsonList != null){
                    val mutableList = object : TypeToken<MutableList<FlightEntity>>(){}.type
                    flightList = Gson().fromJson<MutableList<FlightEntity>>(jsonList,mutableList)
                    fligResponse.postValue(flightList)
//                    BuscarVueloViewModel().setFlights(flightList)
//                    callback(flightList)
//                    return@JsonObjectRequest
                }
            }
//            callback(flightList)
        },{

            // TODO: 20/04/2021
            it.printStackTrace()

//            callback(flightList)
        })
        PaperlessApplication.paperlessAPI.addToRequestQueue(jsonObjectRequest)
    }
}