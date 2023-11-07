package com.aeromexico.aeropuertos.paperlessmobile.common.database.interactors

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aeromexico.aeropuertos.paperlessmobile.common.database.daos.PrimerVueloDiaDAO
import com.aeromexico.aeropuertos.paperlessmobile.common.database.entities.CheckPrimeVueloEntity
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronavePrimerVuelo.entities.RequestFirstFlightForm
import com.google.gson.Gson
import org.jetbrains.anko.doAsync

class PrimerVueloDiaInteractor(private val primerVueloDiaDao: PrimerVueloDiaDAO) {
    val allForms: LiveData<List<CheckPrimeVueloEntity>> = readAllForms()

    private fun readAllForms(): LiveData<List<CheckPrimeVueloEntity>> {
        return primerVueloDiaDao.readAllChecks()
    }

    fun addForm( requestFirstFlightForm: RequestFirstFlightForm): MutableLiveData<Long>{
        var addState = MutableLiveData<Long>()

        doAsync { addState.postValue(
            primerVueloDiaDao.addForm(CheckPrimeVueloEntity(request = Gson().toJson(requestFirstFlightForm).toString()))
        ) }
        return addState
    }

    fun updateForm(requestFirstFlightForm: CheckPrimeVueloEntity): MutableLiveData<Int>{
        val updateState = MutableLiveData<Int>()

        doAsync { updateState.postValue(
            primerVueloDiaDao.updateForm(requestFirstFlightForm)
        ) }
        return updateState
    }

    fun deletePrimerVueloById(id: Int){
        primerVueloDiaDao.deletePrimerVueloById(id)
    }
}