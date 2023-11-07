package com.aeromexico.aeropuertos.paperlessmobile.inspeccionEquipo.repository

import com.aeromexico.aeropuertos.paperlessmobile.inspeccionEquipo.dataSource.CheckListDataSource
import com.aeromexico.aeropuertos.paperlessmobile.webService.IAPI.CheckListAPI

class CheckListRepository(private val checkAPI: CheckListAPI) {

    fun requestCheckListDataSource(): CheckListDataSource {
        return CheckListDataSource(checkAPI)
    }
}