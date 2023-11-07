package com.aeromexico.aeropuertos.paperlessmobile.common.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aeromexico.aeropuertos.paperlessmobile.common.database.entities.ImagenesEntity
import com.aeromexico.aeropuertos.paperlessmobile.common.database.entities.InspeccionDatosEntity

@Dao
interface InspeccionDatosDAO {

    @Query("SELECT * FROM ${InspeccionDatosEntity.TABLE_NAME}")
    fun readAllInspeccionDatosEntity(): List<InspeccionDatosEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addDatosInspeccion(imagen: InspeccionDatosEntity): Long

    @Query("DELETE FROM ${InspeccionDatosEntity.TABLE_NAME} WHERE guid=:guid AND IsLlegada=:isLlegada")
    fun deleteByGuid(guid: String,isLlegada:Boolean): Int
}