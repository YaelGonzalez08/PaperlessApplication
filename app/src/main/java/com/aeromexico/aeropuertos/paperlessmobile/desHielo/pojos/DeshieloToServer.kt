package com.aeromexico.aeropuertos.paperlessmobile.desHielo.pojos

import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.UsuarioCore
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.Vuelos

class DeshieloToServer {
    var vuelo: Vuelos? = null
    var fechaCreacion: String? = null
    var creadoPor: String? = ""
    var tipoFluido: String? = ""
    var mezclaFluido: Double = 0.0
    var horaComienzo: String? = ""
    var horaTermino: String? = ""
    var temperaturaAmbiente: String? = ""
    var temperaturaCongelamiento: String? = ""
    var deshieloManual: Boolean? = false
    var holdoverTime: String? = ""

    var hielo: Boolean? = false
    var lluviaSobreEnfriada: Boolean? = false
    var nevadaFuerte: Boolean? = false
    var nevadaLigera: Boolean? = false
    var escarcha: Boolean? = false

    var capitan: UsuarioCore? = null
    var aplicador: UsuarioCore? = null
    var oficialOperaciones: UsuarioCore? = null
    var firmaOficialOperaciones: String? = null

}