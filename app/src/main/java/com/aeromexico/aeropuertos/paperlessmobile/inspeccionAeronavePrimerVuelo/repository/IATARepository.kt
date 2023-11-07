package com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronavePrimerVuelo.repository

import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronavePrimerVuelo.dataSource.IATADataSource
import com.aeromexico.aeropuertos.paperlessmobile.webService.IAPI.IATAApi

class IATARepository(private val iataApi: IATAApi) {
    fun requestIatasDataSource(): IATADataSource {
        return IATADataSource(
            iataApi
        )
    }
}