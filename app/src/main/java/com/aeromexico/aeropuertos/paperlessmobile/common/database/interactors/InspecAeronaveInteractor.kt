package com.aeromexico.aeropuertos.paperlessmobile.common.database.interactors

import com.aeromexico.aeropuertos.paperlessmobile.PaperlessApplication
import com.aeromexico.aeropuertos.paperlessmobile.common.database.entities.InspeccionAeronaveEntity
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class InspecAeronaveInteractor {


    fun getAllInspecAeronave(callback: (MutableList<InspeccionAeronaveEntity>) -> Unit){
        doAsync {
            val inspecciones = PaperlessApplication.database.inspecAeronaveDAO().getAllInspecAeronave()
            uiThread {
                callback(inspecciones)
            }
        }
    }


    fun addInspecAeronave(inspeccionAeronaveEntity: InspeccionAeronaveEntity, callback: (Long) -> Unit){
        doAsync {
            val newId = PaperlessApplication.database.inspecAeronaveDAO().addInspecAeronave(inspeccionAeronaveEntity)
            PaperlessApplication.database.inspecAeronaveDAO().delete(inspeccionAeronaveEntity)
            uiThread {
               // cambioFirmas.postValue(true)
                callback(newId)
            }
        }
    }


    fun deleteById(id: Long, callback: (Int) -> Unit){
        doAsync {
            val bool = PaperlessApplication.database.inspecAeronaveDAO().deleteById(id)
            uiThread {
               // cambioFirmas.postValue(true)
                callback(bool)
            }
        }
    }


    fun deleteInspecAeronave(inspeccionAeronaveEntity: InspeccionAeronaveEntity, callback: (Int) -> Unit){
        doAsync {
            val id = PaperlessApplication.database.inspecAeronaveDAO().delete(inspeccionAeronaveEntity)
            uiThread {
                callback(id)
            }
        }
    }

}