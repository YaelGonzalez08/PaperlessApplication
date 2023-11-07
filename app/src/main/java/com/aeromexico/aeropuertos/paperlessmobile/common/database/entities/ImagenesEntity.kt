package com.aeromexico.aeropuertos.paperlessmobile.common.database.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import org.jetbrains.annotations.NotNull

@Parcelize
@Entity(tableName = ImagenesEntity.TABLE_NAME)
data class ImagenesEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,
    @ColumnInfo(name = "guid") @NotNull val guid: String,
    @ColumnInfo(name = "IsLlegada") @NotNull val IsLlegada: Boolean,
    @ColumnInfo(name = "codigo") @NotNull val codigo: String,
    @ColumnInfo(name = "datos") @NotNull var datos: String,
):Parcelable{
    companion object {
        const val TABLE_NAME = "ImagenesEntity"
    }
}


