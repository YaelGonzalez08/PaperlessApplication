package com.aeromexico.aeropuertos.paperlessmobile.webService.IAPI

import com.aeromexico.aeropuertos.paperlessmobile.common.utils.Constants
import com.aeromexico.aeropuertos.paperlessmobile.searchList.entities.ResponseGetInfo
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface GetInfoApi {
    @POST(Constants.END_POINT_GET_INFO)
    fun getInfo(@Body jsonBody: JsonObject): Call<ResponseGetInfo>
}