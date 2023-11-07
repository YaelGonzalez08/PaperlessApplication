package com.aeromexico.aeropuertos.paperlessmobile.mensajesOperacionales.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.aeromexico.aeropuertos.paperlessmobile.common.database.entities.ModificarDetalleLirEntity
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.RequestState
import com.aeromexico.aeropuertos.paperlessmobile.webService.IAPI.MensajesOperacionalesAPI
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.ResponseMOBase
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.ResponseMOBaseDetalle
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class MensajesOperacionalesDataSource(private val api: MensajesOperacionalesAPI) {
    var responseState = MutableLiveData<RequestState>()
    var responseStateEnvioFirma = MutableLiveData<RequestState>()
    var responseMO = MutableLiveData<ResponseMOBase>()
    var responseMODetalle = MutableLiveData<ResponseMOBaseDetalle>()

    fun getMensajesOps(flightReferenceNumber: Long){
        responseState.value = RequestState(RequestState.REQ_IN_PROGRESS)

        api.getMensajesOps(flightReferenceNumber).enqueue(object : Callback<ResponseMOBase>{
            override fun onFailure(call: Call<ResponseMOBase>, t: Throwable) {

                responseState.value = RequestState(RequestState.REQ_BAD)
                Log.i("MsjsOperacionales", t.message.toString())
                FirebaseCrashlytics.getInstance().recordException(t)
            }

            override fun onResponse(call: Call<ResponseMOBase>, response: Response<ResponseMOBase>) {
                if(response.isSuccessful){
                    responseState.value = RequestState(RequestState.REQ_OK)
                    Log.i("MsjsOperacionales", response.body().toString())
                    responseMO.postValue(response.body())
                }
                else {
                    responseState.value = RequestState(RequestState.REQ_BAD)
                    try {
                        val jObjError: JSONObject? = JSONObject(response.errorBody()!!.string())
                        Log.i("MsjOperacionales", jObjError.toString())
                        val gson = Gson()
                        responseMO.postValue(
                            gson.fromJson(
                                jObjError.toString(),
                                ResponseMOBase::class.java
                            )
                        )
                    }
                    catch(e: Exception){
                        responseMO.postValue(null)
                        FirebaseCrashlytics.getInstance().recordException(e)
                    }
                }
            }
        })
    }

    fun saveDetalleLir(detalleLirEntity: ModificarDetalleLirEntity){
        responseStateEnvioFirma.value = RequestState(RequestState.REQ_IN_PROGRESS)
        val jsonObj_ = JsonObject()
        jsonObj_.addProperty("nombreRampa", detalleLirEntity.nombreRampa.toString().trim())
        jsonObj_.addProperty("numEmpleadoRampa", detalleLirEntity.numEmpleadoRampa)
        jsonObj_.addProperty("firmaRampa", detalleLirEntity.firmaRampa.trim())
        jsonObj_.addProperty("remarks", detalleLirEntity.remarks.trim())
//        jsonObj_.addProperty("nombreDespacho", detalleLirEntity.nombreDespacho.trim())
//        jsonObj_.addProperty("numEmpleadoDespacho", detalleLirEntity.numEmpleadoDespacho)
//        jsonObj_.addProperty("firmaDespacho", detalleLirEntity.firmaDespacho.trim())
        jsonObj_.addProperty("CreadoPor", detalleLirEntity.CreadoPor)
        jsonObj_.addProperty("FechaCreacion", detalleLirEntity.fechaCreacion)
        jsonObj_.addProperty("idMensajeOps", detalleLirEntity.idMensajeOps)
        api.setDetalleMsjOps(jsonObj_).enqueue(object : Callback<ResponseMOBaseDetalle> {
            override fun onFailure(call: Call<ResponseMOBaseDetalle>, t: Throwable) {
                responseStateEnvioFirma.value = RequestState(RequestState.REQ_BAD)
                Log.i("MsjsOperacionales", t.message.toString())
                FirebaseCrashlytics.getInstance().recordException(t)
            }

            override fun onResponse(call: Call<ResponseMOBaseDetalle>, response: Response<ResponseMOBaseDetalle>) {
                if(response.isSuccessful){
                    responseStateEnvioFirma.value = RequestState(RequestState.REQ_OK)
                    Log.i("MsjsOperacionales", response.body().toString())
                    responseMODetalle.postValue(response.body())
                }
                else {
                    responseStateEnvioFirma.value = RequestState(RequestState.REQ_BAD)
                    try{

                        val jObjError: JSONObject? = JSONObject(response.errorBody()?.string())
                        Log.i("MsjOperacionales", jObjError.toString())
                        val gson = Gson()
                        responseMODetalle.postValue(
                            gson.fromJson(
                                jObjError.toString(),
                                ResponseMOBaseDetalle::class.java
                            )
                        )
                    }
                    catch (e: Exception){
                        responseMODetalle.postValue(null)
                        FirebaseCrashlytics.getInstance().recordException(e)
                            //.log("Error saveDetalleLir ---> Mensajes Operacionales WS ----> ${e.stackTrace}")
                    }
                }
            }
        })
    }
}