package com.aeromexico.aeropuertos.paperlessmobile.notoc

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aeromexico.aeropuertos.paperlessmobile.common.CORE.CoreDataSource
import com.aeromexico.aeropuertos.paperlessmobile.common.CORE.CoreRepository
import com.aeromexico.aeropuertos.paperlessmobile.common.database.entities.ModificarDetalleLirEntity
import com.aeromexico.aeropuertos.paperlessmobile.common.database.entities.NotocEntity
import com.aeromexico.aeropuertos.paperlessmobile.common.database.interactors.NotocInteractor
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.RequestState
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronavePrimerVuelo.entities.RequestFirstFlightForm
import com.aeromexico.aeropuertos.paperlessmobile.mensajesOperacionales.model.MOInteractor
import com.aeromexico.aeropuertos.paperlessmobile.mensajesOperacionales.model.MensajesOperacionalesDataSource
import com.aeromexico.aeropuertos.paperlessmobile.notoc.entities.RequestNotoc
import com.aeromexico.aeropuertos.paperlessmobile.notoc.model.NotocDataSource
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.*
import com.aeromexico.aeropuertos.paperlessmobile.webService.WebServiceApi

class NotocViewModel() : ViewModel() {
    val repository: CoreRepository = CoreRepository(WebServiceApi().getCoreApi())
    lateinit var coreDataSource: CoreDataSource
    lateinit var responseState: MutableLiveData<RequestState>
    lateinit var responseInfoNotoc: MutableLiveData<ResponseNotocBase>
    lateinit var responseSendNotoc: MutableLiveData<ResponseNotocBase>
    lateinit var responseMODetalle: MutableLiveData<ResponseMOBaseDetalle>
    private val interactor: NotocInteractor

    private val notocRepository: NotocRepository = NotocRepository(WebServiceApi().getNotocApi())
    var notocDataSource: NotocDataSource = notocRepository.getDataSource()
    var responseStateGetInfo : MutableLiveData<RequestState> = notocDataSource.responseStateGetInfo
    var responseStateSendNotoc: MutableLiveData<RequestState> = notocDataSource.responseStateSendNotoc

    //objeto para enviar
    lateinit var notoc: RequestNotoc
    val notocLiveData = MutableLiveData<RequestNotoc>()
    val esEditable = MutableLiveData<Boolean>()

    //auxiliar
    val posicionArregloInfoMercancia = MutableLiveData<Int>()
    val posicionArregloOtraCArga = MutableLiveData<Int>()

    private var firmasList: MutableList<NotocEntity>

    init {
        coreDataSource = repository.getCoreDataSource()
        responseState = coreDataSource.responseStateCore
        interactor= NotocInteractor()
        notoc= RequestNotoc(flightReferenceNumber = 0)
        firmasList = mutableListOf()
    }

    fun setEditable(esEditable: Boolean){
        this.esEditable.value = esEditable
    }

    fun setNotocEntity(notoc: RequestNotoc){
        notocLiveData.value=notoc
    }
    fun setPosicionArregloInfoMercancia(id: Int){
        posicionArregloInfoMercancia.value = id
    }
    fun setPosicionArregloOtraCarga(id: Int){
        posicionArregloOtraCArga.value = id
    }

    private val firmas: MutableLiveData<MutableList<NotocEntity>> by lazy {
        MutableLiveData<MutableList<NotocEntity>>().also {
            loadFirmas()
        }
    }

    private fun loadFirmas(){
        interactor.readAll {
            firmas.value = it
            firmasList = it
        }
    }

    fun getCapitan(numEmpleado: String): MutableLiveData<CORE_Base> {
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

    fun getInfoNotoc(flightReferenceNumber: Long){
        responseInfoNotoc = notocDataSource.responseInfoNotoc
        notocDataSource.getInfoNotoc(flightReferenceNumber)
    }
    fun sendInfoNotoc(notoc: RequestNotoc){
        responseSendNotoc = notocDataSource.responseSendNotoc
        notocDataSource.sendInfoNotoc(notoc)
    }

    fun getInfoLocalDB(): LiveData<MutableList<NotocEntity>> {
        return firmas
    }

    fun deleteNotocById(notoc: NotocEntity){
        interactor.deleteNotocById(notoc.id){
            val index = firmasList.indexOf(notoc)
            if (index!= -1){
                firmasList.removeAt(index)
                firmas.value = firmasList
            }
        }
    }
    fun addNotocToDBLocal(requestFirstFlightForm: RequestNotoc): MutableLiveData<Long>{
        return interactor.addForm(requestFirstFlightForm)
    }




}