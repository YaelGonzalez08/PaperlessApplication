package com.aeromexico.aeropuertos.paperlessmobile.ordenCargaCombustibles.repository

import com.aeromexico.aeropuertos.paperlessmobile.ordenCargaCombustibles.dataSource.OrdenCargaDataSource
import com.aeromexico.aeropuertos.paperlessmobile.webService.IAPI.OrdenCargaAPI

class OrdenCargaRepository(private val ordenCargaAPI: OrdenCargaAPI) {

    fun requestOrdenCargaDataSource(): OrdenCargaDataSource = OrdenCargaDataSource(ordenCargaAPI)

}