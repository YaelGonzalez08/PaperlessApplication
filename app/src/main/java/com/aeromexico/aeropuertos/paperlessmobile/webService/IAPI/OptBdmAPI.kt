package com.aeromexico.aeropuertos.paperlessmobile.webService.IAPI

import com.aeromexico.aeropuertos.paperlessmobile.common.utils.Constants
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.ResponseOptBdmBase
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.SendResponse_Base
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface OptBdmAPI {
    @GET(Constants.OPT_EDM+ Constants.GETINFOOPT)
    fun getInfo(@Query("flightReferenceNumber") flightReferenceNumber: Long): Call<ResponseOptBdmBase>

    @POST(Constants.OPT_EDM+ Constants.FIRMAROPT)
    fun postFirmas(@Body jsonBody: JsonObject): Call<SendResponse_Base>
}