package com.aeromexico.aeropuertos.paperlessmobile.common.database.entities

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "AveriaInspeccionAeronaveEntity")
data class AveriaInspeccionAeronaveEntity(
        @PrimaryKey(autoGenerate = true) var id: Long=0,
        var CodigoAveria: String,
        var TieneAveria: Boolean,
        var DescripcionAveria: String?,
        var ImgB64: String?,
        var idInspeccionAeronave: Long = 0,

    //Auxiliar para RQ
    @Ignore
    var Imagenes: MutableList<String>? = mutableListOf()

){
        constructor(): this(
               0,
                CodigoAveria = "",
                TieneAveria = false,
                DescripcionAveria = "",
                ImgB64 = "",
                Imagenes = null
        )

        override fun equals(other: Any?): Boolean {
                if(this === other) return true
                if(javaClass != other?.javaClass) return true
                other as AveriaInspeccionAeronaveEntity
                if(id != other.id) return false
                return true
        }

        override fun hashCode(): Int {
                return id.hashCode()
        }
}
