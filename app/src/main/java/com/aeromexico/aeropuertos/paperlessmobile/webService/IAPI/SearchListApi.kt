package com.aeromexico.aeropuertos.paperlessmobile.webService.IAPI

import com.aeromexico.aeropuertos.paperlessmobile.common.utils.Constants
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.SearchListResponse
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface SearchListApi {
    @POST(Constants.END_POINT_SEARCH_LIST)
    fun sendSearchList(@Body jsonObject: JsonObject): Call<SearchListResponse>
}