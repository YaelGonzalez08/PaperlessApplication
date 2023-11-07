package com.aeromexico.aeropuertos.paperlessmobile.comunicadosACK.pojos;

import com.google.gson.annotations.SerializedName;

data class ResultEnvioCuestionario(

    @SerializedName("ComunicadoContestado") val comunicadoContestado: ComunicadoContestado
)
