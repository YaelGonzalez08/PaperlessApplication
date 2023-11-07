package com.aeromexico.aeropuertos.paperlessmobile.notoc.adapter

import com.aeromexico.aeropuertos.paperlessmobile.notoc.entities.OtraCarga

interface InfoOtraCargaClickListener {
    fun onClick(info: OtraCarga, posicion:Int)
}