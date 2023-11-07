package com.aeromexico.aeropuertos.paperlessmobile.comunicadosACK.pojos

import com.google.gson.annotations.SerializedName

data class Comunicados(
    @SerializedName("idComunicado") val idComunicado: Int,
    @SerializedName("numero") val numero: Int,
    @SerializedName("titulo") val titulo: String,
    @SerializedName("vigencia") val vigencia: String,
    @SerializedName("fechaCreacion") val fechaCreacion: String,
    @SerializedName("idEstatus") val idEstatus: Int,
    @SerializedName("nombreEstatus") val nombreEstatus: String,
    @SerializedName("idUnidadNegocio") val idUnidadNegocio: Int,
    @SerializedName("nombreUnidadNegocio") val nombreUnidadNegocio: String,
    @SerializedName("cuerpoMensaje") val cuerpoMensaje: String,
    @SerializedName("version") val version: Int,
    @SerializedName("link") val link: String,
    @SerializedName("prioritario") val prioritario: Boolean = false
)