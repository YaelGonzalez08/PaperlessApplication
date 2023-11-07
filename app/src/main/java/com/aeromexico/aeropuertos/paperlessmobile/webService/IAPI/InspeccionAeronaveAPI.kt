package com.aeromexico.aeropuertos.paperlessmobile.webService.IAPI

import com.aeromexico.aeropuertos.paperlessmobile.common.utils.Constants
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronaveREF.pojos.ResponseExisteInspeccion
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.CreateInspeccionResponsaBase
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.GetTiposAveriaResponsaBase
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.GET
import retrofit2.http.HTTP

interface InspeccionAeronaveAPI {


    @POST(Constants.INSPECCION_AERONAVE+ Constants.CREATE_INSPECCION_AERONAVE)
    fun createInspeccionAeronave(@Body jsonBody: JsonObject): Call<CreateInspeccionResponsaBase>


    @POST(Constants.INSPECCION_AERONAVE+ Constants.GET_TIPOS_AVERIA_INSPECCION_AERONAVE)
    fun getTiposAveria(@Body jsonBody: JsonObject): Call<GetTiposAveriaResponsaBase>

    @POST("Inspeccion/GetInspeccion")
    fun GetInspeccion(@Body jsonBody: JsonObject): Call<ResponseExisteInspeccion>

}