package com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronavePrimerVuelo.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.RequestState
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronavePrimerVuelo.dataSource.IATADataSource
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronavePrimerVuelo.dataSource.VueloManualDataSource
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronavePrimerVuelo.repository.IATARepository
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronavePrimerVuelo.repository.VueloManulaRepository
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.IATASResponse
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.VueloManualResponse
import com.aeromexico.aeropuertos.paperlessmobile.webService.WebServiceApi

class InformacionManualVueloViewModel(): ViewModel() {

    lateinit var iataRepository:IATARepository
    lateinit var responseIatas : MutableLiveData<IATASResponse>
    lateinit var iataDataSource: IATADataSource
    lateinit var responseState: MutableLiveData<RequestState>

    lateinit var vueloManualRepository: VueloManulaRepository
    lateinit var vueloManualDataSource: VueloManualDataSource
    lateinit var responseStateVueloManual: MutableLiveData<RequestState>
    init {
        iataRepository = IATARepository(WebServiceApi().getIATAApi())
        iataDataSource = iataRepository.requestIatasDataSource()
        responseState =  iataDataSource.responseState
        vueloManualRepository = VueloManulaRepository(WebServiceApi().getVuelosApi())
        vueloManualDataSource = vueloManualRepository.requestVueloManualDataSource()
        responseStateVueloManual = vueloManualDataSource.responseState
    }

    fun obtenerListaIatas() {
        responseIatas = iataDataSource.responseIATA
        iataDataSource.requestIATAS()
    }

    fun insertVueloManual(
        numVuelo: String,
        origen: String,
        destino: String,
        aerolinea: String,
        matricula: String,
        eta: String,
        etd: String,
        fechaVuelo: String,
        equipo: String): MutableLiveData<VueloManualResponse> {
        vueloManualDataSource.insertarVueloManual(numVuelo, origen, destino, aerolinea, matricula, eta, etd, fechaVuelo, equipo)
        return vueloManualDataSource.responseVueloManual
    }
}