package com.aeromexico.aeropuertos.paperlessmobile.controlAbordaje.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aeromexico.aeropuertos.paperlessmobile.common.CORE.CoreDataSource
import com.aeromexico.aeropuertos.paperlessmobile.common.CORE.CoreRepository
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.RequestState
import com.aeromexico.aeropuertos.paperlessmobile.controlAbordaje.datasource.controlAbordajeDataSource
import com.aeromexico.aeropuertos.paperlessmobile.controlAbordaje.pojos.*
import com.aeromexico.aeropuertos.paperlessmobile.controlAbordaje.repository.controlAbordajeRepository
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.CORE_Base
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ControlAbordajeViewModel(
    val repository: CoreRepository,
    val controlRepository: controlAbordajeRepository
) : ViewModel() {

    lateinit var coreDataSource: CoreDataSource
    lateinit var responseState: MutableLiveData<RequestState>
    lateinit var controlAbordajeDataSource: controlAbordajeDataSource

    init {
        coreDataSource = repository.getCoreDataSource()
        responseState = coreDataSource.responseStateCore
    }

    fun getEmpleadoAM(numEmpleado: String): MutableLiveData<CORE_Base> {
        coreDataSource = repository.getCoreDataSource()
        responseState = coreDataSource.responseStateCore
        coreDataSource.getEmpleado(numEmpleado)
        return coreDataSource.responseCore
    }

    fun getNotBoardedPasajeros(fligthReference: String): MutableLiveData<PasajerosReponse> {
        controlAbordajeDataSource = controlRepository.getControlDatasource()
        controlAbordajeDataSource.requestNotBoardedPasajeros(fligthReference)
        return controlAbordajeDataSource.responseControl
    }
    fun getBoardedPasajeros(fligthReference: String): MutableLiveData<PasajerosReponse> {

        controlAbordajeDataSource = controlRepository.getControlDatasource()
        controlAbordajeDataSource.getBoardedPasajeros(fligthReference)
        return controlAbordajeDataSource.responseControl
    }

    fun setChecinPax(pax: PasajerosQuitarCheckInRequest): MutableLiveData<PasajerosReponse> {
        controlAbordajeDataSource = controlRepository.getControlDatasource()
        controlAbordajeDataSource.setChecinPax(pax)
        return controlAbordajeDataSource.responseCheckIn
    }
    fun getEmployeeInfo(fligthReference: String): MutableLiveData<GetEmployeeInfoResponse> {
        controlAbordajeDataSource = controlRepository.getControlDatasource()
        controlAbordajeDataSource.getEmployeeInfo(fligthReference)
        return controlAbordajeDataSource.responseEmpleadoInfo
    }

    fun InsertInfoOficial(datos: InsertInfoOficial): MutableLiveData<resultInsertInfoOficial> {
        controlAbordajeDataSource = controlRepository.getControlDatasource()
        controlAbordajeDataSource.InsertInfoOficial(datos)
        return controlAbordajeDataSource.resultInsertInfoOficial

    }


}