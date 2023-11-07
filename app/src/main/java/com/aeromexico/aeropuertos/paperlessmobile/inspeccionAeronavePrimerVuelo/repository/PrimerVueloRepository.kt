package com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronavePrimerVuelo.repository

import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronavePrimerVuelo.dataSource.PrimerVueloDataSource
import com.aeromexico.aeropuertos.paperlessmobile.webService.IAPI.PrimerVueloDiaApi

class PrimerVueloRepository(private val primerVueloDiaApi: PrimerVueloDiaApi) {

    fun requestPrimerVueloDataSource():PrimerVueloDataSource {
        return PrimerVueloDataSource(primerVueloDiaApi)
    }
}