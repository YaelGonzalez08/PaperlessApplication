package com.aeromexico.aeropuertos.paperlessmobile.webService.IAPI

import com.aeromexico.aeropuertos.paperlessmobile.common.utils.Constants
import com.aeromexico.aeropuertos.paperlessmobile.ordenCargaCombustibles.entities.ResponseCheckOrdenCarga
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.OrdeCargaResponse
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface OrdenCargaAPI {

    @POST(Constants.END_POINT_ORDEN_SEND_FORM)
    fun sendOrdenCarga(@Body jsonBody: JsonObject): Call<OrdeCargaResponse>

    @POST(Constants.END_POINT_ORDEN_CHECK_FORM)
    fun checkOrdenCarga(@Body jsonBody: JsonObject): Call<ResponseCheckOrdenCarga>
}