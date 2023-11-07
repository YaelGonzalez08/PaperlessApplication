package com.aeromexico.aeropuertos.paperlessmobile.optBdm.repository

import com.aeromexico.aeropuertos.paperlessmobile.optBdm.model.OptBdmDataSource
import com.aeromexico.aeropuertos.paperlessmobile.webService.IAPI.OptBdmAPI

class OptBdmRepository(private val api: OptBdmAPI) {
    fun getAPI(): OptBdmDataSource {
        return OptBdmDataSource(api)
    }
}