package com.aeromexico.aeropuertos.paperlessmobile.common.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull
import java.math.BigInteger

@Entity(tableName = "ImagenInspecAeronaveEntity")
data class ImagenInspecAeronaveEntity(
        @PrimaryKey(autoGenerate = true) @NotNull var id: Long=0,
        @NotNull var idAveria: Long=0,
        @NotNull var imagen: String= "",
    )
{
    constructor(): this(
        0,
        idAveria = 0,
        imagen = ""
    )

}