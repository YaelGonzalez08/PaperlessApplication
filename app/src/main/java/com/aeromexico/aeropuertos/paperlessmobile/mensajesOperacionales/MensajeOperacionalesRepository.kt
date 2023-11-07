package com.aeromexico.aeropuertos.paperlessmobile.mensajesOperacionales

import com.aeromexico.aeropuertos.paperlessmobile.mensajesOperacionales.model.MensajesOperacionalesDataSource
import com.aeromexico.aeropuertos.paperlessmobile.webService.IAPI.MensajesOperacionalesAPI

class MensajeOperacionalesRepository(private val mensajesOperacionalesAPI: MensajesOperacionalesAPI) {
    fun getAPI(): MensajesOperacionalesDataSource {
        return MensajesOperacionalesDataSource(mensajesOperacionalesAPI)
    }
    fun saveDetalleLir(): MensajesOperacionalesDataSource{
        return  MensajesOperacionalesDataSource(mensajesOperacionalesAPI)
    }
}