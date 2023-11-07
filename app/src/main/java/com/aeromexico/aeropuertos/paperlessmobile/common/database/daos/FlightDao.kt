package com.aeromexico.aeropuertos.paperlessmobile.common.database.daos

import androidx.room.*
import com.aeromexico.aeropuertos.paperlessmobile.common.database.entities.FlightEntity

@Dao
interface FlightDao {
    @Query("SELECT * FROM FlightEntity")
    fun getAllFlights(): MutableList<FlightEntity>

    @Insert
    fun addFlight(flight: FlightEntity):Long

    @Update
    fun updateFlight(flight: FlightEntity)

    @Delete
    fun deleteFlight(flight: FlightEntity)
}