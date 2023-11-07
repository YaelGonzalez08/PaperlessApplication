package com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronavePrimerVuelo.dataSource

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.RequestState
import com.aeromexico.aeropuertos.paperlessmobile.webService.IAPI.VueloAPI
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.VueloManualResponse
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VueloManualDataSource(private val vueloManualApi: VueloAPI) {

    var responseState = MutableLiveData<RequestState>()
    var responseVueloManual = MutableLiveData<VueloManualResponse>()

    fun insertarVueloManual(numVuelo: String, origen: String, destino: String, aerolinea: String, matricula: String, eta: String, etd: String, fechaVuelo: String, equipo: String){
        responseState.value = RequestState(RequestState.REQ_IN_PROGRESS)

        val jsonObj = JsonObject()
        jsonObj.addProperty("NumVuelo", numVuelo)
        jsonObj.addProperty("Origen", origen)
        jsonObj.addProperty("Destino", destino)
        jsonObj.addProperty("Aerolinea", aerolinea)
        jsonObj.addProperty("Matricula", matricula)
        jsonObj.addProperty("ETA", eta)
        jsonObj.addProperty("ETD", etd)
        jsonObj.addProperty("FechaVuelo", fechaVuelo)
        jsonObj.addProperty("Equipo", equipo)

        Log.e("VueloManual", "$jsonObj")

        vueloManualApi.vueloManual(jsonObj).enqueue(object : Callback<VueloManualResponse> {
            override fun onResponse(
                call: Call<VueloManualResponse>,
                response: Response<VueloManualResponse>
            ) {
                if (response.isSuccessful){
                    responseState.value = RequestState(RequestState.REQ_OK)
                    //Log.i("response vuelo manual", response.body().toString())
                    responseVueloManual.postValue(response.body())
                } else {
                    responseState.value = RequestState(RequestState.REQ_BAD)

                    try{
                        val jSonErr: JSONObject = JSONObject(response.errorBody().toString())
                        Log.i("response login", jSonErr.toString())
                        val gson = Gson()
                        responseVueloManual.postValue(gson.fromJson(jSonErr.toString(), VueloManualResponse::class.java))
                    } catch (e: Exception){
                        Log.i("EVueloManualDataSource", e.message.toString())
                    }
                }
            }

            override fun onFailure(call: Call<VueloManualResponse>, t: Throwable) {
                responseState.value = RequestState(RequestState.REQ_BAD)
                responseVueloManual.postValue(null)
            }

        })
    }
}