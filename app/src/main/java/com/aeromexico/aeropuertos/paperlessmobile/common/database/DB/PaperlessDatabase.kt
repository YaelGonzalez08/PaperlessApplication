package com.aeromexico.aeropuertos.paperlessmobile.common.database.DB

import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.aeromexico.aeropuertos.paperlessmobile.common.database.daos.*
import com.aeromexico.aeropuertos.paperlessmobile.common.database.entities.*
import org.jetbrains.annotations.NotNull


@Database(
    entities = [
        CargacombustibleEntity::class,
        DeshieloEntity::class,
        ModificarDetalleLirEntity::class,
        CheckEquipoDiarioEntity::class,
        CheckPrimeVueloEntity::class,
        InspeccionAeronaveEntity::class,
        AveriaInspeccionAeronaveEntity::class,
        ImagenInspecAeronaveEntity::class,
        PreguntasCheckEntity::class,
        NotocEntity::class,
        ImagenesEntity::class,
        InspeccionDatosEntity::class               ],
    version = 8,
    exportSchema = false
)

abstract class PaperlessDatabase : RoomDatabase() {
    // recuerda si agregas una entity o dao sube la version si no habra que desinstalar la app para que no truene
    abstract fun mODao(): MODao
    abstract fun checkListEquipoDriarioDAO(): CheckListEquipoDriarioDAO
    abstract fun inspecAeronaveDAO(): InspecAeronaveDAO
    abstract fun primerVueloDia(): PrimerVueloDiaDAO
    abstract fun averiaInspecAeronaveDAO(): AveriaInspecAeronaveDAO
    abstract fun imagenInspecAeronaveDAO(): ImagenInspecAeronabeDAO
    abstract fun preguntasCheckDAO(): PreguntasCheckDAO
    abstract fun deshieloDao(): DeshieloDao
    abstract fun cargaCombustible():CargaCombustibleDAO
    abstract fun notocDAO(): NotocDAO
    abstract fun getInspeccionAeronaveDAO():InspeccionAeronaveDAO
    abstract fun getInspeccionDatosDAO():InspeccionDatosDAO

    companion object {

        @Volatile
        private var INSTANCE: PaperlessDatabase? = null

        val MIGRATION_1_2 = object : Migration(1, 2) {
             override fun migrate(database: SupportSQLiteDatabase) {
                 database.execSQL("CREATE TABLE CheckPrimerVueloEntity(id INTEGER PRIMARY KEY NOT NULL, request TEXT NOT NULL)")
             }
         }
        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE CargacombustibleEntity(id INTEGER PRIMARY KEY NOT NULL, request TEXT NOT NULL)")
            }
        }

        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE ImagenInspecAeronaveEntity(id INTEGER PRIMARY KEY NOT NULL, idAveria INTEGER NOT NULL, imagen TEXT NOT NULL)")
            }
        }

        val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE InspeccionAeronaveEntity ADD COLUMN EsLlegada INTEGER")
            }
        }

        val Migration_5_6 = object : Migration(5,6){
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE NotocEntity(id INTEGER PRIMARY KEY NOT NULL, request TEXT NOT NULL)")
            }
        }
        val Migration_6_7 = object : Migration(6,7){
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE ImagenesEntity(id INTEGER PRIMARY KEY NOT NULL, guid TEXT NOT NULL, IsLlegada INTEGER NOT NULL DEFAULT(0), codigo TEXT NOT NULL, datos TEXT NOT NULL)")
            }
        }
        val Migration_7_8 = object : Migration(7,8){
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE InspeccionDatosEntity(id INTEGER PRIMARY KEY NOT NULL, guid TEXT NOT NULL, IsLlegada INTEGER NOT NULL DEFAULT(0), datos TEXT NOT NULL)")
            }
        }

        fun getDatabase(context: Context): PaperlessDatabase {
            val temInstance = INSTANCE
            if (temInstance != null) {
                return temInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PaperlessDatabase::class.java,
                    "PaperlessDatabase"
                ).addMigrations(MIGRATION_1_2,MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5, Migration_5_6,Migration_6_7,Migration_7_8)
                    .allowMainThreadQueries()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }

}