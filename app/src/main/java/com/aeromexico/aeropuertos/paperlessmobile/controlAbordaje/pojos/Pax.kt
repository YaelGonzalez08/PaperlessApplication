package com.aeromexico.aeropuertos.paperlessmobile.controlAbordaje.pojos

import com.google.gson.annotations.SerializedName

data class Pax (
    @SerializedName("passengerID") var passengerID : String,
    @SerializedName("pnr") var pnr : String,
    @SerializedName("name") var name : String,
    @SerializedName("lastName") var lastName : String,
    @SerializedName("aerolinea") var aerolinea : String,
    @SerializedName("conexion") var conexion : String?,
    @SerializedName("destino") var destino : String,
    @SerializedName("seat") var seat : String,
    @SerializedName("bagTags") var bagTags : String,
    @SerializedName("bagCount") var bagCount : String,
    @SerializedName("bagWeight") var bagWeight : String,
    @SerializedName("tieneARPL") var tieneARPL : Boolean,
    @SerializedName("tieneAVIH") var tieneAVIH : Boolean,
    @SerializedName("arpl") var arpl : String?,
    @SerializedName("avih") var avih : String?,
    @SerializedName("checkInNumber") var checkInNumber : Int,
    @SerializedName("HoraConfirmacion") var HoraConfirmacion : String?,
    @SerializedName("evidencia") var evidencia : String?
)