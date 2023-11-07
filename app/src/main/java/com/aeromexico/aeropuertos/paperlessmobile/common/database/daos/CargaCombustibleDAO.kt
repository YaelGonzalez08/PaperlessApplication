package com.aeromexico.aeropuertos.paperlessmobile.common.database.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.aeromexico.aeropuertos.paperlessmobile.common.database.entities.CargacombustibleEntity

@Dao
interface CargaCombustibleDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addCargaCombustible(data: CargacombustibleEntity): Long
    @Update
    fun updateCargaCombustible(data: CargacombustibleEntity): Int

    @Query("SELECT * FROM ${CargacombustibleEntity.TABLE_NAME}")
    fun readAllCargaCombustible(): LiveData<List<CargacombustibleEntity>>

    @Query("DELETE FROM ${CargacombustibleEntity.TABLE_NAME} WHERE id=:id")
    fun deleteCargaCombustiblecheckById(id: Int)
}