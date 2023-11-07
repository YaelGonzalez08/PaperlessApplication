package com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronavePrimerVuelo.repository

import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronavePrimerVuelo.dataSource.VueloManualDataSource
import com.aeromexico.aeropuertos.paperlessmobile.webService.IAPI.VueloAPI

class VueloManulaRepository(private val vueloAPI: VueloAPI) {

    fun requestVueloManualDataSource(): VueloManualDataSource{
        return VueloManualDataSource(
            vueloAPI
        )
    }
}