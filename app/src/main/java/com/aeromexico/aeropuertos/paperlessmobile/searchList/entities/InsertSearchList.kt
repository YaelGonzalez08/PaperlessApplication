package com.aeromexico.aeropuertos.paperlessmobile.searchList.entities


import com.google.gson.annotations.SerializedName

data class InsertSearchList(
    @SerializedName("GuidVuelo")
    var guidVuelo: String? = "",
    @SerializedName("InspeccionCap")
    var inspeccionCap: InspeccionCap = InspeccionCap(),
    @SerializedName("InspeccionPab")
    var inspeccionPab: InspeccionPab = InspeccionPab(),
    @SerializedName("InspeccionSeg")
    var inspeccionSeg: InspeccionSeg = InspeccionSeg(),
    @SerializedName("InspeccionSob")
    var inspeccionSob: InspeccionSob = InspeccionSob(),
    @SerializedName("LPD")
    var lPD: Boolean = false,
    @SerializedName("Oficial")
    var oficial: InspeccionOficial = InspeccionOficial(),
    @SerializedName("Pernocta")
    var pernocta: Boolean = false
)

data class InspeccionOficial(
    @SerializedName("CreadoPor")
    var creadoPor: String = "",
    @SerializedName("FechaCreacion")
    var fechaCreacion: String = "",
    @SerializedName("FirmaB64")
    var firmaB64: String = "",
    @SerializedName("Informacionrelativa")
    var informacionrelativa: String = "",
    @SerializedName("Nombre")
    var nombre: String = "",
    @SerializedName("NumEmpleado")
    var numEmpleado: String = "",
    @SerializedName("Preguntas")
    var preguntas: MutableList<PreguntaCheckList> = mutableListOf()
)

data class InspeccionCap(
    @SerializedName("CreadoPor")
    var creadoPor: String = "",
    @SerializedName("FechaCreacion")
    var fechaCreacion: String = "",
    @SerializedName("FirmaB64")
    var firmaB64: String = "",
    @SerializedName("Informacionrelativa")
    var informacionrelativa: String = "",
    @SerializedName("Nombre")
    var nombre: String = "",
    @SerializedName("NumEmpleado")
    var numEmpleado: String = "",
    @SerializedName("Preguntas")
    var preguntas: List<PreguntaCheckList> = listOf()
)

data class InspeccionPab(
    @SerializedName("CreadoPor")
    var creadoPor: String = "",
    @SerializedName("FechaCreacion")
    var fechaCreacion: String = "",
    @SerializedName("FirmaB64")
    var firmaB64: String = "",
    @SerializedName("Nombre")
    var nombre: String = "",
    @SerializedName("NumEmpleado")
    var numEmpleado: String = "",
    @SerializedName("Preguntas")
    var preguntas: List<PreguntaCheckList> = listOf()
)

data class InspeccionSeg(
    @SerializedName("CreadoPor")
    var creadoPor: String = "",
    @SerializedName("FechaCreacion")
    var fechaCreacion: String = "",
    @SerializedName("FirmaB64")
    var firmaB64: String = "",
    @SerializedName("Nombre")
    var nombre: String = "",
    @SerializedName("NumEmpleado")
    var numEmpleado: String = "",
    @SerializedName("Preguntas")
    var preguntas: List<PreguntaCheckList> = listOf()
)

data class InspeccionSob(
    @SerializedName("CreadoPor")
    var creadoPor: String = "",
    @SerializedName("FechaCreacion")
    var fechaCreacion: String = "",
    @SerializedName("FirmaB64")
    var firmaB64: String = "",
    @SerializedName("Informacionrelativa")
    var informacionrelativa: String = "",
    @SerializedName("Nombre")
    var nombre: String = "",
    @SerializedName("NumEmpleado")
    var numEmpleado: String = "",
    @SerializedName("Preguntas")
    var preguntas: List<PreguntaCheckList> = listOf()
)

data class PreguntaCheckList(
    @SerializedName("Condicion")
    var condicion: Int = 0,
    @SerializedName("Idpregunta")
    var idpregunta: Int = 0
)