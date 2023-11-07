package com.aeromexico.aeropuertos.paperlessmobile.notoc

import com.aeromexico.aeropuertos.paperlessmobile.notoc.model.NotocDataSource
import com.aeromexico.aeropuertos.paperlessmobile.webService.IAPI.NotocApi

class NotocRepository(private val notocApi: NotocApi) {
    fun getDataSource(): NotocDataSource {
        return NotocDataSource(notocApi)
    }
}