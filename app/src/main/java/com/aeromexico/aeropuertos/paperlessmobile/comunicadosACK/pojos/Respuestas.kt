package com.aeromexico.aeropuertos.paperlessmobile.comunicadosACK.pojos

import com.google.gson.annotations.SerializedName

data class Respuestas(

    @SerializedName("idCuestionario") val idCuestionario: Int,
    @SerializedName("idRespuesta") val idRespuesta: Int,
    @SerializedName("respuesta") var respuesta: String?="",
    @SerializedName("esCorrecta") var esCorrecta: Boolean
)
