package com.aeromexico.aeropuertos.paperlessmobile.inspeccionEquipo.pojos

import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.Equipo
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.UsuarioCore

data class CheckToServer(
    var creadoPor: String,
    var fecha: String,
    var lista: List<PreguntasCheckListToServer>,
    var supervisor: UsuarioCore?,
    var equipo: Equipo?,
    var operador: UsuarioCore?
)
