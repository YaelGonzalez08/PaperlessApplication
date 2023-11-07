package com.aeromexico.aeropuertos.paperlessmobile.webService.IAPI

import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.CheckListResponseObjects
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface CheckListAPI {

    @POST("CheckList/InsertCheckList/")
    fun sendCheckListRequest(@Body jsonBody: JsonObject): Call<CheckListResponseObjects>
}