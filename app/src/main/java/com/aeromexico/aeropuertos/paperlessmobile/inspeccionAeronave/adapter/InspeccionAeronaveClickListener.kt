package com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronave.adapter

import com.aeromexico.aeropuertos.paperlessmobile.common.database.entities.InspeccionAeronaveEntity

interface InspeccionAeronaveClickListener {

    fun onSend(inspeccionAeronaveEntity: InspeccionAeronaveEntity);

    fun onDelete(inspeccionAeronaveEntity: InspeccionAeronaveEntity);
}