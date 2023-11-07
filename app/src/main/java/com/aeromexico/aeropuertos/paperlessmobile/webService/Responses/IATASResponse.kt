package com.aeromexico.aeropuertos.paperlessmobile.webService.Responses


import com.google.gson.annotations.SerializedName

data class IATASResponse(
    @SerializedName("Code")
    val code: Int,
    @SerializedName("Error")
    val error: Boolean,
    @SerializedName("ErrorMessage")
    val errorMessage: Any,
    @SerializedName("Message")
    val message: String,
    @SerializedName("ResponseCode")
    val responseCode: String,
    @SerializedName("Result")
    val result: ResultPrimerVuelo,
    @SerializedName("Status")
    val status: String
)

data class ResultPrimerVuelo(
    @SerializedName("Carriers")
    val carriers: List<String>,
    @SerializedName("IATAs")
    val iATAs: List<IATA>,
    @SerializedName("Matriculas")
    val matriculas: List<Matricula>
)

data class IATA(
    @SerializedName("IATA")
    val iATA: String,
    @SerializedName("IdIATA")
    val idIATA: Int,
    @SerializedName("internacional")
    val internacional: Boolean,
    @SerializedName("nombre")
    val nombre: String
){
    override fun toString(): String {
        return "$iATA"
    }
}

data class Matricula(
    @SerializedName("Equipo")
    val equipo: String,
    @SerializedName("Matricula")
    val matricula: String,
    @SerializedName("TipoCabina")
    val tipoCabina: String,
    @SerializedName("CodigoODS")
    val codigoODS: String
) {
    /*override fun toString(): String {
        return "$matricula"
    }*/
}

