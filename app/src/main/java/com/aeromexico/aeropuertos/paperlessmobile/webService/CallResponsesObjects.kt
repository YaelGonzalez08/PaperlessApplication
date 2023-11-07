package com.aeromexico.aeropuertos.paperlessmobile.webService

import com.google.gson.annotations.SerializedName

data class LoginErrorResponse (

    @SerializedName("Status") val status : String,
    @SerializedName("Code") val code : Int,
    @SerializedName("ResponseCode") val responseCode : String,
    @SerializedName("Message") val message : String,
    @SerializedName("Error") val error : Boolean,
    @SerializedName("ErrorMessage") val errorMessage : String,
    @SerializedName("Result") val result : String
)

data class LoginMainReponse (
    @SerializedName("Status") val status : String?,
    @SerializedName("Code") val code : Int?,
    @SerializedName("ResponseCode") val responseCode : String?,
    @SerializedName("Message") val message : String,
    @SerializedName("Error") val error : Boolean?,
    @SerializedName("ErrorMessage") val errorMessage : String?,
    @SerializedName("Result") val result : Result?
)
data class Usuario (

    @SerializedName("Nombre") val nombre : String,
    @SerializedName("ApellidoPaterno") val apellidoPaterno : String,
    @SerializedName("ApellidoMaterno") val apellidoMaterno : String,
    @SerializedName("Email") val email : String,
    @SerializedName("NumeroEmpleado") val numeroEmpleado : String,
    @SerializedName("UserGuid") val userGuid : String,
    @SerializedName("Estatus") val estatus : Boolean,
    @SerializedName("Rol") val rol : String,
    @SerializedName("RolGuid") val rolGuid : String,
    @SerializedName("Estacion") val estacion : String,
    @SerializedName("GuidEstacion") val guidEstacion : String
)
data class Result (

    @SerializedName("Usuario") val usuario : Usuario,
    @SerializedName("token") val token : String
)
data class TokenResponse (

    @SerializedName("Status") val status : String,
    @SerializedName("Code") val code : Int,
    @SerializedName("ResponseCode") val responseCode : String,
    @SerializedName("Message") val message : String,
    @SerializedName("Error") val error : Boolean,
    @SerializedName("ErrorMessage") val errorMessage : String,
    @SerializedName("Result") val result : TokenResponseString
)

data class TokenResponseString (

    @SerializedName("token") val token : String
)
