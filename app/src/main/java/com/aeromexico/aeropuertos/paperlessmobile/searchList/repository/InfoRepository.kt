package com.aeromexico.aeropuertos.paperlessmobile.searchList.repository

import com.aeromexico.aeropuertos.paperlessmobile.searchList.dataSource.GetInfoDataSource
import com.aeromexico.aeropuertos.paperlessmobile.searchList.dataSource.SearchListDataSource
import com.aeromexico.aeropuertos.paperlessmobile.webService.IAPI.GetInfoApi
import com.aeromexico.aeropuertos.paperlessmobile.webService.IAPI.SearchListApi

class InfoRepository(private val infoApi: GetInfoApi) {
    fun requInfoDataSourceRepo(): GetInfoDataSource {
        return GetInfoDataSource(infoApi)
    }
}