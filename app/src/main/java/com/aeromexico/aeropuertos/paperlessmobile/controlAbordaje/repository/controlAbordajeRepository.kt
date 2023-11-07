package com.aeromexico.aeropuertos.paperlessmobile.controlAbordaje.repository

import com.aeromexico.aeropuertos.paperlessmobile.controlAbordaje.datasource.controlAbordajeDataSource
import com.aeromexico.aeropuertos.paperlessmobile.webService.IAPI.ControlAbordajeAPI

class controlAbordajeRepository(private val checkAPI: ControlAbordajeAPI) {

    fun getControlDatasource(): controlAbordajeDataSource {
        return controlAbordajeDataSource(checkAPI)
    }
}