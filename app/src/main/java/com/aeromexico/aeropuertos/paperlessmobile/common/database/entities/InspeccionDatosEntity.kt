package com.aeromexico.aeropuertos.paperlessmobile.common.database.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import org.jetbrains.annotations.NotNull

@Parcelize
@Entity(tableName = InspeccionDatosEntity.TABLE_NAME)
data class InspeccionDatosEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,
    @ColumnInfo(name = "guid") @NotNull val guid: String,
    @ColumnInfo(name = "IsLlegada") @NotNull val IsLlegada: Boolean,
    @ColumnInfo(name = "datos") @NotNull var datos: String,
): Parcelable {
    companion object {
        const val TABLE_NAME = "InspeccionDatosEntity"
    }
}