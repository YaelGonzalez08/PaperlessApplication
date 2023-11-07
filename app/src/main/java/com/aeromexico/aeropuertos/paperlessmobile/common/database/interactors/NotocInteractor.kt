package com.aeromexico.aeropuertos.paperlessmobile.common.database.interactors

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aeromexico.aeropuertos.paperlessmobile.PaperlessApplication
import com.aeromexico.aeropuertos.paperlessmobile.common.database.entities.CheckPrimeVueloEntity
import com.aeromexico.aeropuertos.paperlessmobile.common.database.entities.ModificarDetalleLirEntity
import com.aeromexico.aeropuertos.paperlessmobile.common.database.entities.NotocEntity
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronavePrimerVuelo.entities.RequestFirstFlightForm
import com.aeromexico.aeropuertos.paperlessmobile.notoc.entities.RequestNotoc
import com.google.gson.Gson
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class NotocInteractor {


    fun readAll(callback: (MutableList<NotocEntity>) -> Unit) {
        doAsync {
            val forms = PaperlessApplication.database.notocDAO().readAll()
            uiThread {
                callback(forms)
            }
        }
    }

    fun addForm( requestFirstFlightForm: RequestNotoc): MutableLiveData<Long> {
        var addState = MutableLiveData<Long>()

        doAsync { addState.postValue(
            PaperlessApplication.database.notocDAO().addForm(NotocEntity(request = Gson().toJson(requestFirstFlightForm).toString()))
        ) }
        return addState
    }

    fun updateFormNotoc(requestFirstFlightForm: NotocEntity): MutableLiveData<Int> {
        val updateState = MutableLiveData<Int>()

        doAsync { updateState.postValue(
            PaperlessApplication.database.notocDAO().updateForm(requestFirstFlightForm)
        ) }
        return updateState
    }

    fun deleteNotocById(id: Long, callback: (Int) -> Unit){
        doAsync {
            var bool = PaperlessApplication.database.notocDAO().deleteNotocById(id)
            uiThread {
                callback(bool)
            }
        }

    }
}