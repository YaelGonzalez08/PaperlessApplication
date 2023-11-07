package com.aeromexico.aeropuertos.paperlessmobile.controlAbordaje.pojos

import com.google.gson.annotations.SerializedName

data class PasajerosResult (

    @SerializedName("Pax") val pax : ArrayList<Pax>,
    @SerializedName("ClaseJ") val claseJ : Int,
    @SerializedName("ClaseY") val claseY : Int
)