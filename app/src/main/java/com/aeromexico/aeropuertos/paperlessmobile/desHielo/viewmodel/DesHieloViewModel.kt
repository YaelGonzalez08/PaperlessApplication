package com.aeromexico.aeropuertos.paperlessmobile.desHielo.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aeromexico.aeropuertos.paperlessmobile.PaperlessApplication
import com.aeromexico.aeropuertos.paperlessmobile.common.CORE.CoreDataSource
import com.aeromexico.aeropuertos.paperlessmobile.common.CORE.CoreRepository
import com.aeromexico.aeropuertos.paperlessmobile.common.database.interactors.DeshieloInteractor
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.RequestState
import com.aeromexico.aeropuertos.paperlessmobile.desHielo.pojos.DeshieloToServer
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.CORE_Base

class DesHieloViewModel(val repository: CoreRepository) :ViewModel() {
    lateinit var coreDataSource: CoreDataSource
    private val interactorDeshielo:DeshieloInteractor = DeshieloInteractor(PaperlessApplication.database.deshieloDao())
    lateinit var responseState: MutableLiveData<RequestState>

    init {
        coreDataSource = repository.getCoreDataSource()
        responseState = coreDataSource.responseStateCore
    }

    fun getCapitan(numEmpleado: String): MutableLiveData<CORE_Base> {
        coreDataSource = repository.getCoreDataSource()
        responseState = coreDataSource.responseStateCore
        coreDataSource.getEmpleado(numEmpleado)
        return coreDataSource.responseCore
    }

    fun getAplicador(numEmpleado: String): MutableLiveData<CORE_Base> {
        coreDataSource = repository.getCoreDataSource()
        responseState = coreDataSource.responseStateCore
        coreDataSource.getEmpleado(numEmpleado)
        return coreDataSource.responseCore
    }

    fun getOficialOperaciones(numEmpleado: String): MutableLiveData<CORE_Base> {
        coreDataSource = repository.getCoreDataSource()
        responseState = coreDataSource.responseStateCore
        coreDataSource.getEmpleado(numEmpleado)
        return coreDataSource.responseCore
    }

    fun send(){

    }

    fun saveBdDeshielo(toServer: DeshieloToServer): MutableLiveData<Long> {
       return interactorDeshielo.addDeshieloCheck(toServer)
    }
}