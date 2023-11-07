package com.aeromexico.aeropuertos.paperlessmobile.webService.IAPI

import com.aeromexico.aeropuertos.paperlessmobile.comunicadosACK.pojos.ComunicadoPdfResponse
import com.aeromexico.aeropuertos.paperlessmobile.comunicadosACK.pojos.ComunicadosResponse
import com.aeromexico.aeropuertos.paperlessmobile.comunicadosACK.pojos.RequestEnvioCuesitonario
import com.aeromexico.aeropuertos.paperlessmobile.comunicadosACK.pojos.ResponseEnvioCuestionario
import com.google.gson.JsonObject
import getFileResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ComunicadosAPI {

    @GET("ACK/GetComunicados/Honeywell")
    fun getComunicados(): Call<ComunicadosResponse>

    @POST("ACK/GetDetalleComunicado")
    fun getComunicadoById(@Query("IdComunicado") IdComunicado: Int): Call<ComunicadoPdfResponse>

    @POST("ACK/InsertComunicadoResult")
    fun sendComunicadoResuelto(@Body jsonBody: JsonObject): Call<ResponseEnvioCuestionario>

    @POST("ACK/GetArchivo")
    fun getDownloadDocumentById(@Query("idArchivo") idArchivo: Int):Call<getFileResponse>


}