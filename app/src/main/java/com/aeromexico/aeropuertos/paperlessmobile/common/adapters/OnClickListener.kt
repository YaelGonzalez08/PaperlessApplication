package com.aeromexico.aeropuertos.paperlessmobile.common.adapters

import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.Vuelos

interface OnClickListener {
    fun onClick(flightEntity: Vuelos)
}