package com.aeromexico.aeropuertos.paperlessmobile.common.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ModificarDetalleLirEntity")
data class ModificarDetalleLirEntity(@PrimaryKey(autoGenerate = true) var id: Long=0,
                                     var numEmpleadoRampa: String,
                                     var nombreRampa: String,
                                     var firmaRampa: String,
                                     var remarks: String,
                                     /*var numEmpleadoDespacho: String,
                                     var nombreDespacho: String,
                                     var firmaDespacho: String,*/
                                     var CreadoPor: String,
                                     var fechaCreacion: String,
                                     var idMensajeOps: Long,
                                     var NumVuelo: Int,
                                     var Aerolinea: String,
                                     var Origen: String,
                                     var Destino: String,
                                     var FechaVueloLocal: String,
                                     var matricula: String,
                                     var equipo: String){
    constructor() : this(numEmpleadoRampa = "", nombreRampa = "",
            firmaRampa = "",remarks = "",
        /* numEmpleadoDespacho = "", nombreDespacho = "",
            firmaDespacho = "",*/
            CreadoPor = "", fechaCreacion = "",
            idMensajeOps = 0L, NumVuelo = 0, Aerolinea = "", Origen = "", Destino = "",
            FechaVueloLocal = "", matricula = "", equipo = "")

    override fun equals(other: Any?): Boolean {
        if(this === other) return true
        if(javaClass != other?.javaClass) return true

        other as ModificarDetalleLirEntity
        if(id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}

