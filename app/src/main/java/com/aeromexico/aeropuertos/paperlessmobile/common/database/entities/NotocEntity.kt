package com.aeromexico.aeropuertos.paperlessmobile.common.database.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import org.jetbrains.annotations.NotNull

@Parcelize
@Entity(tableName = NotocEntity.TABLE_NAME)
data class NotocEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0,
    @ColumnInfo(name = "request") @NotNull
    var request: String
): Parcelable{
    companion object{
        const val TABLE_NAME = "NotocEntity"
    }
}