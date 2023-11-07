package com.aeromexico.aeropuertos.paperlessmobile.webService.Responses

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
@Parcelize
data class ResultEquipo(

    @SerializedName("Equipo") var equipo: Equipo?,
    @SerializedName("Preguntas") var preguntas: List<Preguntas>?,
): Parcelable

data class EquipoResponseObjects(

    @SerializedName("Status") val status: String,
    @SerializedName("Code") val code: Int,
    @SerializedName("ResponseCode") val responseCode: String,
    @SerializedName("Message") val message: String,
    @SerializedName("Error") val error: Boolean,
    @SerializedName("ErrorMessage") val errorMessage: String,
    @SerializedName("Result") val result: ResultEquipo
)

@Parcelize
data class Equipo(

    @SerializedName("idEquipo") val idEquipo: Int,
    @SerializedName("idCompania") val idCompania: String,
    @SerializedName("numeroActivo") val numeroActivo: String,
    @SerializedName("descripcion") val descripcion: String,
    @SerializedName("idEstacion") val idEstacion: String,
    @SerializedName("estado") val estado: String,
    @SerializedName("familia") val familia: String,
    @SerializedName("idEstatus") val idEstatus: String,
    @SerializedName("idCombustible") var idCombustible: String?=""
) : Parcelable

@Parcelize
data class Preguntas(
    @SerializedName("idPregunta") var idPregunta: Int,
    @SerializedName("titulo") var titulo: String,
    @SerializedName("descripcion") var descripcion: String,
    @SerializedName("estate") var estate: Int?,
    @SerializedName("observation") var observation: String?,
    @SerializedName("photo") var photo: String?
) : Parcelable {
    constructor(idPregunta:Int,titulo: String, descripcion: String) : this(idPregunta, titulo, descripcion, 0, null,null) {
       this.idPregunta = idPregunta
        this.titulo = titulo
        this.descripcion = descripcion
        this.estate = 0
    }
}
