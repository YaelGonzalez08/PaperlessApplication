package com.aeromexico.aeropuertos.paperlessmobile.common.database.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.aeromexico.aeropuertos.paperlessmobile.common.database.entities.InspeccionAeronaveEntity
import com.aeromexico.aeropuertos.paperlessmobile.common.database.entities.ModificarDetalleLirEntity

@Dao
interface InspecAeronaveDAO {

    @Insert
    fun addInspecAeronave(inspeccionAeronaveEntity: InspeccionAeronaveEntity):Long

    @Query("SELECT * FROM InspeccionAeronaveEntity")
    fun getAllInspecAeronave(): MutableList<InspeccionAeronaveEntity>

    @Query("delete from InspeccionAeronaveEntity where id = :id")
    fun deleteById(id: Long): Int

    @Delete
    fun delete(inspeccionAeronaveEntity: InspeccionAeronaveEntity): Int
}