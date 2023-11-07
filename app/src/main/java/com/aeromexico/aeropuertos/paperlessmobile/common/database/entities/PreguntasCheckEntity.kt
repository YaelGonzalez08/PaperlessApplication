package com.aeromexico.aeropuertos.paperlessmobile.common.database.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import org.jetbrains.annotations.NotNull


@Parcelize
@Entity(tableName = PreguntasCheckEntity.TABLE_NAME)
data class PreguntasCheckEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "index") val index: Int=0,
    @ColumnInfo(name = "fecha") @NotNull val fecha: String,
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "estate") val estate: Int,
    @ColumnInfo(name = "observation") val observation: String? = "",
    @ColumnInfo(name = "photo") val photo: String? = ""

) : Parcelable {
    companion object {
        const val TABLE_NAME = "PreguntasCheckEntity"
    }

}