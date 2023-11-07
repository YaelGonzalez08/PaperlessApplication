package com.aeromexico.aeropuertos.paperlessmobile.mensajesOperacionales.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aeromexico.aeropuertos.paperlessmobile.common.CORE.CoreDataSource
import com.aeromexico.aeropuertos.paperlessmobile.common.CORE.CoreRepository
import com.aeromexico.aeropuertos.paperlessmobile.common.database.entities.ModificarDetalleLirEntity
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.RequestState
import com.aeromexico.aeropuertos.paperlessmobile.mensajesOperacionales.MensajeOperacionalesRepository
import com.aeromexico.aeropuertos.paperlessmobile.mensajesOperacionales.model.MOInteractor
import com.aeromexico.aeropuertos.paperlessmobile.mensajesOperacionales.model.MensajesOperacionalesDataSource
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.CORE_Base
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.ResponseMOBase
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.ResponseMOBaseDetalle
import com.aeromexico.aeropuertos.paperlessmobile.webService.WebServiceApi

class MensajesOperacionalesViewModel(private val repository: MensajeOperacionalesRepository?) : ViewModel() {

    lateinit var responseMO: MutableLiveData<ResponseMOBase>
    lateinit var responseMODetalle: MutableLiveData<ResponseMOBaseDetalle>
    private val interactor: MOInteractor
    var msjOpDataSource: MensajesOperacionalesDataSource? = repository?.getAPI()
    //response state
    var responseState: MutableLiveData<RequestState>? = msjOpDataSource?.responseState
    var responseStateEnvioFirma: MutableLiveData<RequestState>? = msjOpDataSource?.responseStateEnvioFirma

    //Core
    lateinit var responseCore: MutableLiveData<CORE_Base>
    private val repositoryCore: CoreRepository = CoreRepository(WebServiceApi().getCoreApi())
    var coreDatasource: CoreDataSource = repositoryCore.getCoreDataSource()
    var responseStateCore: MutableLiveData<RequestState> = coreDatasource.responseStateCore

    //RequestGuardarFirmasLir
    var detalleLirEntity: ModificarDetalleLirEntity = ModificarDetalleLirEntity()


    //Firmas
    val b64OficialRampa = MutableLiveData<String>();
    val b64OficialDespacho = MutableLiveData<String>();

    val textoMensajeSeleccionado = MutableLiveData<String>()
    val isLir = MutableLiveData<Boolean>()
    private val result = MutableLiveData<Any>()
    private val cambioFirmas= MutableLiveData<Boolean>()

    init {
        interactor= MOInteractor()
    }


    fun setTextoSeleccionado(texto: String){
        textoMensajeSeleccionado.value = texto
    }

    fun getTextoSeleccionado(): LiveData<String> {
        return textoMensajeSeleccionado
    }
    fun setIsLir(isLir: Boolean){
        this.isLir.value = isLir
    }
    fun getIsLir(): LiveData<Boolean> {
        return isLir
    }

    fun setBase64OfRampa(base64: String){
        this.b64OficialRampa.value = base64;
    }
    fun getBase64OfRampa(): LiveData<String> {
        return b64OficialRampa;
    }

    fun setBase64Despacho(base64: String){
        this.b64OficialDespacho.value = base64;
    }
    fun getBase64Despacho(): LiveData<String> {
        return b64OficialDespacho;
    }


    fun getMensajes(flightReferenceNumber: Long){
        responseMO = msjOpDataSource!!.responseMO
        msjOpDataSource?.getMensajesOps(flightReferenceNumber)
    }

    fun sendDetalle(){
        responseMODetalle = msjOpDataSource!!.responseMODetalle
        msjOpDataSource?.saveDetalleLir(detalleLirEntity)
    }

    fun getEmpleado(noEmpleado: String){
        responseCore = coreDatasource.responseCore
        coreDatasource.getEmpleado(noEmpleado)
    }

    fun saveDetalleFirmasRoom(){
        interactor.saveFirmasLir(detalleLirEntity,{newId ->
            result.value = newId
            cambioFirmas.value = true
        })
    }

    fun deleteDetalleFirmasRoom(id: Long){
        interactor.deleteFirmaById(id,{
            cambioFirmas.value = true
            if (it!=0) result.value = true else false
        })
    }

    fun getDetalleById(id:Long){
        interactor.getFirmasById(id,{firmas ->
            result.value = firmas
        })
    }

    fun setResult(value: Any){
        result.value = value
    }
    fun getResult(): LiveData<Any>{
        return result
    }

    fun getCambioFirmas(): LiveData<Boolean>{
        return cambioFirmas
    }

    private val cuenta: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>().also {
            loadFirmasFaltantes()
        }
    }
    fun loadFirmasFaltantes() {

        interactor.getAllFirmasPendientes {
            //firmas.value= it
            cuenta.value = it.size
        }
    }
    fun getFirmas(): LiveData<Int>{
        return cuenta
    }

}