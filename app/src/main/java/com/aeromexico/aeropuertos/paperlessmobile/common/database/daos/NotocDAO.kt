package com.aeromexico.aeropuertos.paperlessmobile.common.database.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.aeromexico.aeropuertos.paperlessmobile.common.database.entities.CheckPrimeVueloEntity
import com.aeromexico.aeropuertos.paperlessmobile.common.database.entities.NotocEntity

@Dao
interface NotocDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addForm(notocEntity: NotocEntity): Long

    @Query("SELECT * FROM ${NotocEntity.TABLE_NAME}")
    fun readAll(): MutableList<NotocEntity>

    @Update
    fun updateForm(notocEntity: NotocEntity): Int

    @Query("DELETE FROM ${NotocEntity.TABLE_NAME} WHERE id=:id")
    fun deleteNotocById(id: Long): Int
}