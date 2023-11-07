package com.aeromexico.aeropuertos.paperlessmobile.common.database.interactors

import com.aeromexico.aeropuertos.paperlessmobile.PaperlessApplication
import com.aeromexico.aeropuertos.paperlessmobile.common.database.entities.AveriaInspeccionAeronaveEntity
import com.aeromexico.aeropuertos.paperlessmobile.common.database.entities.ImagenInspecAeronaveEntity
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class ImagenInspecAeronaveInteractor {

    fun addImagebInspecAeronave(imagenesInspecAeronaveEntity: MutableList<ImagenInspecAeronaveEntity>, callback: (List<Long>) -> Unit){
        doAsync {
            val success = PaperlessApplication.database.imagenInspecAeronaveDAO().addImagenInspecAeronave(imagenesInspecAeronaveEntity)
            uiThread {
                callback(success)
            }
        }
    }

    fun getImagenesEnAveria( idAveria: Long, callback: (MutableList<ImagenInspecAeronaveEntity>) -> Unit){
        doAsync {
            val imagenes = PaperlessApplication.database.imagenInspecAeronaveDAO().getImagenesEnAveria(idAveria)
            uiThread {
                callback(imagenes)
            }
        }
    }


    fun deleteImagenesEnAveria(idAveria: Long, callback: (Int) -> Unit){
        doAsync {
            val id = PaperlessApplication.database.imagenInspecAeronaveDAO().deleteImagenesEnAveria(idAveria)
            uiThread {
                callback(id)
            }
        }
    }


}