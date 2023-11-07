package com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronavePrimerVuelo.dataSource

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.RequestState
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronavePrimerVuelo.entities.RequestFirstFlightForm
import com.aeromexico.aeropuertos.paperlessmobile.webService.IAPI.PrimerVueloDiaApi
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.PrimerVueloResponse
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.PrimerVueloResponseGET
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.ResponseMOBaseDetalle
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.gson.Gson
import com.google.gson.JsonParser
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class PrimerVueloDataSource(private val primerVueloDiaApi: PrimerVueloDiaApi) {
    var responsePrimerVuelo = MutableLiveData<PrimerVueloResponse>()
    var responseGET = MutableLiveData<PrimerVueloResponseGET>()

    var responseState = MutableLiveData<RequestState>()

    fun insertPrimerVuelo(requestFirstFlightForm: RequestFirstFlightForm) {
        primerVueloDiaApi.sendPrimerVueloDia(
            JsonParser.parseString(
                Gson().toJson(
                    requestFirstFlightForm
                ).toString()
            ).asJsonObject
        )
            .enqueue(object : Callback<PrimerVueloResponse>{
            override fun onResponse(
                call: Call<PrimerVueloResponse>,
                response: Response<PrimerVueloResponse>
            ) {
                if(response.isSuccessful)
                    responsePrimerVuelo.postValue(response.body())
                else
                    responsePrimerVuelo.postValue(null)
            }

            override fun onFailure(call: Call<PrimerVueloResponse>, t: Throwable) {
                responsePrimerVuelo.postValue(null)
            }
        })
    }

    fun getInfoPrimervuelo(flightReferenceNumber: Long){
        responseState.value = RequestState(RequestState.REQ_IN_PROGRESS)
        primerVueloDiaApi.getInfoDB(flightReferenceNumber).enqueue(object : Callback<PrimerVueloResponseGET>{

            override fun onResponse(
                call: Call<PrimerVueloResponseGET>,
                response: Response<PrimerVueloResponseGET>
            ) {
                if (response.isSuccessful) {
                    Log.i("Get info primer vuelo", response.body().toString())
                    responseState.value = RequestState(RequestState.REQ_OK)
                    responseGET.postValue(response.body())
                }
                else {
                    try {
//                        Log.i("Get info pv bad", response.body().toString())
                        responseState.value = RequestState(RequestState.REQ_BAD)
                        val jObjError: JSONObject? = JSONObject(response.errorBody()?.string())
                        responseGET.postValue(
                            Gson().fromJson(
                                jObjError.toString(),
                                PrimerVueloResponseGET::class.java
                            )
                        )
                    }
                    catch (e:Exception){
                        Log.i("Get info pv bad", response.body().toString())
                        responseGET.postValue(null)
                        FirebaseCrashlytics.getInstance().recordException(e)
                    }

                }
            }

            override fun onFailure(call: Call<PrimerVueloResponseGET>, t: Throwable) {
                Log.i("Get info pv bad bad",t.message.toString())
                responseState.value = RequestState(RequestState.REQ_BAD)
//                responseGET.postValue(null)
                FirebaseCrashlytics.getInstance().recordException(t)
            }
        })
    }
}