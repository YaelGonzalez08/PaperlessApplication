package com.aeromexico.aeropuertos.paperlessmobile.searchList.dataSource

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.RequestState
import com.aeromexico.aeropuertos.paperlessmobile.searchList.entities.GetInfo
import com.aeromexico.aeropuertos.paperlessmobile.searchList.entities.ResponseGetInfo
import com.aeromexico.aeropuertos.paperlessmobile.webService.IAPI.GetInfoApi
import com.google.gson.Gson
import com.google.gson.JsonParser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetInfoDataSource(private val getInfoApi: GetInfoApi) {
    var responseState = MutableLiveData<RequestState>()
    var responseGetInfo = MutableLiveData<ResponseGetInfo>()

    fun getInfo(guid: String) {
        getInfoApi.getInfo(
            JsonParser.parseString(
                Gson().toJson(
                    GetInfo(guid)
                ).toString()
            ).asJsonObject
        ).enqueue(object : Callback<ResponseGetInfo> {
            override fun onResponse(
                call: Call<ResponseGetInfo>,
                response: Response<ResponseGetInfo>
            ) {
                if (response.isSuccessful) {
                    responseState.value = RequestState(RequestState.REQ_OK)
                    Log.i("response login", response.body().toString())
                    responseGetInfo.postValue(response.body())
                } else
                    responseState.value = RequestState(RequestState.REQ_BAD)
            }

            override fun onFailure(call: Call<ResponseGetInfo>, t: Throwable) {
                responseState.value = RequestState(RequestState.REQ_BAD)
            }

        })
    }
}