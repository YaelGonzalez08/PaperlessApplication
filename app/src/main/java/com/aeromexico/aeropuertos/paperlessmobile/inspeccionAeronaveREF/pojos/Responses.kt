package com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronaveREF.pojos

import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.Averia
import com.google.gson.annotations.SerializedName

data class RequestExisteInspeccion (

    @SerializedName("GuidVuelo") val guidVuelo : String,
    @SerializedName("EsLlegada") val esLlegada : Boolean
)
data class ResponseExisteInspeccion (

    @SerializedName("Status") val status : String,
    @SerializedName("Code") val code : Int,
    @SerializedName("ResponseCode") val responseCode : String,
    @SerializedName("Message") val message : String,
    @SerializedName("Error") val error : Boolean,
    @SerializedName("ErrorMessage") val errorMessage : String,
    @SerializedName("Result") val result : ResultInspeccion?=null
)
data class ResultInspeccion (

    @SerializedName("InspeccionAeronave") val inspeccionAeronave : InspeccionAeronave
)
data class InspeccionAeronave (

    @SerializedName("idInspeccionAeronave") val idInspeccionAeronave : Int?=0,
    @SerializedName("FechaCreacion") val fechaCreacion : String?="",
    @SerializedName("CreadoPor") val creadoPor : String?="",
    @SerializedName("FechaModificacion") var fechaModificacion :String?="",
    @SerializedName("ModificadoPor") var modificadoPor : String?="",
    @SerializedName("EnviadoPor") var enviadoPor : String?="",
    @SerializedName("GuidInspeccion") val guid : String?="",
    @SerializedName("GuidVuelo") var guidVuelo : String?="",
    @SerializedName("idVuelo") val idVuelo : Int?=0,
    @SerializedName("FirmaB64OficialOperaciones") var firmaB64OficialOperaciones : String?=null,
    @SerializedName(value ="NoEmpleadoOficialOperaciones", alternate = [ "NumEmpleado_OficialOperaciones"]) var numEmpleado_OficialOperaciones : String?=null,
    @SerializedName(value = "NombreOficialOperaciones", alternate = ["Nombre_OficialOperaciones"]) var nombre_OficialOperaciones : String?=null,

    @SerializedName( value = "NoEmpleadoTecnicoMantenimiento", alternate = ["NumEmpleado_TecnicoMantenimiento"]) var numEmpleado_TecnicoMantenimiento : String?="",
    @SerializedName(value = "NombreTecnicoMantenimiento", alternate = ["Nombre_TecnicoMantenimiento"]) var nombre_TecnicoMantenimiento : String?="",

    @SerializedName("FirmaB64TecnicoMantenimiento") var firmaB64TecnicoMantenimiento : String?=null,

    @SerializedName("ResponsablesEstiba") var responsablesEstiba : ArrayList<ResponsablesEstiba>?= null,
    @SerializedName("Completado") var Completado : Boolean?=false,
    @SerializedName("Pernocta") var pernocta : Boolean?=null,
    @SerializedName("EsLlegada") val esLlegada : Boolean?=null,
    @SerializedName("Version") val version : Int?=0,
    @SerializedName("Averias") var averias : ArrayList<Averia>?= arrayListOf()

)

data class ResponsablesEstiba (

    @SerializedName("NumeroEmpleado") val noEmpleado : String,
    @SerializedName("Nombre") val nombre : String
){
    override fun toString(): String {
        return "$noEmpleado | $nombre"
    }
}

