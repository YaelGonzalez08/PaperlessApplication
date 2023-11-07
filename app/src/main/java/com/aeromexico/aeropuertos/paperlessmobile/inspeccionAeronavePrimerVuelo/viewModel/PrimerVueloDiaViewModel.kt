package com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronavePrimerVuelo.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aeromexico.aeropuertos.paperlessmobile.PaperlessApplication
import com.aeromexico.aeropuertos.paperlessmobile.common.CORE.CoreDataSource
import com.aeromexico.aeropuertos.paperlessmobile.common.CORE.CoreRepository
import com.aeromexico.aeropuertos.paperlessmobile.common.database.entities.CheckPrimeVueloEntity
import com.aeromexico.aeropuertos.paperlessmobile.common.database.interactors.PrimerVueloDiaInteractor
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.RequestState
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronavePrimerVuelo.dataSource.PrimerVueloDataSource
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronavePrimerVuelo.entities.RequestFirstFlightForm
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronavePrimerVuelo.repository.PrimerVueloRepository
import com.aeromexico.aeropuertos.paperlessmobile.webService.IAPI.PrimerVueloDiaApi
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.CORE_Base
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.PrimerVueloResponse
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.PrimerVueloResponseGET
import com.aeromexico.aeropuertos.paperlessmobile.webService.WebServiceApi

class PrimerVueloDiaViewModel(): ViewModel() {

    var coreRepository: CoreRepository = CoreRepository(WebServiceApi().getCoreApi())
    var coreDataSource: CoreDataSource = coreRepository.getCoreDataSource()

    val primerVueloDiaInteractor: PrimerVueloDiaInteractor = PrimerVueloDiaInteractor(PaperlessApplication.database.primerVueloDia())
    var primerVueloDataSource: PrimerVueloDataSource = PrimerVueloRepository(WebServiceApi().getPrimerVueloDiaApi()).requestPrimerVueloDataSource()

    var responseState:MutableLiveData<RequestState> = primerVueloDataSource.responseState

    fun insertPrimerVuelo(requestFirstFlightForm: RequestFirstFlightForm): MutableLiveData<PrimerVueloResponse>{
        primerVueloDataSource.insertPrimerVuelo(requestFirstFlightForm)
        return primerVueloDataSource.responsePrimerVuelo
    }

    fun getEmpleado(id:String): MutableLiveData<CORE_Base> {
        coreDataSource.getEmpleado(id)
        return coreDataSource.responseCore
    }

    fun addFormToDB(requestFirstFlightForm: RequestFirstFlightForm): MutableLiveData<Long>{
        return primerVueloDiaInteractor.addForm(requestFirstFlightForm)
    }

    fun getAllForms(): LiveData<List<CheckPrimeVueloEntity>> {
        return primerVueloDiaInteractor.allForms
    }

    fun updateForm(requestFirstFlightForm: CheckPrimeVueloEntity): MutableLiveData<Int> {
        return primerVueloDiaInteractor.updateForm(requestFirstFlightForm)
    }

    fun deletePtimerVueloCheckById(id:Int){
        primerVueloDiaInteractor.deletePrimerVueloById(id)
    }

    fun getInfoDB(frn: Long): MutableLiveData<PrimerVueloResponseGET>{
        primerVueloDataSource.getInfoPrimervuelo(frn)
        return primerVueloDataSource.responseGET
    }
}