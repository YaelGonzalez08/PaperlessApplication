package com.aeromexico.aeropuertos.paperlessmobile.common.database.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aeromexico.aeropuertos.paperlessmobile.common.database.entities.CheckEquipoDiarioEntity
import com.aeromexico.aeropuertos.paperlessmobile.common.database.entities.DeshieloEntity

@Dao
interface DeshieloDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addDeshieloCheck(data: DeshieloEntity): Long

    @Query("SELECT * FROM ${DeshieloEntity.TABLE_NAME}")
    fun readAllDeshielo(): LiveData<List<DeshieloEntity>>

    @Query("DELETE FROM ${DeshieloEntity.TABLE_NAME} WHERE id=:id")
    fun deleteDeshielocheckById(id: Int)
}