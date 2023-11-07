package com.aeromexico.aeropuertos.paperlessmobile.mensajesOperacionales.model

import androidx.lifecycle.MutableLiveData
import com.aeromexico.aeropuertos.paperlessmobile.PaperlessApplication
import com.aeromexico.aeropuertos.paperlessmobile.common.database.entities.ModificarDetalleLirEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class MOInteractor {
    var cambioFirmas = MutableLiveData<Boolean>()


    /*fun getFlightsRoom(callback: (MutableList<FlightEntity>) -> Unit){
        doAsync {
            val flightList = PaperlessApplication.database.flightDao().getAllFlights()
            uiThread {
                val json = Gson().toJson(flightList)
                callback(flightList)
            }
        }
    }*/

    fun saveFirmasLir(modificarDetalleLirEntity: ModificarDetalleLirEntity,
                      callback: (Long) -> Unit){
        doAsync {
            val newId = PaperlessApplication.database.mODao().addDetalleMensaje(modificarDetalleLirEntity)
            PaperlessApplication.database.mODao().deleteDetalleMensaje(modificarDetalleLirEntity)
            uiThread {
                cambioFirmas.postValue(true)
                callback(newId)
            }
        }
    }

    fun getFirmasById(id: Long, callback:(ModificarDetalleLirEntity) -> Unit){
        doAsync {
            val firmas = PaperlessApplication.database.mODao().getDetalleMensajeById(id)
            uiThread {
                callback(firmas)
            }
        }
    }

    fun deleteFirmaById(id: Long, callback: (Int) -> Unit){
        doAsync {
            val bool = PaperlessApplication.database.mODao().deleteById(id)
            uiThread {
                cambioFirmas.postValue(true)
                callback(bool)
            }
        }
    }

    fun getAllFirmasPendientes(callback: (MutableList<ModificarDetalleLirEntity>) -> Unit){
        doAsync {
            val firmas = PaperlessApplication.database.mODao().getAllDetalleMensaje()
            uiThread {
                callback(firmas)
            }
        }
    }

}