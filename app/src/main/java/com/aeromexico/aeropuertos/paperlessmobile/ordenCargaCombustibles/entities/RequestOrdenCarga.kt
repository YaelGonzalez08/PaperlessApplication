package com.aeromexico.aeropuertos.paperlessmobile.ordenCargaCombustibles.entities


import com.google.gson.annotations.SerializedName

data class RequestOrdenCarga(
    @SerializedName("TipoCarga")
    var tipoCarga: String = "",
    @SerializedName("UnidadDensidad")
    var unidadDensidad: String = "",
    @SerializedName("Combustible_antes_carga")
    var combustibleAntesCarga: Double = 0.0,
    @SerializedName("CreadoPor")
    var creadoPor: String = "",
    @SerializedName("Densidad")
    var densidad: Double = 0.0,
    @SerializedName("Distribucion_combustible")
    var distribucionCombustible: String = "",
    @SerializedName("Extra_Fuel")
    var extraFuel: String = "",
    @SerializedName("FechaCreacion")
    var fechaCreacion: String = "",
    @SerializedName("FirmaB64Mecanico")
    var firmaB64Mecanico: String = "",
    @SerializedName("FirmaB64Operaciones")
    var firmaB64Operaciones: String = "",
    @SerializedName("Hora_Registro")
    var horaRegistro: String = "",
    @SerializedName("GuidVuelo")
    var guidVuelo: String = "",
    @SerializedName("NoEmpleadoMecanico")
    var noEmpleadoMecanico: String = "",
    @SerializedName("NoEmpleadoOficialOperaciones")
    var noEmpleadoOficialOperaciones: String = "",
    @SerializedName("NombreMecanico")
    var nombreMecanico: String = "",
    @SerializedName("NombreOficialOperaciones")
    var nombreOficialOperaciones: String = "",
    @SerializedName("ObservacionesOficial")
    var observacionesOficial: String = "",
    @SerializedName("ObservacionesMecanico")
    var observacionesMecanico: String = "",
    @SerializedName("Percnota")
    var percnota: Boolean = false,
    @SerializedName("Tanque_Central")
    var tanqueCentral: TanqueCentral = TanqueCentral(),
    @SerializedName("Tanque_Derecho")
    var tanqueDerecho: TanqueDerecho = TanqueDerecho(),
    @SerializedName("Tanque_Izquierdo")
    var tanqueIzquierdo: TanqueIzquierdo = TanqueIzquierdo(),
    @SerializedName("Temperatura")
    var temperatura: String = "",
    @SerializedName("Volumen_cargado_dispensador")
    var volumenCargadoDispensador: String = "",
    @SerializedName("HoraLLegadaMecanico")
    var horaLlegadaMecanico: String = "",
    @SerializedName("Veces") val veces : Int=0,

    var isPendingToSend: Boolean = false, // si esta pendiente de enviar se pone en true, al igual que si viene de servicios
    var isForService: Boolean = false // si el RQ ya viene de servicios se pone true
)

data class TanqueCentral(
    @SerializedName("Indicado")
    var indicado: Double = 0.0,
    @SerializedName("Reglas")
    var reglas: Double = 0.0,
    @SerializedName("Requerido")
    var requerido: Double = 0.0
)
data class GuidFormCheck(
    @SerializedName("GuidVuelo")
    var guidVuelo: String = ""
)

data class TanqueDerecho(
    @SerializedName("Indicado")
    var indicado: Double = 0.0,
    @SerializedName("Reglas")
    var reglas: Double = 0.0,
    @SerializedName("Requerido")
    var requerido: Double = 0.0
)

data class TanqueIzquierdo(
    @SerializedName("Indicado")
    var indicado: Double = 0.0,
    @SerializedName("Reglas")
    var reglas: Double = 0.0,
    @SerializedName("Requerido")
    var requerido: Double = 0.0
)

data class ResultOrdenCarga (

    @SerializedName("OrdenCarga") val ordenCarga : RequestOrdenCarga
)

data class ResponseCheckOrdenCarga (

    @SerializedName("Status") val status : String,
    @SerializedName("Code") val code : Int,
    @SerializedName("ResponseCode") val responseCode : String,
    @SerializedName("Message") val message : String,
    @SerializedName("Error") val error : Boolean,
    @SerializedName("ErrorMessage") val errorMessage : String,
    @SerializedName("Result") val result : ResultOrdenCarga
)