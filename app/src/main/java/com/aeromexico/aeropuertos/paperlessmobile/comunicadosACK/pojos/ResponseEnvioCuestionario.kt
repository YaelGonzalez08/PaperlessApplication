package com.aeromexico.aeropuertos.paperlessmobile.comunicadosACK.pojos

import com.google.gson.annotations.SerializedName

data class ResponseEnvioCuestionario(
    @SerializedName("Status") val status: String?,
    @SerializedName("Code") val code: Int?,
    @SerializedName("ResponseCode") val responseCode: String?,
    @SerializedName("Message") var message: String?,
    @SerializedName("Error") val error: Boolean?,
    @SerializedName("ErrorMessage") val errorMessage: String?,
    @SerializedName("Result") val result: ResultEnvioCuestionario?
)