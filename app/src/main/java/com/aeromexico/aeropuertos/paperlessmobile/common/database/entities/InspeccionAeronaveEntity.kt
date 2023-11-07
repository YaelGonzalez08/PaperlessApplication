package com.aeromexico.aeropuertos.paperlessmobile.common.database.entities

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity(tableName = "InspeccionAeronaveEntity")
data class InspeccionAeronaveEntity(

        //Inspección (header)
        @PrimaryKey(autoGenerate = true) var id: Long=0,
        var GuidVuelo: String= "",
        var CreadoPor: String = "",
        var FechaCreacion: String = "",
        var NombreOficialOperaciones: String = "",
        var NoEmpleadoOficialOperaciones: String= "",
        var FirmaB64OficialOperaciones: String = "",

        var NombreTecnicoMantenimiento: String = "",
        var NoEmpleadoTecnicoMantenimiento: String = "",
        var FirmaB64TecnicoMantenimiento: String = "",
        var ResponsablesEstiba: String = "",
        var Pernocta: Boolean = false,
        var EsLlegada: Boolean? = false,


        //Datos del vuelo
        var Origen: String = "",
        var Destino: String = "",
        var NumVuelo : String = "",
        var FechaVuelo: String = "",
        var Matricula : String = "",
        var Equipo : String = "",

        //Auxiliar para RQ
        @Ignore
        var Averias: MutableList<AveriaInspeccionAeronaveEntity> = mutableListOf()

)