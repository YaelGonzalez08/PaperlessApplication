package com.aeromexico.aeropuertos.paperlessmobile.common.database.interactors

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aeromexico.aeropuertos.paperlessmobile.common.database.daos.DeshieloDao
import com.aeromexico.aeropuertos.paperlessmobile.common.database.entities.DeshieloEntity
import com.aeromexico.aeropuertos.paperlessmobile.desHielo.pojos.DeshieloToServer
import com.google.gson.Gson
import org.jetbrains.anko.doAsync

class DeshieloInteractor(private val daoDeshielo: DeshieloDao) {
    var allDeshielo: LiveData<List<DeshieloEntity>> = readAllDeshielo()

    fun addDeshieloCheck(
        data: DeshieloToServer
    ): MutableLiveData<Long> {

        var addState = MutableLiveData<Long>()

        doAsync {
            addState.postValue(daoDeshielo.addDeshieloCheck(DeshieloEntity(datos = Gson().toJson(data).toString())))
        }

        return addState
    }

    fun readAllDeshielo(): LiveData<List<DeshieloEntity>> {
        return daoDeshielo.readAllDeshielo()
    }

    fun deleteDeshielocheckById(id: Int) {

        doAsync {
            daoDeshielo.deleteDeshielocheckById(id)
        }

    }
}