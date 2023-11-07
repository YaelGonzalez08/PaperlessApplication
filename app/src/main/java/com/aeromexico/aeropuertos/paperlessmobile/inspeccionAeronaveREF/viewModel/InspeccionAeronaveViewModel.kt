package com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronaveREF.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.aeromexico.aeropuertos.paperlessmobile.common.CORE.CoreRepository
import com.aeromexico.aeropuertos.paperlessmobile.common.database.entities.ImagenInspecAeronaveEntity
import com.aeromexico.aeropuertos.paperlessmobile.common.database.entities.InspeccionAeronaveEntity
import com.aeromexico.aeropuertos.paperlessmobile.common.database.interactors.AveriaInspecAeronaveInteractor
import com.aeromexico.aeropuertos.paperlessmobile.common.database.interactors.ImagenInspecAeronaveInteractor
import com.aeromexico.aeropuertos.paperlessmobile.common.database.interactors.InspecAeronaveInteractor
import com.aeromexico.aeropuertos.paperlessmobile.common.database.interactors.InspeccionAeronavesInteractor
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.Constants
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.RequestState
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronave.DataSource.InspeccionAeronaveDataSource
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronave.InspeccionAeronaveRepository
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronaveREF.pojos.InspeccionAeronave
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronaveREF.pojos.ResponseExisteInspeccion
import com.aeromexico.aeropuertos.paperlessmobile.mensajesOperacionales.MensajeOperacionalesRepository
import com.aeromexico.aeropuertos.paperlessmobile.mensajesOperacionales.model.MensajesOperacionalesDataSource
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.CORE_Base
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.CreateInspeccionResponsaBase
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.GetTiposAveriaResponsaBase
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.ResponseMOBaseDetalle
import com.aeromexico.aeropuertos.paperlessmobile.webService.WebServiceApi


class InspeccionAeronaveViewModel(): ViewModel() {

    //Datos del vuelo actual
    private val esWideBody = MutableLiveData<Boolean>(false)
    var noVuelo = MutableLiveData<String>()
    var matricula = MutableLiveData<String>("")
    var equipo = MutableLiveData<String>("")
    var fechaVuelo = MutableLiveData<String>("")
    var origen = MutableLiveData<String>("")
    var destino = MutableLiveData<String>("")
    var guidVuelo = MutableLiveData<String>("")


    //Request
    var request: MutableLiveData<InspeccionAeronaveEntity> = MutableLiveData<InspeccionAeronaveEntity>(InspeccionAeronaveEntity())

    //Bandera para determinar si se ha finalizado el proceso y folio de reporte creado
    var hasFinished =  MutableLiveData<Boolean>(false)
    var idInspeccionCreada =  MutableLiveData<Long>(0)


    //Interactor de BD
    private val inspeccionIinteractor: InspecAeronaveInteractor
    private val averiaInteractor: AveriaInspecAeronaveInteractor
    private val imagenInteractor: ImagenInspecAeronaveInteractor
    private val guardadoLocal = MutableLiveData<Boolean>(false)

    //Conexión con el api
    private val inspecAeronaveRepository = InspeccionAeronaveRepository(WebServiceApi().getInspecAeronaveApi())

    //SI TRUENA ALGO, REGRESAR A ESTO: Y BORRAR METODO CREATEDATASOURCE
    //var dataSource: InspeccionAeronaveDataSource? = inspecAeronaveRepository?.getAPI()
    //var responseState: MutableLiveData<RequestState>? = dataSource?.responseStateCreateInspeccion

    var dataSource: InspeccionAeronaveDataSource? = null
    var responseState: MutableLiveData<RequestState>? = null
    var responseStateTiposAveria: MutableLiveData<RequestState>? = null


    //Responses
    lateinit var createInspecBaseResponse: MutableLiveData<CreateInspeccionResponsaBase>
    var getTiposAveriaBaseResponse= MutableLiveData<GetTiposAveriaResponsaBase>()


    //Listado de solicitudes pendientes
    var solicitudesPendientes = MutableLiveData<MutableList<InspeccionAeronaveEntity>?>(null)
    var coreRepository: CoreRepository
    //
    val interactorInspeccionAeronave:InspeccionAeronavesInteractor
    var responseError = MutableLiveData<String?>()

    init {
        inspeccionIinteractor= InspecAeronaveInteractor()
        averiaInteractor= AveriaInspecAeronaveInteractor()
        imagenInteractor = ImagenInspecAeronaveInteractor()
        coreRepository = CoreRepository(WebServiceApi().getCoreApi())

        createDataSource()
        interactorInspeccionAeronave = InspeccionAeronavesInteractor()
    }


    //Setters
    fun setNoVuelo(noVuelo: String){
        this.noVuelo.value = noVuelo
    }

    fun setMatricula(matricula: String){
        this.matricula.value = matricula
    }
    fun setEquipo(equipo: String){
        this.equipo.value = equipo
    }

    fun setFechaVuelo(fechaVuelo: String){
        this.fechaVuelo.value = fechaVuelo
    }

    fun setOrigen(origen: String){
        this.origen.value = origen
    }

    fun setDestino(destino: String){
        this.destino.value = destino
    }

    fun setGuidVuelo(guidVuelo: String){
        this.guidVuelo.value = guidVuelo
    }
    fun setIdInspeccionCreada(idInspeccionCreada: Long){
        this.idInspeccionCreada.value = idInspeccionCreada
    }
    fun setEsWideBody(esWideBody: Boolean){
        this.esWideBody.value = esWideBody
    }




