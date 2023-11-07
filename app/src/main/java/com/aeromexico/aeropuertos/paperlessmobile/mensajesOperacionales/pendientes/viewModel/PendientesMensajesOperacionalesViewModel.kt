package com.aeromexico.aeropuertos.paperlessmobile.mensajesOperacionales.pendientes.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aeromexico.aeropuertos.paperlessmobile.common.database.entities.ModificarDetalleLirEntity
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.RequestState
import com.aeromexico.aeropuertos.paperlessmobile.mensajesOperacionales.MensajeOperacionalesRepository
import com.aeromexico.aeropuertos.paperlessmobile.mensajesOperacionales.model.MOInteractor
import com.aeromexico.aeropuertos.paperlessmobile.mensajesOperacionales.model.MensajesOperacionalesDataSource
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.ResponseMOBaseDetalle

class PendientesMensajesOperacionalesViewModel(private val repository: MensajeOperacionalesRepository?) : ViewModel() {
    var msjOpDataSource: MensajesOperacionalesDataSource? = repository?.getAPI()
    var responseState: MutableLiveData<RequestState>? = msjOpDataSource?.responseState
    lateinit var responseMODetalle: MutableLiveData<ResponseMOBaseDetalle>
    var responseStateEnvioFirma: MutableLiveData<RequestState>? = msjOpDataSource?.responseStateEnvioFirma
    private val interactor: MOInteractor
    private val result = MutableLiveData<Any>()
    private val cambioFirmas= MutableLiveData<Boolean>()
    private var firmasList: MutableList<ModificarDetalleLirEntity>

    init {
        interactor= MOInteractor()
        firmasList = mutableListOf()
    }

    private val firmas: MutableLiveData<MutableList<ModificarDetalleLirEntity>> by lazy {
        MutableLiveData<MutableList<ModificarDetalleLirEntity>>().also {
            loadFirmas()
        }
    }
    private fun loadFirmas(){
        interactor.getAllFirmasPendientes {
            firmas.value = it
            firmasList = it
        }
    }
    fun sendDetalle(detalleLirEntity: ModificarDetalleLirEntity){
        responseMODetalle = msjOpDataSource!!.responseMODetalle
        msjOpDataSource?.saveDetalleLir(detalleLirEntity)
    }
    fun deleteDetalleFirmasRoom(pendiente: ModificarDetalleLirEntity){
        interactor.deleteFirmaById(pendiente.id,{
            val index = firmasList.indexOf(pendiente)
            if (index!= -1){
                firmasList.removeAt(index)
                firmas.value = firmasList
            }
//            cambioFirmas.value = true
            if (it!=0) result.value = true
            else result.value = false
        })
    }
    fun getResult(): LiveData<Any> {
        return result
    }
    fun getFirmas(): LiveData<MutableList<ModificarDetalleLirEntity>>{
        return firmas
    }
}