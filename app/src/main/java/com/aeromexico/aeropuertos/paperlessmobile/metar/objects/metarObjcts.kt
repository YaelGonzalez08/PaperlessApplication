package com.aeromexico.aeropuertos.paperlessmobile.metar.objects

import com.google.gson.annotations.SerializedName

data class MetarResponse (

    @SerializedName("Status") val status : String,
    @SerializedName("Code") val code : Int,
    @SerializedName("ResponseCode") val responseCode : String,
    @SerializedName("Message") val message : String,
    @SerializedName("Error") val error : Boolean,
    @SerializedName("ErrorMessage") val errorMessage : String,
    @SerializedName("Result") val result : MetarResult?
)

data class Metar (

    @SerializedName("idMetar") val idMetar : Int,
    @SerializedName("FechaCreacion") val fechaCreacion : String,
    @SerializedName("Contenido") val contenido : String,
    @SerializedName("Fuente") val fuente : String
)
data class MetarResult (

    @SerializedName("Metar") val metar : List<Metar>?
)