package com.aeromexico.aeropuertos.paperlessmobile.webService.IAPI

import com.aeromexico.aeropuertos.paperlessmobile.controlAbordaje.pojos.GetEmployeeInfoResponse
import com.aeromexico.aeropuertos.paperlessmobile.controlAbordaje.pojos.PasajerosReponse
import com.aeromexico.aeropuertos.paperlessmobile.controlAbordaje.pojos.Pax
import com.aeromexico.aeropuertos.paperlessmobile.controlAbordaje.pojos.resultInsertInfoOficial
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.*

interface ControlAbordajeAPI {

    @GET("ProcSeguridadCtrlAbordaje/GetConfirmedPax?")
    fun getBoardedPasajeros(@Query("flightReferenceNumber")flightReferenceNumber:String): Call<PasajerosReponse>

    @GET("ProcSeguridadCtrlAbordaje/GetNotBoardedPax?")
    fun getNotBoardedPasajeros(@Query("flightReferenceNumber")flightReferenceNumber:String): Call<PasajerosReponse>

    @POST("ProcSeguridadCtrlAbordaje/SavePaxByBW")
    fun setChecinPax(@Body jsonBody: JsonObject): Call<PasajerosReponse>

    @GET("ProcSeguridadCtrlAbordaje/GetEmployeeInfo?")
    fun getEmployeeInfo(@Query("flightReferenceNumber")flightReferenceNumber:String): Call<GetEmployeeInfoResponse>

    @POST("ProcSeguridadCtrlAbordaje/InsertInfoOficial")
    fun InsertInfoOficial(@Body jsonBody: JsonObject): Call<resultInsertInfoOficial>


}