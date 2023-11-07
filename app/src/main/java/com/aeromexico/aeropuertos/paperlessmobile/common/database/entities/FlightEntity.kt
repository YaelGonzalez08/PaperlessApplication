package com.aeromexico.aeropuertos.paperlessmobile.common.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "FlightEntity")
data class FlightEntity(@PrimaryKey(autoGenerate = true) var id: Long=0,
                        var FlightReferenceNumber: Long,
                        var NumVuelo: Int,
                        var Aerolinea: String,
                        var Origen: String,
                        var Destino: String,
                        var FechaVueloLocal: String
                        ){
    constructor(): this(FlightReferenceNumber = 0L,NumVuelo = 0,Aerolinea = "",Origen = "",
        Destino = "", FechaVueloLocal = "")

    override fun equals(other: Any?): Boolean {
        if(this === other) return true
        if(javaClass != other?.javaClass) return true

        other as FlightEntity
        if(id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
