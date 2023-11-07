package com.aeromexico.aeropuertos.paperlessmobile.manifiestoCarga.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.RequestState
import com.aeromexico.aeropuertos.paperlessmobile.webService.IAPI.ManifiestoCargaAPI
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.ResponseManifiestoCargaBase
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.gson.Gson
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class ManifiestoCargaDataSource(private val api: ManifiestoCargaAPI) {
    var responseState = MutableLiveData<RequestState>()
    var responseMC = MutableLiveData<ResponseManifiestoCargaBase>()

    fun getManifiestoCarga(flightReferenceNumber: Long){
        responseState.value = RequestState(RequestState.REQ_IN_PROGRESS)

        api.getManifiestoPDF(flightReferenceNumber).enqueue(object : Callback<ResponseManifiestoCargaBase>{
            override fun onResponse(
                call: Call<ResponseManifiestoCargaBase>,
                response: Response<ResponseManifiestoCargaBase>
            ) {
                if(response.isSuccessful){
                    responseState.value = RequestState(RequestState.REQ_OK)
                    Log.i("ManifiestoCarga", response.body().toString())
                    responseMC.postValue(response.body())
                }
                else {
                    responseState.value = RequestState(RequestState.REQ_BAD)
                    try {
                        val jObjError: JSONObject? = JSONObject(response.errorBody()!!.string())
                        Log.e("ManifiestoCarga", jObjError.toString())
                        val gson = Gson()
                        responseMC.postValue(
                            gson.fromJson(
                                jObjError.toString(),
                                ResponseManifiestoCargaBase::class.java
                            )
                        )
                    }
                    catch(e: Exception){
                        responseMC.postValue(null)
                        FirebaseCrashlytics.getInstance().recordException(e)
                    }
                }
            }

            override fun onFailure(call: Call<ResponseManifiestoCargaBase>, t: Throwable) {
                responseState.value = RequestState(RequestState.REQ_BAD)
                Log.e("ManifiestoCarga", t.message.toString())
                FirebaseCrashlytics.getInstance().recordException(t)
            }
        })
    }
}