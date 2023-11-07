package com.aeromexico.aeropuertos.paperlessmobile.manifiestoCarga

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aeromexico.aeropuertos.paperlessmobile.common.CORE.CoreRepository
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.RequestState
import com.aeromexico.aeropuertos.paperlessmobile.manifiestoCarga.model.ManifiestoCargaDataSource
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.ResponseManifiestoCargaBase
import com.aeromexico.aeropuertos.paperlessmobile.webService.WebServiceApi

class ManifiestoCargaViewModel : ViewModel() {
    private val repository: ManifiestoCargaRespository = ManifiestoCargaRespository(WebServiceApi().getManifiestoCargaAPI())
    private var datasource: ManifiestoCargaDataSource = repository.getDataSource()
    var responseState: MutableLiveData<RequestState> = datasource.responseState
    lateinit var responseMC : MutableLiveData<ResponseManifiestoCargaBase>

    fun getManifiestoCarga(flightReferenceNumber: Long){
        responseMC = datasource.responseMC
        datasource.getManifiestoCarga(flightReferenceNumber)
    }
}