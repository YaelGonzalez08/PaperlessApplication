package com.aeromexico.aeropuertos.paperlessmobile.comunicadosACK.repository

import com.aeromexico.aeropuertos.paperlessmobile.common.CORE.CoreDataSource
import com.aeromexico.aeropuertos.paperlessmobile.comunicadosACK.datasource.comunicadosDataSource
import com.aeromexico.aeropuertos.paperlessmobile.webService.IAPI.ComunicadosAPI

class ComunicadosRepository(var api:ComunicadosAPI) {

    fun getComunicadosDatasource() = comunicadosDataSource(api)

}