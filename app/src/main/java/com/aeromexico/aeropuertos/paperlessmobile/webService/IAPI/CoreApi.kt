package com.aeromexico.aeropuertos.paperlessmobile.webService.IAPI

import com.aeromexico.aeropuertos.paperlessmobile.common.utils.Constants
import com.aeromexico.aeropuertos.paperlessmobile.metar.objects.MetarResponse
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.CORE_Base
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.EquipoResponseObjects
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.PDFB64FILEResponse
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.PdfImageResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CoreApi {
    @GET(Constants.CORE+ Constants.GET_EMPLEADO)
    fun getEmpleado(@Query("noEmpleado") noEmpleado: String): Call<CORE_Base>

    @GET("${Constants.CHECKLIST}${Constants.GETEQUIPO}")
    fun getEquipo(@Query ("NoEquipo") NoEquipo:String):Call<EquipoResponseObjects>

    @GET("Test/GetPDF_B64/1")
    fun getPDFFileB64():Call<PDFB64FILEResponse>

    @GET("Metar/GetMetar")
    fun getMetar(@Query ("GuidVuelo") GuidVuelo:String):Call<MetarResponse>

}