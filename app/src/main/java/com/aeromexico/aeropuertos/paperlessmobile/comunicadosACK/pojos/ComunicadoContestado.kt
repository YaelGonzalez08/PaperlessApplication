package com.aeromexico.aeropuertos.paperlessmobile.comunicadosACK.pojos

import com.google.gson.annotations.SerializedName

data class ComunicadoContestado(
    @SerializedName("idComunicadoFinalizado") val idComunicadoFinalizado: Int
)
