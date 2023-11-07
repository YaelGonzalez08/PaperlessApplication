package com.aeromexico.aeropuertos.paperlessmobile.comunicadosACK.pojos

import com.google.gson.annotations.SerializedName

data class RequestEnvioCuesitonario(

    @SerializedName("idComunicado") var idComunicado: Int? = null,
    @SerializedName("Puesto") var puesto: String? = "",
    @SerializedName("Correo") var correo: String? = "",
    @SerializedName("NoEmpleado") var noEmpleado: String? = "",
    @SerializedName("Nombre") var nombre: String? = "",
    @SerializedName("CreadoPor") var creadoPor: String? = "",
    @SerializedName("Respuestas") var respuestas: ArrayList<Respuestas>? = null,
    @SerializedName("origen") var origen: String = "android"
)