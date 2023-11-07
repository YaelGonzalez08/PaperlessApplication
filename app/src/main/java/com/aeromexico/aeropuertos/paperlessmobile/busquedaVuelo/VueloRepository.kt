package com.aeromexico.aeropuertos.paperlessmobile.busquedaVuelo

import com.aeromexico.aeropuertos.paperlessmobile.busquedaVuelo.model.BusquedaVueloDataSource
import com.aeromexico.aeropuertos.paperlessmobile.webService.IAPI.VueloAPI

class VueloRepository(private val vueloApi: VueloAPI) {
    fun requestBusquedaVuelo(): BusquedaVueloDataSource {
        return BusquedaVueloDataSource(
                vueloApi
        )
    }
}