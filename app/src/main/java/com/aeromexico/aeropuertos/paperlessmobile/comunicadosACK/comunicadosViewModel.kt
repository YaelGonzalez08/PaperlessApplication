package com.aeromexico.aeropuertos.paperlessmobile.comunicadosACK

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aeromexico.aeropuertos.paperlessmobile.common.CORE.CoreRepository
import com.aeromexico.aeropuertos.paperlessmobile.comunicadosACK.datasource.comunicadosDataSource
import com.aeromexico.aeropuertos.paperlessmobile.comunicadosACK.pojos.ComunicadoPdfResponse
import com.aeromexico.aeropuertos.paperlessmobile.comunicadosACK.pojos.ComunicadosResponse
import com.aeromexico.aeropuertos.paperlessmobile.comunicadosACK.pojos.RequestEnvioCuesitonario
import com.aeromexico.aeropuertos.paperlessmobile.comunicadosACK.pojos.ResponseEnvioCuestionario
import com.aeromexico.aeropuertos.paperlessmobile.comunicadosACK.repository.ComunicadosRepository
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.CORE_Base
import getFileResponse

class comunicadosViewModel(
    var repository: ComunicadosRepository,
    val coreRepository: CoreRepository
) : ViewModel() {
    private lateinit var data: comunicadosDataSource
    var isLoading: MutableLiveData<Boolean> =
        MutableLiveData<Boolean>()
    init {
        data = repository.getComunicadosDatasource()
        isLoading = data.isLoading
    }

    fun getComunicados(): MutableLiveData<ComunicadosResponse> {
        data.getComunicados()
        return data.responseComunicados

    }

    fun getComunicadoById(comunicadoId: Int): MutableLiveData<ComunicadoPdfResponse?> {
        return data.getComunicadoById(comunicadoId)
    }
    fun getDownloadDocumentById(idArchivo: Int): MutableLiveData<getFileResponse?> {
        return data.getDownloadDocumentById(idArchivo)
    }

    fun sendComunicadoResuelto(requestEnvioCuesitonario: RequestEnvioCuesitonario): MutableLiveData<ResponseEnvioCuestionario?> {
        return data.sendComunicadoResuelto(requestEnvioCuesitonario)
    }

    fun getEmpleado(numEmpleado: String): MutableLiveData<CORE_Base> {
        var datacore = coreRepository.getCoreDataSource()
        datacore.getEmpleado(numEmpleado)
        return datacore.responseCore
    }

}