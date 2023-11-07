package com.aeromexico.aeropuertos.paperlessmobile.webService.IAPI

import com.aeromexico.aeropuertos.paperlessmobile.common.utils.Constants
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.ResponseBase
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.VueloManualResponse
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface VueloAPI {


    @POST(Constants.VUELOS+Constants.BUSCAR_VUELOS)
//    @Headers({
//        "Authorization: Bearer fgggggggggg",
//    })
    fun buscarVuelo(@Body jsonBody: JsonObject): Call<ResponseBase>

    @POST("${Constants.VUELOS}${Constants.VUELO_MANUAL}")
    fun vueloManual(@Body jsonBody: JsonObject): Call<VueloManualResponse>
}