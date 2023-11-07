package com.aeromexico.aeropuertos.paperlessmobile.notoc.adapter

import com.aeromexico.aeropuertos.paperlessmobile.notoc.entities.OtraCarga
import com.aeromexico.aeropuertos.paperlessmobile.notoc.entities.RequestNotoc
import com.aeromexico.aeropuertos.paperlessmobile.notoc.entities.InformacionMercancia

interface InfoMercanciaClickListener {
    fun onClick(info: InformacionMercancia, posicion:Int)
}