package com.aeromexico.aeropuertos.paperlessmobile.notoc.pendientes.adapter

import com.aeromexico.aeropuertos.paperlessmobile.common.database.entities.NotocEntity
import com.aeromexico.aeropuertos.paperlessmobile.notoc.entities.RequestNotoc

interface OnClickListener {
    fun onClick(notoc: NotocEntity)
    fun onDelete(notoc: NotocEntity)
}