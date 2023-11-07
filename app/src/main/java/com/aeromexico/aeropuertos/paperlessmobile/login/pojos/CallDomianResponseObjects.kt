package com.aeromexico.aeropuertos.paperlessmobile.login.pojos

import com.google.gson.annotations.SerializedName

data class Dominios (

    @SerializedName("idDominio") val idDominio : Int,
    @SerializedName("Dominio") val dominio : String,
    @SerializedName("Descripcion") val descripcion : String,
    @SerializedName("Activo") val activo : Boolean
)


data class DominiosResponse (

    @SerializedName("Status") val status : String,
    @SerializedName("Code") val code : Int,
    @SerializedName("ResponseCode") val responseCode : String,
    @SerializedName("Message") val message : String,
    @SerializedName("Error") val error : Boolean,
    @SerializedName("ErrorMessage") val errorMessage : String,
    @SerializedName("Result") val result : DominiosResult
)

data class DominiosResult (
    @SerializedName("Dominios") val dominios : List<Dominios>
)