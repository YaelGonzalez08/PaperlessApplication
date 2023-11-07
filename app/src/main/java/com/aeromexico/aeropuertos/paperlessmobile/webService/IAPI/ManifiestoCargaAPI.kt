package com.aeromexico.aeropuertos.paperlessmobile.webService.IAPI

import com.aeromexico.aeropuertos.paperlessmobile.common.utils.Constants
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.ResponseMOBase
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.ResponseManifiestoCargaBase
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ManifiestoCargaAPI {
    @GET(Constants.MANIFIESTOCARGA+ Constants.GET_MANIFIESTO)
    fun getManifiestoPDF(@Query("flightReferenceNumber") flightReferenceNumber: Long): Call<ResponseManifiestoCargaBase>
}