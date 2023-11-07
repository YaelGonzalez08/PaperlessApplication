package com.aeromexico.aeropuertos.paperlessmobile.controlAbordaje.pojos

import com.google.gson.annotations.SerializedName

data class InsertInfoOficial (
    @SerializedName("CreadoPor") val creadoPor : String,
    @SerializedName("FechaCreacion") val fechaCreacion : String,
    @SerializedName("flightReferenceNumber") val flightReferenceNumber : String,
    @SerializedName("Tecnica") val tecnica : String,
    @SerializedName("Servicio") val servicio : String,
    @SerializedName("SolicitudProcedimientoSeguridad") val solicitudProcedimientoSeguridad : String,
    @SerializedName("EntregaEquipajeLL") val entregaEquipajeLL : String,
    @SerializedName("RecepcionEquipajeLL") val recepcionEquipajeLL : String,
    @SerializedName("LL") val lL : String,
    @SerializedName("NumeroLL") var numeroLL : String,
    @SerializedName("Oficial") val oficial : String,
    @SerializedName("NumeroOficial") var numeroOficial : String,
    @SerializedName("ObservacionesOficial") val observacionesOficial : String,
    @SerializedName("FirmaB64LL") val firmaB64LL : String,
    @SerializedName("FirmaB64Oficial") val firmaB64Oficial : String
)