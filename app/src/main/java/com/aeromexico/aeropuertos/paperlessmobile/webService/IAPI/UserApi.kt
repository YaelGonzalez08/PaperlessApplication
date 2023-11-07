package com.aeromexico.aeropuertos.paperlessmobile.webService.IAPI

import androidx.lifecycle.LiveData
import com.aeromexico.aeropuertos.paperlessmobile.login.pojos.DominiosResponse
import com.aeromexico.aeropuertos.paperlessmobile.webService.LoginMainReponse
import com.aeromexico.aeropuertos.paperlessmobile.webService.TokenResponse
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface UserApi {
    @POST("Autenticacion/AutenticarUsuario/")
    fun loginRequest(   @Body jsonBody: JsonObject): Call<LoginMainReponse>

    @GET("Token/Renovar/")
    fun renewToken():Call<TokenResponse>

    @GET("Autenticacion/GetDominiosLogin")
    fun getdomains():Call<DominiosResponse>
}