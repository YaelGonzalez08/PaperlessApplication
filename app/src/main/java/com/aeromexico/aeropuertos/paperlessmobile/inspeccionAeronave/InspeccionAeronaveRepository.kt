package com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronave

import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronave.DataSource.InspeccionAeronaveDataSource
import com.aeromexico.aeropuertos.paperlessmobile.mensajesOperacionales.model.MensajesOperacionalesDataSource
import com.aeromexico.aeropuertos.paperlessmobile.webService.IAPI.InspeccionAeronaveAPI


class InspeccionAeronaveRepository(private val inspeccionAeronaveAPI: InspeccionAeronaveAPI) {
    fun getAPI(): InspeccionAeronaveDataSource {
        return InspeccionAeronaveDataSource(inspeccionAeronaveAPI)
    }


    fun createInspeccionAeronave(): InspeccionAeronaveDataSource {
        return  InspeccionAeronaveDataSource(inspeccionAeronaveAPI)
    }
}