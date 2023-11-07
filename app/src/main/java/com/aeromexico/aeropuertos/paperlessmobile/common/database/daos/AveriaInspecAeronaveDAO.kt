package com.aeromexico.aeropuertos.paperlessmobile.common.database.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.aeromexico.aeropuertos.paperlessmobile.common.database.entities.AveriaInspeccionAeronaveEntity
import com.aeromexico.aeropuertos.paperlessmobile.common.database.entities.InspeccionAeronaveEntity

@Dao
interface AveriaInspecAeronaveDAO {
    @Insert
    fun addAveriaInspecAeronave(averiaInspeccionAeronaveEntity: AveriaInspeccionAeronaveEntity):Long

    @Insert
    fun addAveriaInspecAeronave(averiasInspeccionAeronaveEntity: MutableList<AveriaInspeccionAeronaveEntity>): List<Long>

    @Query("SELECT * FROM AveriaInspeccionAeronaveEntity where idInspeccionAeronave = :idInspeccion")
    fun getAveriasEnInspeccion(idInspeccion: Long): MutableList<AveriaInspeccionAeronaveEntity>

    @Delete
    fun delete(averiaInspeccionAeronaveEntity: AveriaInspeccionAeronaveEntity)


    @Query("delete from AveriaInspeccionAeronaveEntity where idInspeccionAeronave = :idInspeccion")
    fun deleteAveriasEnInspeccion(idInspeccion: Long): Int

}