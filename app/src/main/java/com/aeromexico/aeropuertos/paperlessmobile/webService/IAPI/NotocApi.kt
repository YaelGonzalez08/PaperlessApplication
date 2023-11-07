package com.aeromexico.aeropuertos.paperlessmobile.webService.IAPI

import com.aeromexico.aeropuertos.paperlessmobile.common.utils.Constants
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.ResponseNotocBase
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface NotocApi {
    @POST(Constants.NOTOC+ Constants.GET_NOTOC)
    fun getNotoc(@Query("FlightReferenceNumber") flightReferenceNumber: Long): Call<ResponseNotocBase>

    @POST(Constants.NOTOC+Constants.SET_DETALLE_NOTOC)
    fun sendNotoc(@Body jsonBody: JsonObject): Call<ResponseNotocBase>
}