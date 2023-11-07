package com.aeromexico.aeropuertos.paperlessmobile.metar.Model

import androidx.lifecycle.MutableLiveData
import com.aeromexico.aeropuertos.paperlessmobile.common.CORE.CoreDataSource
import com.aeromexico.aeropuertos.paperlessmobile.common.CORE.CoreRepository
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.RequestState
import com.aeromexico.aeropuertos.paperlessmobile.metar.objects.MetarResponse
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.PDFB64FILEResponse
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.PdfImageResponse

class MetarViewModel(var repository:CoreRepository) {

    lateinit var coreDataSource: CoreDataSource
    lateinit var responseState: MutableLiveData<RequestState>
    lateinit var responseMetar : MutableLiveData<MetarResponse>

    init {
        coreDataSource = repository.getCoreDataSource()
        responseState = coreDataSource.responseStateCore
    }

    fun getMetar(guidVuelo:String) {
        responseMetar = coreDataSource.responseMetar
        return coreDataSource.getMetar(guidVuelo)
    }
}