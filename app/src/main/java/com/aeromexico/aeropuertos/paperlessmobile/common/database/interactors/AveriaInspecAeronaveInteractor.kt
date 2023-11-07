package com.aeromexico.aeropuertos.paperlessmobile.common.database.interactors

import com.aeromexico.aeropuertos.paperlessmobile.PaperlessApplication
import com.aeromexico.aeropuertos.paperlessmobile.common.database.entities.AveriaInspeccionAeronaveEntity
import com.aeromexico.aeropuertos.paperlessmobile.common.database.entities.InspeccionAeronaveEntity
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class AveriaInspecAeronaveInteractor {


    fun getAveriasEnInspeccion(idInspeccionAeronave: Long,  callback: (MutableList<AveriaInspeccionAeronaveEntity>) -> Unit){
        doAsync {
            val averias = PaperlessApplication.database.averiaInspecAeronaveDAO().getAveriasEnInspeccion(idInspeccionAeronave)
            uiThread {
                callback(averias)
            }
        }
    }


    fun addAveriaInspecAeronave(averiaInspeccionAeronaveEntity: AveriaInspeccionAeronaveEntity, callback: (Long) -> Unit){
        doAsync {
            val newId = PaperlessApplication.database.averiaInspecAeronaveDAO().addAveriaInspecAeronave(averiaInspeccionAeronaveEntity)
            PaperlessApplication.database.averiaInspecAeronaveDAO().delete(averiaInspeccionAeronaveEntity)
            uiThread {
                callback(newId)
            }
        }
    }


    fun addAveriaInspecAeronave(averiasInspeccionAeronaveEntity: MutableList<AveriaInspeccionAeronaveEntity>, callback: (List<Long>) -> Unit){
        doAsync {
            val success = PaperlessApplication.database.averiaInspecAeronaveDAO().addAveriaInspecAeronave(averiasInspeccionAeronaveEntity)
            //PaperlessApplication.database.averiaInspecAeronaveDAO().delete(averiaInspeccionAeronaveEntity)
            uiThread {
                callback(success)
            }
        }
    }


    fun deleteAveriasEnInspeccion(idInspeccionAeronave: Long, callback: (Int) -> Unit){
        doAsync {
            val id = PaperlessApplication.database.averiaInspecAeronaveDAO().deleteAveriasEnInspeccion(idInspeccionAeronave)

            var a = id;
            uiThread {
                callback(id)
            }
        }
    }
}