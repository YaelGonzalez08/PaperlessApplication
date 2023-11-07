package com.aeromexico.aeropuertos.paperlessmobile.common.database.interactors

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aeromexico.aeropuertos.paperlessmobile.common.database.daos.CargaCombustibleDAO
import com.aeromexico.aeropuertos.paperlessmobile.common.database.entities.CargacombustibleEntity
import com.aeromexico.aeropuertos.paperlessmobile.ordenCargaCombustibles.entities.RequestOrdenCarga
import com.google.gson.Gson
import org.jetbrains.anko.doAsync

class CargaCombustibleInteractor(private val dao: CargaCombustibleDAO) {

    val  allCargaCombustible : LiveData<List<CargacombustibleEntity>> = readAllData()

    fun readAllData(): LiveData<List<CargacombustibleEntity>> {
        return dao.readAllCargaCombustible()
    }

    fun addCargaCombustible(data: RequestOrdenCarga): MutableLiveData<Long> {
        var addState = MutableLiveData<Long>()

        doAsync {
            addState.postValue(dao.addCargaCombustible(CargacombustibleEntity(request = Gson().toJson(data).toString())))
        }
        return addState
    }
    fun updateCargaCOmbustible(data: CargacombustibleEntity): MutableLiveData<Int> {
        var addState = MutableLiveData<Int>()

        doAsync {
            addState.postValue(dao.updateCargaCombustible(data))
        }

        return addState
    }

    fun deleteCargaCombustiblecheckById(id:Int){
        dao.deleteCargaCombustiblecheckById(id)
    }

}