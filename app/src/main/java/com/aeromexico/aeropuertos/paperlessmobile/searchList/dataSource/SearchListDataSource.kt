package com.aeromexico.aeropuertos.paperlessmobile.searchList.dataSource

import androidx.lifecycle.MutableLiveData
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.RequestState
import com.aeromexico.aeropuertos.paperlessmobile.searchList.entities.InsertSearchList
import com.aeromexico.aeropuertos.paperlessmobile.webService.IAPI.SearchListApi
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.SearchListResponse
import com.google.gson.Gson
import com.google.gson.JsonParser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchListDataSource(private val searchListApi: SearchListApi) {
    var responseSearchList = MutableLiveData<SearchListResponse>()
    var responseState = MutableLiveData<RequestState>()

    fun insertSearchList(insertSearchList: InsertSearchList){
        searchListApi.sendSearchList(
            JsonParser.parseString(
                Gson().toJson(
                    insertSearchList
                ).toString()
            ).asJsonObject
        )
            .enqueue(object : Callback<SearchListResponse>{
                override fun onResponse(
                    call: Call<SearchListResponse>,
                    response: Response<SearchListResponse>
                ) {
                    if(response.isSuccessful)
                        responseSearchList.postValue(response.body())
                    else
                        responseSearchList.postValue(null)
                }

                override fun onFailure(call: Call<SearchListResponse>, t: Throwable) {
                    responseSearchList.postValue(null)
                }

            })
    }

}