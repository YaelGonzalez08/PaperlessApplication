package com.aeromexico.aeropuertos.paperlessmobile.webService.Responses

import com.google.gson.annotations.SerializedName

data class VueloManualResponse (
    @SerializedName("Code")
    val code: Int,
    @SerializedName("Error")
    val error: Boolean,
    @SerializedName("ErrorMessage")
    val errorMessage: Any,
    @SerializedName("Message")
    val message: String,
    @SerializedName("ResponseCode")
    val responseCode: String,
    @SerializedName("Result")
    val result: ResultvueloManual,
    @SerializedName("Status")
    val status: String
)

data class ResultvueloManual(
    @SerializedName("Vuelos")
    val vuelos: List<Vuelos>
)