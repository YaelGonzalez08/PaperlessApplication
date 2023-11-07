package com.aeromexico.aeropuertos.paperlessmobile.optBdm.entities

import com.google.gson.annotations.SerializedName

data class Piloto(
    @SerializedName("Remark")
    var remark: String = "",
    @SerializedName("FirmaB64")
    var firmaB64: String = "",
    @SerializedName("Nombre")
    var nombre: String = "",
    @SerializedName("NumEmpleado")
    var numEmpleado: String = ""
)
data class Planificador(
    @SerializedName("Remark")
    var remark: String = "",
    @SerializedName("FirmaB64")
    var firmaB64: String = "",
    @SerializedName("Nombre")
    var nombre: String = "",
    @SerializedName("NumEmpleado")
    var numEmpleado: String = ""
)

data class RequestOptBdmForm(
    @SerializedName("FlightReferenceNumber")
    var flightReferenceNumber: Long,
    @SerializedName("Planificador")
    var planificador: Planificador = Planificador(),
    @SerializedName("Piloto")
    var piloto: Piloto = Piloto(),
    @SerializedName("FechaCreacion")
    var fechaCreacion: String = "",
    @SerializedName("CreadoPor")
    var creadoPor: String = ""
)