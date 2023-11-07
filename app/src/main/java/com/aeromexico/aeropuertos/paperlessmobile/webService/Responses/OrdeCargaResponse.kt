package com.aeromexico.aeropuertos.paperlessmobile.webService.Responses


import com.google.gson.annotations.SerializedName

data class OrdeCargaResponse(
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
    var result: ResultOrdenCarga = ResultOrdenCarga(),
    @SerializedName("Status")
    var status: String = ""
)

data class OrdenCarga(
    @SerializedName("idOrdenCarga")
    var idOrdenCarga: Int = 0,
    @SerializedName("cargasExcedidas")
    var cargasExcedidas: Boolean = false
)

data class ResultOrdenCarga(
    @SerializedName("OrdenCarga")
    var ordenCarga: OrdenCarga = OrdenCarga()
)