package com.aeromexico.aeropuertos.paperlessmobile.webService.IAPI

import com.aeromexico.aeropuertos.paperlessmobile.common.utils.Constants
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.IATASResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface IATAApi {
    @GET(Constants.END_POINT_GET_IATAS)
    fun getIatas():Call<IATASResponse>
}