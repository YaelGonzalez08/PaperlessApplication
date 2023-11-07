package com.aeromexico.aeropuertos.paperlessmobile.busquedaVuelo.model

data class RequestBuscarVuelo(var Origen: String?=null, var Destino: String? = null, var NumVuelo: Long? = null, var FechaVuelo: String? = null)
