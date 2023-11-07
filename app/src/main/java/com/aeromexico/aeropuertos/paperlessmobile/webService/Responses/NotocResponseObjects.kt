package com.aeromexico.aeropuertos.paperlessmobile.webService.Responses

import com.aeromexico.aeropuertos.paperlessmobile.notoc.entities.RequestNotoc
import com.google.gson.annotations.SerializedName

data class ResponseNotocBase (

    @SerializedName("Status") val status : String,
    @SerializedName("Code") val code : Int,
    @SerializedName("ResponseCode") val responseCode : String,
    @SerializedName("Message") val message : String,
    @SerializedName("Error") val error : Boolean,
    @SerializedName("ErrorMessage") val errorMessage : String,
    @SerializedName("Result") val result : ResultNotoc
)
data class ResultNotoc (

    @SerializedName("Notoc") val notoc : RequestNotoc
)
/*
data class Notoc (

    @SerializedName("idNOTOC") val idNOTOC : Int,
    @SerializedName("FlightReferenceNumber") val flightReferenceNumber : Long,
    @SerializedName("InformacionMercancia") val informacionMercancia : List<InformacionMercancia>,
    @SerializedName("OtraCarga") val otraCarga : List<OtraCarga>,
    @SerializedName("Consentimiento") val consentimiento : Consentimiento,
    @SerializedName("CreadoPor") val creadoPor : String,
    @SerializedName("FechaCreacion") val fechaCreacion : String
)
data class InformacionMercancia (

    @SerializedName("NumeroGuiaAerea") val numeroGuiaAerea : String,
    @SerializedName("NombreExpedicion") val nombreExpedicion : String,
    @SerializedName("Clave") val clave : String,
    @SerializedName("ONUID") val oNUID : Int,
    @SerializedName("PeligroSecundario") val peligroSecundario : String,
    @SerializedName("CantidadBultos") val cantidadBultos : Int,
    @SerializedName("GrupoEmbalaje") val grupoEmbalaje : String,
    @SerializedName("Codigo") val codigo : String,
    @SerializedName("CRE") val cRE : Int,
    @SerializedName("CantidadNeta") val cantidadNeta : Int,
    @SerializedName("Categoria") val categoria : String,
    @SerializedName("IDULD") val iDULD : String,
    @SerializedName("Posicion") val posicion : String
)
data class OtraCarga (

    @SerializedName("idCargaEspecial") val idCargaEspecial : Int,
    @SerializedName("GuiaAerea") val guiaAerea : String,
    @SerializedName("Descripcion") val descripcion : String,
    @SerializedName("Bultos") val bultos : List<Bultos>,
    @SerializedName("InformacionComplementaria") val informacionComplementaria : String,
    @SerializedName("Codigo") val codigo : String,
    @SerializedName("IDULD") val iDULD : String,
    @SerializedName("Posicion") val posicion : String
)
data class Consentimiento (

    @SerializedName("Capitan") val capitan : Capitan,
    @SerializedName("Oficial") val oficial : Oficial
)
data class Bultos (

    @SerializedName("IdCargaEspecial") val idCargaEspecial : Int,
    @SerializedName("NumeroDeBultos") val numeroDeBultos : Int,
    @SerializedName("Cantidad") val cantidad : Int
)
data class Capitan (

    @SerializedName("FirmaB64") val firmaB64 : String,
    @SerializedName("Nombre") val nombre : String,
    @SerializedName("NumEmpleado") val numEmpleado : String
)
data class Oficial (

    @SerializedName("FirmaB64") val firmaB64 : String,
    @SerializedName("Nombre") val nombre : String,
    @SerializedName("NumEmpleado") val numEmpleado : String
)*/
