package com.aeromexico.aeropuertos.paperlessmobile.controlAbordaje.pojos

import com.google.gson.annotations.SerializedName

data class PasajerosQuitarCheckInRequest(
    @SerializedName("pax") val pax: Pax,
    @SerializedName("flightReferenceNumber") val flightReferenceNumber: String

)