    //Getters
    fun getNoVuelo(): LiveData<String> {
        return this.noVuelo
    }

    fun getMatricula(): LiveData<String> {
        return this.matricula
    }
    fun getEquipo(): LiveData<String> {
        return this.equipo
    }

    fun getFechaVuelo():LiveData<String> {
        return this.fechaVuelo
    }

    fun getOrigen(): LiveData<String> {
        return this.origen
    }

    fun getDestino(): LiveData<String> {
        return this.destino
    }

    fun getGuidVuelo():LiveData<String> {
        return this.guidVuelo
    }

    fun getResponse(): MutableLiveData<CreateInspeccionResponsaBase> {
        return this.createInspecBaseResponse
    }

    fun getIdInspeccionCreada(): LiveData<Long>{
        return this.idInspeccionCreada
    }
    fun getGuardadoLocal(): LiveData<Boolean>{
        return this.guardadoLocal
    }
    fun getEsWideBody(): MutableLiveData<Boolean>{
        return esWideBody
    }



    //Data Source
    fun createDataSource(){
        dataSource = inspecAeronaveRepository.getAPI()
        responseState = dataSource?.responseStateCreateInspeccion
        responseError = dataSource!!.responseError
    }

    //Obtener tipos de avería (para elementos mandatorios)
    fun createInsieccionAeronave(){
        createInspecBaseResponse = dataSource!!.responseCreateInspeccion
        dataSource?.createInspeccionAeronave(this.request.value!!)
    }

    //Envío al api
    fun  getTiposAveria(esSalida:Boolean,esLlegada:Boolean): MutableLiveData<GetTiposAveriaResponsaBase>? {
       // getTiposAveriaBaseResponse = dataSource!!.responseGetTiposAveria
       return dataSource?.getTiposAveria(esSalida,esLlegada)
    }

    fun GetInspeccion(guid: String, isLlegada: Boolean): MutableLiveData<ResponseExisteInspeccion?> {
        return dataSource!!.GetInspeccion(guid,isLlegada)
    }





    //Guardar / borrar y lectura de solicitudes pendientes en bd local
    fun addInspecAeronave(){
        Log.i("INSPEC_AERONAVE", "Agregando inspección pendiente a la base de datos local")
        inspeccionIinteractor.addInspecAeronave((this.request.value!!)) { insetredId ->
            var a = insetredId
            Log.i("INSPEC_AERONAVE", "Se agregó correctamente la inspección con el ID LOCAL de: " + insetredId.toString())

            addAverias(insetredId)
        }
    }

    fun addAverias(idInspeccion: Long){
        Log.i("INSPEC_AERONAVE", "Agregando averías de la inspección pendiente a la base de datos local")
        //Asignar id de su avería correspondiente
        this.request.value!!.Averias.forEach{
         it.idInspeccionAeronave = idInspeccion
        }

        //Insertar lista de averías
        averiaInteractor.addAveriaInspecAeronave(this.request.value!!.Averias) { insetredIds ->
            Log.i("INSPEC_AERONAVE", "Se han guardado exitosamente un total de " + request.value!!.Averias.size + " registros")

            var idOtros = insetredIds.last()
            addImagenesOtros(idOtros)
        }
    }

    fun addImagenesOtros(idOtros: Long){
        var averiaOtros = request.value!!.Averias.firstOrNull{ it.CodigoAveria == Constants.TiposAveria.otro.name }

        //Asignar id de avería a fotos corresponientes
        var imagenes:  MutableList<ImagenInspecAeronaveEntity> = mutableListOf()
        averiaOtros?.Imagenes?.forEach{
            var imagen: ImagenInspecAeronaveEntity = ImagenInspecAeronaveEntity(idAveria = idOtros, imagen = it)
            imagenes.add(imagen)
        }


        imagenInteractor.addImagebInspecAeronave(imagenes) { insetredIds ->
            Log.i("INSPEC_AERONAVE", "Se han guardado exitosamente un total de " + imagenes.size + " imagenes oara OTROS")
            guardadoLocal.value = true

            var a = insetredIds
        }


    }


    //Reiniciar VM
    fun reiniciarVM(){
        esWideBody.value = false
        noVuelo.value = ""
        matricula.value = ""
        equipo.value = ""
        fechaVuelo.value = ""
        origen.value = ""
        destino.value = ""
        guidVuelo.value = ""

        request.value = InspeccionAeronaveEntity()

        hasFinished .value= false
        idInspeccionCreada.value =  0

        createDataSource()
    }

    fun saveInspeccion(inspeccionAeronave: InspeccionAeronave): MutableLiveData<CreateInspeccionResponsaBase?> {

        return dataSource!!.saveInspeccion(inspeccionAeronave)
    }

    fun getEmpleado(numEmpleado: String): MutableLiveData<CORE_Base> {
        var datacore = coreRepository.getCoreDataSource()
        datacore.getEmpleado(numEmpleado)
        return datacore.responseCore
    }

    fun saveLocal(inspeccionAeronave: InspeccionAeronave) {

        interactorInspeccionAeronave.addInspeccionLocal(inspeccionAeronave)
    }

    fun consultarExisLocal(guid: String, llegada: Boolean): MutableLiveData<InspeccionAeronave?> {
        return interactorInspeccionAeronave.consultarExisLocal(guid,llegada)
    }

    fun deleteByguid(guid: String,isLlegada:Boolean){
        interactorInspeccionAeronave.deleteByguid(guid, isLlegada)
    }
}