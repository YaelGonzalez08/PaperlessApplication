package com.aeromexico.aeropuertos.paperlessmobile.notoc.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.RequestState
import com.aeromexico.aeropuertos.paperlessmobile.notoc.entities.RequestNotoc
import com.aeromexico.aeropuertos.paperlessmobile.webService.IAPI.NotocApi
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.ResponseNotocBase
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.gson.Gson
import com.google.gson.JsonParser
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class NotocDataSource(private val api: NotocApi) {
    var responseStateGetInfo = MutableLiveData<RequestState>()
    var responseStateSendNotoc = MutableLiveData<RequestState>()
    var responseInfoNotoc = MutableLiveData<ResponseNotocBase>()
    var responseSendNotoc = MutableLiveData<ResponseNotocBase>()

    fun getInfoNotoc(flightReferenceNumber: Long){
        responseStateGetInfo.value = RequestState(RequestState.REQ_IN_PROGRESS)

        api.getNotoc(flightReferenceNumber).enqueue(object : Callback<ResponseNotocBase> {
            override fun onFailure(call: Call<ResponseNotocBase>, t: Throwable) {

                responseStateGetInfo.value = RequestState(RequestState.REQ_BAD)
                Log.e("Notoc", t.message.toString())
                FirebaseCrashlytics.getInstance().recordException(t)
            }

            override fun onResponse(call: Call<ResponseNotocBase>, response: Response<ResponseNotocBase>) {
                if(response.isSuccessful){
                    responseStateGetInfo.value = RequestState(RequestState.REQ_OK)
                    Log.i("Notoc", response.body().toString())
                    responseInfoNotoc.postValue(response.body())
                }
                else {
                    responseStateGetInfo.value = RequestState(RequestState.REQ_BAD)
                    try {
                        val jObjError: JSONObject? = JSONObject(response.errorBody()!!.string())
                        Log.i("Notoc", jObjError.toString())
                        val gson = Gson()
                        responseInfoNotoc.postValue(
                            gson.fromJson(
                                jObjError.toString(),
                                ResponseNotocBase::class.java
                            )
                        )
                    }
                    catch(e: Exception){
                        responseInfoNotoc.postValue(null)
                        FirebaseCrashlytics.getInstance().recordException(e)
                    }
                }
            }
        })
    }

    fun sendInfoNotoc(notoc: RequestNotoc){
        responseStateSendNotoc.value = RequestState(RequestState.REQ_IN_PROGRESS)
        api.sendNotoc(
            JsonParser.parseString(
            Gson().toJson(
                notoc
            ).toString()
        ).asJsonObject).enqueue(object : Callback<ResponseNotocBase>{

            override fun onResponse(
                call: Call<ResponseNotocBase>,
                response: Response<ResponseNotocBase>
            ) {
                if(response.isSuccessful){
                    responseStateSendNotoc.value = RequestState(RequestState.REQ_OK)
                    Log.i("Send Notoc", response.body().toString())
                    responseSendNotoc.postValue(response.body())
                }
                else {
                    responseStateSendNotoc.value = RequestState(RequestState.REQ_BAD)
                    try {
                        val jObjError: JSONObject? = JSONObject(response.errorBody()!!.string())
                        Log.i("Send Notoc", jObjError.toString())
                        val gson = Gson()
                        responseSendNotoc.postValue(
                            gson.fromJson(
                                jObjError.toString(),
                                ResponseNotocBase::class.java
                            )
                        )
                    }
                    catch(e: Exception){
                        responseSendNotoc.postValue(null)
                        FirebaseCrashlytics.getInstance().recordException(e)
                    }
                }
            }

            override fun onFailure(call: Call<ResponseNotocBase>, t: Throwable) {
                responseStateSendNotoc.value = RequestState(RequestState.REQ_BAD)
                Log.i("Send Notoc", t.message.toString())
                FirebaseCrashlytics.getInstance().recordException(t)
            }
        })
    }
}