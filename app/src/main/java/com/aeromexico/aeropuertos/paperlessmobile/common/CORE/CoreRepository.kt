package com.aeromexico.aeropuertos.paperlessmobile.common.CORE

import com.aeromexico.aeropuertos.paperlessmobile.webService.IAPI.CoreApi

class CoreRepository(private val coreAPI: CoreApi) {

    fun getCoreDataSource(): CoreDataSource{
        return CoreDataSource(coreAPI)
    }
}