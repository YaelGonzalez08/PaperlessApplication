package com.aeromexico.aeropuertos.paperlessmobile.common.database.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aeromexico.aeropuertos.paperlessmobile.common.database.entities.CheckEquipoDiarioEntity

@Dao
interface CheckListEquipoDriarioDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addCheckEquipo(data: CheckEquipoDiarioEntity): Long

    @Query("SELECT * FROM ${CheckEquipoDiarioEntity.TABLE_NAME}")
    fun readAllChecks(): LiveData<List<CheckEquipoDiarioEntity>>


    @Query("DELETE FROM ${CheckEquipoDiarioEntity.TABLE_NAME} WHERE id=:id")
    fun deleteCheckEquipoById(id: Int)

}

