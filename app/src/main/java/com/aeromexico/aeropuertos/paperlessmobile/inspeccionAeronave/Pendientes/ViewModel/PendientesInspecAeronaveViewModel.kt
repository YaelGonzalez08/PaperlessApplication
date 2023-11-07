package com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronave.Pendientes.ViewModel

import android.util.Log
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aeromexico.aeropuertos.paperlessmobile.common.database.entities.InspeccionAeronaveEntity
import com.aeromexico.aeropuertos.paperlessmobile.common.database.entities.ModificarDetalleLirEntity
import com.aeromexico.aeropuertos.paperlessmobile.common.database.interactors.AveriaInspecAeronaveInteractor
import com.aeromexico.aeropuertos.paperlessmobile.common.database.interactors.ImagenInspecAeronaveInteractor
import com.aeromexico.aeropuertos.paperlessmobile.common.database.interactors.InspecAeronaveInteractor
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.Constants
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.RequestState
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronave.ConsentimientoForm.viewModel.ConsentimientoFormViewModel
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronave.DataSource.InspeccionAeronaveDataSource
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronave.InspeccionAeronaveRepository
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.CreateInspeccionResponsaBase
import com.aeromexico.aeropuertos.paperlessmobile.webService.WebServiceApi


class PendientesInspecAeronaveViewModel: ViewModel() {

    //Lista de solicitudes pendientes
    var inspeccionesPendientes = MutableLiveData<MutableList<InspeccionAeronaveEntity>>();

    //Interactor de BD
    private val inspeccionIinteractor: InspecAeronaveInteractor
    private val averiaInteractor: AveriaInspecAeronaveInteractor
    private val imagenInteractor: ImagenInspecAeronaveInteractor

    //Conexión con el api
    private val inspecAeronaveRepository = InspeccionAeronaveRepository(WebServiceApi().getInspecAeronaveApi())
    var dataSource: InspeccionAeronaveDataSource? = inspecAeronaveRepository?.getAPI()
    var responseState: MutableLiveData<RequestState>? = dataSource?.responseStateCreateInspeccion

    //Request
    var request: MutableLiveData<InspeccionAeronaveEntity> = MutableLiveData<InspeccionAeronaveEntity>(null)
    var readyToSend: MutableLiveData<Boolean> = MutableLiveData(false)

    //Response
    lateinit var createInspecBaseResponse: MutableLiveData<CreateInspeccionResponsaBase>
    var idInsertado: MutableLiveData<Long> = MutableLiveData(0)

    private val inspeccionEliminada = MutableLiveData<Boolean>(false)


    init {
        inspeccionIinteractor= InspecAeronaveInteractor()
        averiaInteractor= AveriaInspecAeronaveInteractor()
        imagenInteractor= ImagenInspecAeronaveInteractor()
    }

    fun getBaseResponse(): LiveData<CreateInspeccionResponsaBase>{
        return createInspecBaseResponse
    }


    fun getReadyToSend(): LiveData<Boolean>{
        return this.readyToSend
    }

    fun setReadyToSend(readyToSend: Boolean){
        this.readyToSend.value = readyToSend
    }


    fun getIdInsertado(): LiveData<Long>{
        return this.idInsertado
    }

    fun setIdInsertado(idInsertado: Long){
        this.idInsertado.value = idInsertado
    }

    fun getInspeccionEliminada(): LiveData<Boolean>{
        return this.inspeccionEliminada
    }


    fun getInspeccionesPendientes(): LiveData<MutableList<InspeccionAeronaveEntity>> {
        return this.inspeccionesPendientes
    }
    

    fun getAllInspecAeronave() {
        inspeccionIinteractor.getAllInspecAeronave {
            var a = it;
            inspeccionesPendientes.value = it
        }
    }


    fun deleteInspeccionAeronave(pendiente: InspeccionAeronaveEntity){
        inspeccionIinteractor.deleteInspecAeronave(pendiente) {
            val index = inspeccionesPendientes.value!!.indexOf(pendiente)
            if (index != -1) {

                deleteImagenesEnInspeccion(pendiente.id)

                inspeccionesPendientes.value!!.removeAt(index)
                inspeccionesPendientes.value = inspeccionesPendientes.value
            }

        }
    }

    fun deleteImagenesEnInspeccion(idInspeccion: Long){
        //Obtener averias
        averiaInteractor.getAveriasEnInspeccion(idInspeccion){
            var averiaOtros = it.last()

            imagenInteractor.deleteImagenesEnAveria(averiaOtros?.id){
                var a = it
                deleteAveriasEnInspeccion(idInspeccion)
            }


        }

    }


    fun deleteAveriasEnInspeccion(idInspeccion: Long){
        averiaInteractor.deleteAveriasEnInspeccion(idInspeccion) {
            var a ="listo";
            inspeccionEliminada.value = true
        }
    }


    fun getAveriasEnInspeccion(inspeccion: InspeccionAeronaveEntity){
        Log.i("PEND_INSPEC_AERONAVE", "Obteniendo lista de averías para la inspección seleccionada, con el id LOCAL de: " + inspeccion.id.toString())
        //Obtener averías de esta inspección
        averiaInteractor.getAveriasEnInspeccion(inspeccion.id){
            inspeccion.Averias = it
            request.value = inspeccion
            Log.i("PEND_INSPEC_AERONAVE", "Se obtuvieron exitosamente de la bd local un total de " + it.size + " averias")

            getImagenesEnAverias()
        }

    }

    fun getImagenesEnAverias(){

       request.value?.Averias?.forEach{ averia ->
            var idAveria = averia.id

           if(averia.CodigoAveria == Constants.TiposAveria.otro.name){
               imagenInteractor.getImagenesEnAveria(idAveria){
                   var a = it
                   if(it.size > 0){
                       averia.Imagenes = mutableListOf()
                       it.forEach{ imagen ->
                           averia.Imagenes?.add(imagen.imagen)
                       }
                   }

                   setReadyToSend(true)
               }
           }

       }


    }


    //Envío al api
    fun sendInspeccionAeronavePendiente(){
        createInspecBaseResponse = dataSource!!.responseCreateInspeccion
        dataSource?.createInspeccionAeronave(request.value!!)
    }

    //Reset
    fun resetRequest(){
        request= MutableLiveData<InspeccionAeronaveEntity>(null)
    }

    fun resetReadyToSend(){
        setReadyToSend(false)
    }

    fun resetIdInserted(){
        setIdInsertado(0)
    }


}