package com.aeromexico.aeropuertos.paperlessmobile.common.database.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.aeromexico.aeropuertos.paperlessmobile.common.database.entities.ModificarDetalleLirEntity

@Dao
interface MODao {
    @Query("SELECT * FROM ModificarDetalleLirEntity")
    fun getAllDetalleMensaje(): MutableList<ModificarDetalleLirEntity>

    @Query("SELECT * FROM ModificarDetalleLirEntity where id = :id")
    fun getDetalleMensajeById(id: Long): ModificarDetalleLirEntity

    @Insert
    fun addDetalleMensaje(detalleLirEntity: ModificarDetalleLirEntity):Long

    @Delete
    fun deleteDetalleMensaje(detalleLirEntity: ModificarDetalleLirEntity)

    @Query("delete from ModificarDetalleLirEntity where id = :id")
    fun deleteById(id: Long): Int
}