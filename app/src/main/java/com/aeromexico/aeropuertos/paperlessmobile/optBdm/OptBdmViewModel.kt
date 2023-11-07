package com.aeromexico.aeropuertos.paperlessmobile.optBdm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aeromexico.aeropuertos.paperlessmobile.common.CORE.CoreDataSource
import com.aeromexico.aeropuertos.paperlessmobile.common.CORE.CoreRepository
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.RequestState
import com.aeromexico.aeropuertos.paperlessmobile.optBdm.entities.RequestOptBdmForm
import com.aeromexico.aeropuertos.paperlessmobile.optBdm.model.OptBdmDataSource
import com.aeromexico.aeropuertos.paperlessmobile.optBdm.repository.OptBdmRepository
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.CORE_Base
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.ResponseOptBdmBase
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.SendResponse_Base
import com.aeromexico.aeropuertos.paperlessmobile.webService.WebServiceApi

class OptBdmViewModel : ViewModel() {
    var coreRepository: CoreRepository = CoreRepository(WebServiceApi().getCoreApi())
    var coreDataSource: CoreDataSource = coreRepository.getCoreDataSource()

    var optBdmDataSource: OptBdmDataSource = OptBdmRepository(WebServiceApi().getOptBdmApi()).getAPI()
    lateinit var responseGetInfo: MutableLiveData<ResponseOptBdmBase>
    lateinit var responseSend: MutableLiveData<SendResponse_Base>
    var responseEstado: MutableLiveData<RequestState>? = optBdmDataSource.responseState

    fun getEmpleado(id:String): MutableLiveData<CORE_Base> {
        coreDataSource.getEmpleado(id)
        return coreDataSource.responseCore
    }

    fun getInfoOptBdm(flightReferenceNumber: Long){
        responseGetInfo = optBdmDataSource.responseOptBdm
        optBdmDataSource.getDatos(flightReferenceNumber)
    }

    fun sendDetalle(request: RequestOptBdmForm){
        responseSend = optBdmDataSource.responseSend
        optBdmDataSource.save(request)
    }


}