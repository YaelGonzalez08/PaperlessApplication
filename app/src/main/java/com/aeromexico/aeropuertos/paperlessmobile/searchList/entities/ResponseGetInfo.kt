package com.aeromexico.aeropuertos.paperlessmobile.searchList.entities


import com.google.gson.annotations.SerializedName

data class ResponseGetInfo(
    @SerializedName("Code")
    var code: Int = 0,
    @SerializedName("Error")
    var error: Boolean = false,
    @SerializedName("ErrorMessage")
    var errorMessage: Any = Any(),
    @SerializedName("Message")
    var message: String = "",
    @SerializedName("ResponseCode")
    var responseCode: String = "",
    @SerializedName("Result")
    var result: Result = Result(),
    @SerializedName("Status")
    var status: String = ""
)

data class SearchListGetInfo(
    @SerializedName("Info")
    var info: Boolean = false,
    @SerializedName("InspeccionCap")
    var inspeccionCap: InspeccionCapGetInfo = InspeccionCapGetInfo(),
    @SerializedName("InspeccionPab")
    var inspeccionPab: InspeccionPabGetInfo = InspeccionPabGetInfo(),
    @SerializedName("InspeccionSeg")
    var inspeccionSeg: InspeccionSegGetInfo = InspeccionSegGetInfo(),
    @SerializedName("InspeccionSob")
    var inspeccionSob: InspeccionSobGetInfo = InspeccionSobGetInfo(),
    @SerializedName("LPD")
    var lPD: Boolean = false,
    @SerializedName("Oficial")
    var oficial: OficialGetInfo = OficialGetInfo(),
    @SerializedName("Pernocta")
    var pernocta: Boolean = false,
    @SerializedName("Preguntas")
    var preguntas: List<PreguntaGetInfo> = listOf()
)

data class Result(
    @SerializedName("SearchList")
    var searchList: SearchListGetInfo = SearchListGetInfo()
)

data class PreguntaGetInfo(
    @SerializedName("Condicion")
    var condicion: Boolean = false,
    @SerializedName("Idpregunta")
    var idpregunta: Int = 0
)

data class OficialGetInfo(
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
    var numEmpleado: String = ""
)

data class InspeccionSobGetInfo(
    @SerializedName("CreadoPor")
    var creadoPor: String = "",
    @SerializedName("FechaCreacion")
    var fechaCreacion: Any = Any(),
    @SerializedName("FirmaB64")
    var firmaB64: String = "",
    @SerializedName("Informacionrelativa")
    var informacionrelativa: String = "",
    @SerializedName("Nombre")
    var nombre: String = "",
    @SerializedName("NumEmpleado")
    var numEmpleado: String = ""
)

data class InspeccionSegGetInfo(
    @SerializedName("CreadoPor")
    var creadoPor: String = "",
    @SerializedName("FechaCreacion")
    var fechaCreacion: Any = Any(),
    @SerializedName("FirmaB64")
    var firmaB64: String = "",
    @SerializedName("Informacionrelativa")
    var informacionrelativa: String = "",
    @SerializedName("Nombre")
    var nombre: String = "",
    @SerializedName("NumEmpleado")
    var numEmpleado: String = ""
)

data class InspeccionPabGetInfo(
    @SerializedName("CreadoPor")
    var creadoPor: String = "",
    @SerializedName("FechaCreacion")
    var fechaCreacion: Any = Any(),
    @SerializedName("FirmaB64")
    var firmaB64: String = "",
    @SerializedName("Informacionrelativa")
    var informacionrelativa: String = "",
    @SerializedName("Nombre")
    var nombre: String = "",
    @SerializedName("NumEmpleado")
    var numEmpleado: String = ""
)

data class InspeccionCapGetInfo(
    @SerializedName("CreadoPor")
    var creadoPor: String = "",
    @SerializedName("FechaCreacion")
    var fechaCreacion: Any = Any(),
    @SerializedName("FirmaB64")
    var firmaB64: String = "",
    @SerializedName("Informacionrelativa")
    var informacionrelativa: String = "",
    @SerializedName("Nombre")
    var nombre: String = "",
    @SerializedName("NumEmpleado")
    var numEmpleado: String = ""
)