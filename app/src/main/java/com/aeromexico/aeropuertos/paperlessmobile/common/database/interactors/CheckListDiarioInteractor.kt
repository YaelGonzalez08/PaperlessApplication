package com.aeromexico.aeropuertos.paperlessmobile.common.database.interactors

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aeromexico.aeropuertos.paperlessmobile.common.database.daos.CheckListEquipoDriarioDAO
import com.aeromexico.aeropuertos.paperlessmobile.common.database.entities.CheckEquipoDiarioEntity
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionEquipo.pojos.CheckToServer
import com.google.gson.Gson
import org.jetbrains.anko.doAsync

class CheckListDiarioInteractor(private val dao: CheckListEquipoDriarioDAO) {

    val allChecks: LiveData<List<CheckEquipoDiarioEntity>> = readAllChecks()

    fun addCheckEquipo(
        data: CheckToServer
    ): MutableLiveData<Long> {

        var addState = MutableLiveData<Long>()

        doAsync {
            addState.postValue(
                dao.addCheckEquipo(
                    CheckEquipoDiarioEntity(
                        datos = Gson().toJson(data).toString()
                    )
                )
            )
        }

        return addState
    }

    fun readAllChecks(): LiveData<List<CheckEquipoDiarioEntity>> {
        return dao.readAllChecks()
    }


    fun deleteCheckEquipoById(id: Int) {

        doAsync {
            dao.deleteCheckEquipoById(id)
        }

    }


}