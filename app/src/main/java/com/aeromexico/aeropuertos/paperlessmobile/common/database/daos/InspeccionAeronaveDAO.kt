package com.aeromexico.aeropuertos.paperlessmobile.common.database.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aeromexico.aeropuertos.paperlessmobile.common.database.entities.DeshieloEntity
import com.aeromexico.aeropuertos.paperlessmobile.common.database.entities.ImagenesEntity
import com.aeromexico.aeropuertos.paperlessmobile.common.database.entities.InspeccionDatosEntity
import com.aeromexico.aeropuertos.paperlessmobile.common.database.entities.NotocEntity

@Dao
interface InspeccionAeronaveDAO {
    @Query("SELECT * FROM ${ImagenesEntity.TABLE_NAME}")
    fun readAllImagenesEntity(): List<ImagenesEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addImagen(imagen: ImagenesEntity): Long

    @Query("DELETE FROM ${ImagenesEntity.TABLE_NAME} WHERE guid=:guid AND IsLlegada=:isLlegada")
    fun deleteByGuid(guid: String,isLlegada:Boolean): Int
}