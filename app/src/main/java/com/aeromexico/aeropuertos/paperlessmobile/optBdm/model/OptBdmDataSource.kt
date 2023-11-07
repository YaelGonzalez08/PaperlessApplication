package com.aeromexico.aeropuertos.paperlessmobile.optBdm.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.RequestState
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronavePrimerVuelo.entities.RequestFirstFlightForm
import com.aeromexico.aeropuertos.paperlessmobile.optBdm.entities.RequestOptBdmForm
import com.aeromexico.aeropuertos.paperlessmobile.webService.IAPI.OptBdmAPI
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.*
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.gson.Gson
import com.google.gson.JsonParser
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class OptBdmDataSource(private val api: OptBdmAPI) {
    var responseState = MutableLiveData<RequestState>()
    var responseOptBdm = MutableLiveData<ResponseOptBdmBase>()

    var responseSend = MutableLiveData<SendResponse_Base>()

    fun getDatos(flightReferenceNumber: Long){
        responseState.value = RequestState(RequestState.REQ_IN_PROGRESS)

        api.getInfo(flightReferenceNumber).enqueue(object : Callback<ResponseOptBdmBase>{
            override fun onFailure(call: Call<ResponseOptBdmBase>, t: Throwable) {
                responseState.value = RequestState(RequestState.REQ_BAD)
                Log.i("OptBdm", t.message.toString())
                FirebaseCrashlytics.getInstance().recordException(t)
            }

            override fun onResponse(
                call: Call<ResponseOptBdmBase>,
                response: Response<ResponseOptBdmBase>
            ) {
                if(response.isSuccessful){
                    responseState.value = RequestState(RequestState.REQ_OK)
                    Log.i("OptBdm Successfull", response.body().toString())
                    responseOptBdm.postValue(response.body())
                }
                else {
                    responseState.value = RequestState(RequestState.REQ_BAD)
                    try {
                        val jObjError: JSONObject? = JSONObject(response.errorBody()!!.string())
                        Log.i("OptBdm Bad", jObjError.toString())
                        val gson = Gson()
                        responseOptBdm.postValue(
                            gson.fromJson(
                                jObjError.toString(),
                                ResponseOptBdmBase::class.java
                            )
                        )
                    }
                    catch(e: Exception){
                        responseOptBdm.postValue(null)
                        FirebaseCrashlytics.getInstance().recordException(e)
                    }
                }
            }
        })
    }

    fun save(requestFirstFlightForm: RequestOptBdmForm) {
        responseState.value = RequestState(RequestState.REQ_IN_PROGRESS)
        api.postFirmas(
            JsonParser.parseString(
                Gson().toJson(
                    requestFirstFlightForm
                ).toString()
            ).asJsonObject
        )
            .enqueue(object : Callback<SendResponse_Base>{
                override fun onResponse(
                    call: Call<SendResponse_Base>,
                    response: Response<SendResponse_Base>
                ) {
                    if(response.isSuccessful) {
                        responseSend.postValue(response.body())
                        responseState.value = RequestState(RequestState.REQ_OK)
                    }
                    else {

                        responseState.value = RequestState(RequestState.REQ_BAD)
                        try{

                            val jObjError: JSONObject? = JSONObject(response.errorBody()?.string())
                            Log.i("OPTBDM", jObjError.toString())
                            val gson = Gson()
                            responseSend.postValue(
                                gson.fromJson(
                                    jObjError.toString(),
                                    SendResponse_Base::class.java
                                )
                            )
                        }
                        catch (e: Exception){
                            responseSend.postValue(null)
                            FirebaseCrashlytics.getInstance().recordException(e)
                            //.log("Error saveDetalleLir ---> Mensajes Operacionales WS ----> ${e.stackTrace}")
                        }
                    }
                }

                override fun onFailure(call: Call<SendResponse_Base>, t: Throwable) {
                    responseOptBdm.postValue(null)
                    responseState.value = RequestState(RequestState.REQ_BAD)
                }
            })
    }
}