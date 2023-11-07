package com.aeromexico.aeropuertos.paperlessmobile.webService.Responses

import com.google.gson.annotations.SerializedName

data class ResponseOptBdmBase (

    @SerializedName("Status") val status : String,
    @SerializedName("Code") val code : Int,
    @SerializedName("ResponseCode") val responseCode : String,
    @SerializedName("Message") val message : String,
    @SerializedName("Error") val error : Boolean,
    @SerializedName("ErrorMessage") val errorMessage : String,
    @SerializedName("Result") val result : ResultOpt
)
data class ResultOpt (

    @SerializedName("Creado") val creado : Boolean,
    @SerializedName("Firmado") val firmado : Boolean,
    @SerializedName("FlightReferenceNumber") val flightReferenceNumber : Long,
    @SerializedName("Compania") val compania : String,
    @SerializedName("NumVuelo") val numVuelo : Int,
    @SerializedName("FechaVueloLocal") val fechaVueloLocal : String,
    @SerializedName("Origen") val origen : String,
    @SerializedName("Destino") val destino : String,
    @SerializedName("CodigoEquipo") val codigoEquipo : String,
    @SerializedName("Matricula") val matricula : String,
    @SerializedName("Tipo") val tipo : String,
    @SerializedName("idOPT_EDM") val idOPT_EDM : Int,
    @SerializedName("ZFW") val zFW : Double,
    @SerializedName("CG") val cG : Double,
    @SerializedName("ObservCaptain") val observCaptain : String,
    @SerializedName("ObservPlaner") val observPlaner : String,
    @SerializedName("ImgB64") val imgB64 : String
)

data class SendResponse_Base (

    @SerializedName("Status") val status : String,
    @SerializedName("Code") val code : Int,
    @SerializedName("ResponseCode") val responseCode : String,
    @SerializedName("Message") val message : String,
    @SerializedName("Error") val error : Boolean,
    @SerializedName("ErrorMessage") val errorMessage : String,
    @SerializedName("Result") val result : ResultBase
)
data class ResultBase (

    @SerializedName("Success") val success : Boolean
)