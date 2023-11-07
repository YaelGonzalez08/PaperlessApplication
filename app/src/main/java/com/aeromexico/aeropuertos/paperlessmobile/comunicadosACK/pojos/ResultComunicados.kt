package com.aeromexico.aeropuertos.paperlessmobile.comunicadosACK.pojos

import com.google.gson.annotations.SerializedName

data class ResultComunicados(

    @SerializedName("Comunicados") val comunicados : ArrayList<Comunicados>?
)
