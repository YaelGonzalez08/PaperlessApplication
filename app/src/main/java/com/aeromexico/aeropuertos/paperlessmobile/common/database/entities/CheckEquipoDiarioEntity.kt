package com.aeromexico.aeropuertos.paperlessmobile.common.database.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import org.jetbrains.annotations.NotNull

@Parcelize
@Entity(tableName = CheckEquipoDiarioEntity.TABLE_NAME)
data class CheckEquipoDiarioEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,
    @ColumnInfo(name
    = "datos") @NotNull val datos: String,

    ) : Parcelable {
    companion object {
        const val TABLE_NAME = "CheckEquipoDiarioEntity"
    }
}



