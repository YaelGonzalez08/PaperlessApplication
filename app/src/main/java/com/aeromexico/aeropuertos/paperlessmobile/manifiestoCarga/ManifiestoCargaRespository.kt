package com.aeromexico.aeropuertos.paperlessmobile.manifiestoCarga

import com.aeromexico.aeropuertos.paperlessmobile.manifiestoCarga.model.ManifiestoCargaDataSource
import com.aeromexico.aeropuertos.paperlessmobile.webService.IAPI.ManifiestoCargaAPI

class ManifiestoCargaRespository(private val api: ManifiestoCargaAPI) {
    fun getDataSource(): ManifiestoCargaDataSource{
        return ManifiestoCargaDataSource(api)
    }
}