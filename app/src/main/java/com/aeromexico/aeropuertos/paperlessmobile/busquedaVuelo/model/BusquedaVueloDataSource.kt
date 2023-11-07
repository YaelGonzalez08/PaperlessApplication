package com.aeromexico.aeropuertos.paperlessmobile.busquedaVuelo.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.RequestState
import com.aeromexico.aeropuertos.paperlessmobile.webService.IAPI.VueloAPI
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.ResponseBase
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BusquedaVueloDataSource(private val vueloAPI: VueloAPI) {
    var responseState = MutableLiveData<RequestState>()
    var responseBuscarVuelo = MutableLiveData<ResponseBase>()

    fun requestBuscarVuelo(request: RequestBuscarVuelo){
        responseState.value = RequestState(RequestState.REQ_IN_PROGRESS)
        var jsonObject= JsonObject()
        jsonObject.addProperty("Origen",request.Origen)
        jsonObject.addProperty("Destino", request.Destino)
        jsonObject.addProperty("NumVuelo",request.NumVuelo)
        jsonObject.addProperty("FechaVuelo", request.FechaVuelo)
        vueloAPI.buscarVuelo(jsonObject).enqueue(object : Callback<ResponseBase>{
            override fun onFailure(call: Call<ResponseBase>, t: Throwable) {

                responseState.value = RequestState(RequestState.REQ_BAD)
                Log.i("response busqeuda vuelo", t.message.toString())
            }

            override fun onResponse(call: Call<ResponseBase>,
                                    response: Response<ResponseBase>) {
                if (response.isSuccessful){
                    responseState.value = RequestState(RequestState.REQ_OK)
                    Log.i("response busqeuda vuelo", response.body().toString())
                    responseBuscarVuelo.postValue(response.body())
                }
                else{
                    responseState.value = RequestState(RequestState.REQ_BAD)
                    val jObjError: JSONObject? = JSONObject(response.errorBody()!!.string())
                    Log.i("response Busqueda Vuelo", jObjError.toString())
                    val gson = Gson()
                    responseBuscarVuelo.postValue(
                            gson.fromJson(
                                    jObjError.toString(),
                                ResponseBase::class.java
                            )
                    )
                }
            }
        })
    }
}