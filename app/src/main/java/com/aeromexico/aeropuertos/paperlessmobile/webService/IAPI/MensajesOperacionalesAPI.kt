package com.aeromexico.aeropuertos.paperlessmobile.webService.IAPI

import com.aeromexico.aeropuertos.paperlessmobile.common.utils.Constants
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.ResponseBase
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.ResponseMOBase
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.ResponseMOBaseDetalle
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.*

interface MensajesOperacionalesAPI {
    @GET(Constants.MENSAJES_OPERACIONALES+Constants.GET_MENSAJES_OPERACIONALES)
    fun getMensajesOps(@Query("flightReferenceNumber") flightReferenceNumber: Long): Call<ResponseMOBase>

    @POST(Constants.MENSAJES_OPERACIONALES+Constants.SET_DETALLE_MSJ_OPS)
    fun setDetalleMsjOps(@Body jsonBody: JsonObject): Call<ResponseMOBaseDetalle>
}