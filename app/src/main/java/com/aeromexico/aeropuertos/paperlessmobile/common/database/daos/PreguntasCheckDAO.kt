package com.aeromexico.aeropuertos.paperlessmobile.common.database.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aeromexico.aeropuertos.paperlessmobile.common.database.entities.PreguntasCheckEntity

@Dao
interface PreguntasCheckDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addPreguntaCheck(data: PreguntasCheckEntity): Long

    @Query("SELECT * FROM ${PreguntasCheckEntity.TABLE_NAME} WHERE fecha=:date")
    fun readPreguntakByFecha(date: String): LiveData<List<PreguntasCheckEntity>>

    @Query("DELETE FROM ${PreguntasCheckEntity.TABLE_NAME} WHERE fecha=:date")
    fun deletePreguntakByFecha(date: String)

}