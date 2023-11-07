package com.aeromexico.aeropuertos.paperlessmobile.controlAbordaje.pojos

import com.google.gson.annotations.SerializedName


data class ResultGetEmployeeInfo (
    @SerializedName("EmpleadoInfo") val empleadoInfo : EmpleadoInfo?
)


data class EmpleadoInfo (

    @SerializedName("idControlAbordaje") val idControlAbordaje : Int?,
    @SerializedName("idVuelo") val idVuelo : Int?,
    @SerializedName("flightReferenceNumber") val flightReferenceNumber : String?,
    @SerializedName("CoordinacionAbordajeCapitan") val coordinacionAbordajeCapitan : String?,
    @SerializedName("Capitan") val capitan : String?,
    @SerializedName("NumeroCapitan") val numeroCapitan : String?,
    @SerializedName("InicioAbordaje") val inicioAbordaje : String?,
    @SerializedName("HoraCierre") val horaCierre : String?,
    @SerializedName("ASC") val aSC : String?,
    @SerializedName("NumeroASC") val numeroASC : String?,
    @SerializedName("ObservacionesASC") val observacionesASC : String?,
    @SerializedName("Tecnica") val tecnica : String?,
    @SerializedName("Servicio") val servicio : String?,
    @SerializedName("SolicitudProcedimientoSeguridad") val solicitudProcedimientoSeguridad : String?,
    @SerializedName("EntregaEquipajeLL") val entregaEquipajeLL : String?,
    @SerializedName("RecepcionEquipajeLL") val recepcionEquipajeLL : String?,
    @SerializedName("LL") val lL : String?,
    @SerializedName("NumeroLL") val numeroLL : String?,
    @SerializedName("Oficial") val oficial : String?,
    @SerializedName("NumeroOficial") val numeroOficial : String?,
    @SerializedName("ObservacionesOficial") val observacionesOficial : String?
)