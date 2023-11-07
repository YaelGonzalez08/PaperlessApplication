package com.aeromexico.aeropuertos.paperlessmobile.mensajesOperacionales.pendientes.adapters

import com.aeromexico.aeropuertos.paperlessmobile.common.database.entities.ModificarDetalleLirEntity

interface OnClickListenerMOPendientes {
    fun onClick(detalleLirEntity: ModificarDetalleLirEntity)
    fun onDelete(detalleLirEntity: ModificarDetalleLirEntity)
}