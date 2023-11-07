package com.aeromexico.aeropuertos.paperlessmobile.common.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.aeromexico.aeropuertos.paperlessmobile.common.database.entities.ImagenInspecAeronaveEntity

@Dao
interface ImagenInspecAeronabeDAO {
    @Insert
    fun addImagenInspecAeronave(imagenesInspecAeronaveEntity: MutableList<ImagenInspecAeronaveEntity>): List<Long>

    @Query("SELECT * FROM ImagenInspecAeronaveEntity where idAveria = :idAveria")
    fun getImagenesEnAveria(idAveria: Long): MutableList<ImagenInspecAeronaveEntity>

    @Query("delete from ImagenInspecAeronaveEntity where idAveria = :idAveria")
    fun deleteImagenesEnAveria(idAveria: Long): Int
}