package com.aeromexico.aeropuertos.paperlessmobile.metar.Model

import androidx.lifecycle.MutableLiveData
import com.aeromexico.aeropuertos.paperlessmobile.common.CORE.CoreDataSource
import com.aeromexico.aeropuertos.paperlessmobile.common.CORE.CoreRepository
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.RequestState
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.PDFB64FILEResponse
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.PdfImageResponse

class PDFViewModel(var repository:CoreRepository) {

    lateinit var coreDataSource: CoreDataSource
    lateinit var responseState: MutableLiveData<RequestState>

    init {
        coreDataSource = repository.getCoreDataSource()
        responseState = coreDataSource.responseStateCore
    }

    fun getPDFFile(): MutableLiveData<PDFB64FILEResponse?> {
        return coreDataSource.getPDFileB64()
    }
}