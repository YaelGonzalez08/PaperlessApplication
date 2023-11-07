package com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronavePrimerVuelo.entities


import com.google.gson.annotations.SerializedName

data class RequestFirstFlightForm(
    @SerializedName("FlightReferenceNumber")
    var flightReferenceNumber: Long,
    @SerializedName("Oficial")
    var oficial: Oficial = Oficial(),
    @SerializedName("Piloto")
    var piloto: Piloto = Piloto(),
    @SerializedName("Pernocta")
    var pernocta: Boolean = false,
    @SerializedName("Preguntas")
    var preguntas: MutableList<Pregunta> = mutableListOf(),
    @SerializedName("Sobrecargo")
    var sobrecargo: Sobrecargo = Sobrecargo(),
    @SerializedName("Cocinas")
    var cocinas: Cocinas = Cocinas()
)

data class Oficial(
    @SerializedName("CreadoPor")
    var creadoPor: String = "",
    @SerializedName("Discrepancia")
    var discrepancia: String = "",
    @SerializedName("FechaCreacion")
    var fechaCreacion: String = "",
    @SerializedName("FirmaB64")
    var firmaB64: String = "",
    @SerializedName("Nombre")
    var nombre: String = "",
    @SerializedName("NumEmpleado")
    var numEmpleado: String = ""
)

data class Piloto(
    @SerializedName("CreadoPor")
    var creadoPor: String = "",
    @SerializedName("Discrepancia")
    var discrepancia: String = "",
    @SerializedName("FechaCreacion")
    var fechaCreacion: String = "",
    @SerializedName("FirmaB64")
    var firmaB64: String = "",
    @SerializedName("Nombre")
    var nombre: String = "",
    @SerializedName("NumEmpleado")
    var numEmpleado: String = ""
)

data class Pregunta(
    @SerializedName("Condicion")
    var condicion: Int,
    @SerializedName("Idpregunta")
    var idpregunta: Int
)

data class Sobrecargo(
    @SerializedName("CreadoPor")
    var creadoPor: String = "",
    @SerializedName("Discrepancia")
    var discrepancia: String = "",
    @SerializedName("FechaCreacion")
    var fechaCreacion: String = "",
    @SerializedName("FirmaB64")
    var firmaB64: String = "",
    @SerializedName("Nombre")
    var nombre: String = "",
    @SerializedName("NumEmpleado")
    var numEmpleado: String = ""
)
data class Cocinas(
    @SerializedName("CreadoPor")
    var creadoPor: String = "",
    @SerializedName("Discrepancia")
    var discrepancia: String = "",
    @SerializedName("FechaCreacion")
    var fechaCreacion: String = "",
    @SerializedName("FirmaB64")
    var firmaB64: String = "",
    @SerializedName("Nombre")
    var nombre: String = "",
    @SerializedName("NumEmpleado")
    var numEmpleado: String = ""
)