package com.aeromexico.aeropuertos.paperlessmobile.comunicadosACK.pojos

import DetalleComunicado
import com.google.gson.annotations.SerializedName

data class ResultPdfComunicado (
    @SerializedName("DetalleComunicado") val detalleComunicado : DetalleComunicado
    )
