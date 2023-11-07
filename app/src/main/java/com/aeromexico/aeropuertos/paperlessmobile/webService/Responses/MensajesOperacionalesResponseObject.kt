package com.aeromexico.aeropuertos.paperlessmobile.webService.Responses

import com.google.gson.annotations.SerializedName

data class ResponseMOBase (

        @SerializedName("Status") val status : String,
        @SerializedName("Code") val code : Int,
        @SerializedName("ResponseCode") val responseCode : String,
        @SerializedName("Message") val message : String,
        @SerializedName("Error") val error : Boolean,
        @SerializedName("ErrorMessage") val errorMessage : String,
        @SerializedName("Result") val result : Results
)
data class Msjs (

        @SerializedName("idMensajeOperacional") val idMensajeOperacional : Long,
        @SerializedName("cuerpoMensaje") val cuerpoMensaje : String,
        @SerializedName("imgb64") val imgb64 : String?,
        @SerializedName("codigo") val codigo : String,
        @SerializedName("isLir") val isLir : Boolean,
        @SerializedName("firmado") val firmado : Boolean,
        @SerializedName("FehcaMsjOperacional") val fecha: String?=null
)
data class Results (

        @SerializedName("Msjs") val msjs : List<Msjs>,
        @SerializedName("TiposMsjs") val tiposMsjs : List<String>
)

data class ResponseMOBaseDetalle (

        @SerializedName("Status") val status : String,
        @SerializedName("Code") val code : Int,
        @SerializedName("ResponseCode") val responseCode : String,
        @SerializedName("Message") val message : String,
        @SerializedName("Error") val error : Boolean,
        @SerializedName("ErrorMessage") val errorMessage : String,
        @SerializedName("Result") val result : ResultDetalle
)
data class ResultDetalle (

        @SerializedName("InsertUpdate") val insertUpdate : Boolean
)