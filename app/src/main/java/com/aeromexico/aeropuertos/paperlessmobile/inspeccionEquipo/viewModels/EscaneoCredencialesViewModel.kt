package com.aeromexico.aeropuertos.paperlessmobile.inspeccionEquipo.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aeromexico.aeropuertos.paperlessmobile.common.CORE.CoreDataSource
import com.aeromexico.aeropuertos.paperlessmobile.common.CORE.CoreRepository
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.RequestState
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.CORE_Base
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.EquipoResponseObjects

class EscaneoCredencialesViewModel(val repository: CoreRepository) : ViewModel() {
    lateinit var coreDataSource: CoreDataSource
    lateinit var responseState: MutableLiveData<RequestState>

    init {
        coreDataSource = repository.getCoreDataSource()
        responseState = coreDataSource.responseStateCore
    }

    fun getOperador(numEmpleado: String): MutableLiveData<CORE_Base> {
        coreDataSource = repository.getCoreDataSource()
        responseState = coreDataSource.responseStateCore
        coreDataSource.getEmpleado(numEmpleado)
        return coreDataSource.responseCore
    }
    fun getSupervisor(numEmpleado: String): MutableLiveData<CORE_Base> {
        coreDataSource = repository.getCoreDataSource()
        responseState = coreDataSource.responseStateCore
        coreDataSource.getEmpleado(numEmpleado)
        return coreDataSource.responseCore
    }

    fun getEquipo(idEquipo: String): MutableLiveData<EquipoResponseObjects> {
        coreDataSource = repository.getCoreDataSource()
        responseState = coreDataSource.responseStateCore
        coreDataSource.getEquipo(idEquipo)
        return coreDataSource.responseEquipo
    }


}
