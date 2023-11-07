package com.aeromexico.aeropuertos.paperlessmobile.webService.Responses

import com.google.gson.annotations.SerializedName

data class ResponseManifiestoCargaBase (

    @SerializedName("Status") val status : String,
    @SerializedName("Code") val code : Int,
    @SerializedName("ResponseCode") val responseCode : String,
    @SerializedName("Message") val message : String,
    @SerializedName("Error") val error : Boolean,
    @SerializedName("ErrorMessage") val errorMessage : String,
    @SerializedName("Result") val result : ResultMC
)

data class ResultMC (

    @SerializedName("pdfBase64") val pdfBase64 : String,
    @SerializedName("nombreArchivo") val nombreArchivo : String
)