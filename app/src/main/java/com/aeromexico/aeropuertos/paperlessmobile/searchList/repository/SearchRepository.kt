package com.aeromexico.aeropuertos.paperlessmobile.searchList.repository

import com.aeromexico.aeropuertos.paperlessmobile.searchList.dataSource.SearchListDataSource
import com.aeromexico.aeropuertos.paperlessmobile.webService.IAPI.SearchListApi

class SearchRepository(private val serachListApi: SearchListApi) {
    fun requDataSourceRepo(): SearchListDataSource{
        return SearchListDataSource(serachListApi)
    }
}