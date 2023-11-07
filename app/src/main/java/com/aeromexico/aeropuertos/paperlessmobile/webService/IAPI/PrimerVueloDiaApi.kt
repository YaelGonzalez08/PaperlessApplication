package com.aeromexico.aeropuertos.paperlessmobile.webService.IAPI

import com.aeromexico.aeropuertos.paperlessmobile.common.utils.Constants
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.PrimerVueloResponse
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.PrimerVueloResponseGET
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface PrimerVueloDiaApi {

    @POST(Constants.END_POINT_SEND_FORM)
    fun sendPrimerVueloDia(@Body jsonBody: JsonObject): Call<PrimerVueloResponse>

    @GET(Constants.PRIMERVUELO_GET_INFO)
    fun getInfoDB(@Query("flightReferenceNumber") flightReferenceNumber: Long): Call<PrimerVueloResponseGET>
}