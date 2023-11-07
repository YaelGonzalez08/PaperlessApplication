package com.aeromexico.aeropuertos.paperlessmobile.common.database.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.aeromexico.aeropuertos.paperlessmobile.common.database.entities.CargacombustibleEntity
import com.aeromexico.aeropuertos.paperlessmobile.common.database.entities.CheckPrimeVueloEntity
import com.aeromexico.aeropuertos.paperlessmobile.common.database.entities.FlightEntity

@Dao
interface PrimerVueloDiaDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addForm(checkPrimerVueloEntity: CheckPrimeVueloEntity): Long

    @Query("SELECT * FROM ${CheckPrimeVueloEntity.TABLE_NAME}")
    fun readAllChecks():LiveData<List<CheckPrimeVueloEntity>>

    @Update
    fun updateForm(checkPrimerVueloEntity: CheckPrimeVueloEntity): Int

    @Query("DELETE FROM ${CheckPrimeVueloEntity.TABLE_NAME} WHERE id=:id")
    fun deletePrimerVueloById(id: Int)
}