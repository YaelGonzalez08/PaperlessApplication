package com.aeromexico.aeropuertos.paperlessmobile.inspeccionEquipo.dataSource

import androidx.lifecycle.MutableLiveData
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionEquipo.pojos.CheckToServer
import com.aeromexico.aeropuertos.paperlessmobile.webService.IAPI.CheckListAPI
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.CheckListResponseObjects
import com.google.gson.Gson
import com.google.gson.JsonParser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CheckListDataSource(private val checkAPI: CheckListAPI) {

    var responseCheckList = MutableLiveData<CheckListResponseObjects>()

    fun requestSendCheckList(
        data: CheckToServer
    ) {

        checkAPI.sendCheckListRequest(
            JsonParser.parseString(
                Gson().toJson(data).toString()
            ).asJsonObject
        )
            .enqueue(object : Callback<CheckListResponseObjects> {
                override fun onResponse(
                    call: Call<CheckListResponseObjects>,
                    response: Response<CheckListResponseObjects>
                ) {
                    if (response.isSuccessful) {
                        responseCheckList.postValue(response.body())

                    } else {
                        responseCheckList.postValue(null)
                    }

                }

                override fun onFailure(call: Call<CheckListResponseObjects>, t: Throwable) {
                    responseCheckList.postValue(null)
                }
            })
    }
}