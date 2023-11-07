package com.aeromexico.aeropuertos.paperlessmobile.comunicadosACK.pojos

import com.google.gson.annotations.SerializedName

data class ComunicadosResponse(

    @SerializedName("Status") val status : String,
    @SerializedName("Code") val code : Int,
    @SerializedName("ResponseCode") val responseCode : String,
    @SerializedName("Message") val message : String,
    @SerializedName("Error") val error : Boolean,
    @SerializedName("ErrorMessage") val errorMessage : String,
    @SerializedName("Result") val result : ResultComunicados?
)