package com.aeromexico.aeropuertos.paperlessmobile.webService.Responses

import com.google.gson.annotations.SerializedName

data class CreateInspeccionResponsaBase (
    @SerializedName("Status") val status : String,
    @SerializedName("Code") val code : Int,
    @SerializedName("ResponseCode") val responseCode : String,
    @SerializedName("Message") val message : String,
    @SerializedName("Error") val error : Boolean,
    @SerializedName("ErrorMessage") val errorMessage : String,
    @SerializedName("Result") val result : CreateInspeccionesult
)

data class CreateInspeccionesult (
    @SerializedName("InspeccionAeronave") val InspeccionAeronave : Long,
)

data class InspeccionAeronave(
    @SerializedName("idInspeccionAeronave") val idInspeccionAeronave: Long,
    @SerializedName("GuidInspeccionAeronave") val GuidInspeccionAeronave: String
)



data class GetTiposAveriaResponsaBase (
    @SerializedName("Status") val status : String,
    @SerializedName("Code") val code : Int,
    @SerializedName("ResponseCode") val responseCode : String,
    @SerializedName("Message") val message : String,
    @SerializedName("Error") val error : Boolean,
    @SerializedName("ErrorMessage") val errorMessage : String,
    @SerializedName("Result") val result : TiposAveriaResult
)

data class TiposAveriaResult (
    @SerializedName("TiposAveria") val TiposAveria : ArrayList<Averia>
)

data class Averia (
    @SerializedName("idTipoAveria") val idTipoAveria : Int?=0,
    @SerializedName("TipoAveria") val TipoAveria : String?="",
    @SerializedName("Codigo") val Codigo : String?="",
    @SerializedName("Activo") val Activo : Boolean?=false,
    @SerializedName("EsObligatorio") val EsObligatorio : Boolean?=false,
    @SerializedName("RespuestaAux") var respuestaAux :  String?="",
    @SerializedName("RespNumerica") val respNumerica : Boolean?=false,


    //lo que se recibe
    @SerializedName("idAveria") var idAveria : Int?=0,
    @SerializedName("CodigoAveria") var codigoAveria : String?="",
    @SerializedName("TieneAveria") var tieneAveria : Boolean?=null,
    @SerializedName("DescripcionAveria") var descripcionAveria : String?="",
    @SerializedName("Imagenes") var imagenes : ArrayList<Imagenes>?= arrayListOf()

    ) {
    var naSelected: Boolean = false
}

data class Imagenes (
    @SerializedName("ImgB64") val imgB64 : String?=null
)