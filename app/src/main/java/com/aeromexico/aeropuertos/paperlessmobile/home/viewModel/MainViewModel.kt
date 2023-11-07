package com.aeromexico.aeropuertos.paperlessmobile.home.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aeromexico.aeropuertos.paperlessmobile.PaperlessApplication
import com.aeromexico.aeropuertos.paperlessmobile.common.database.entities.*
import com.aeromexico.aeropuertos.paperlessmobile.common.database.interactors.*
import com.aeromexico.aeropuertos.paperlessmobile.mensajesOperacionales.model.MOInteractor


class MainViewModel: ViewModel() {

    //Interactors
    private val interactor: MOInteractor = MOInteractor()
    val interactorCheckList: CheckListDiarioInteractor =
        CheckListDiarioInteractor(PaperlessApplication.database.checkListEquipoDriarioDAO())
    private val inspeccionIinteractor: InspecAeronaveInteractor = InspecAeronaveInteractor()
    private val interactorDeshielo: DeshieloInteractor = DeshieloInteractor(PaperlessApplication.database.deshieloDao())
    private val interactorORdernCarga: CargaCombustibleInteractor = CargaCombustibleInteractor(PaperlessApplication.database.cargaCombustible())
    private val interactorPrimerVuelo: PrimerVueloDiaInteractor = PrimerVueloDiaInteractor(PaperlessApplication.database.primerVueloDia())
    private val interactorNotoc:  NotocInteractor = NotocInteractor()

    private val inspecAeronavePendientesCount: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>().also {
            inspecAeronaveCount()
        }
    }


    private val firmas= MutableLiveData<MutableList<ModificarDetalleLirEntity>>()
    private val cambioFirmas= MutableLiveData<Boolean>()

    private val cuenta: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>().also {
            loadFirmasFaltantes()
        }
    }
    private val notocCuenta: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>().also {
            loadNotocFaltante()
        }
    }
    fun loadNotocFaltante(){
        interactorNotoc.readAll {
            notocCuenta.value = it.size
        }
    }
    fun loadFirmasFaltantes() {

        interactor.getAllFirmasPendientes {
            firmas.value= it
            cuenta.value = it.size
        }
    }

    fun inspecAeronaveCount() {
        inspeccionIinteractor.getAllInspecAeronave {
            inspecAeronavePendientesCount.value = it.size
        }
    }

    fun getFirmas(): LiveData<MutableList<ModificarDetalleLirEntity>>{
        return firmas
    }
    fun setCambioFirmas(){
        cambioFirmas.value = true
    }
    fun getCambioFirmas(): LiveData<Boolean>{
        return cambioFirmas
    }
    fun getCuenta(): LiveData<Int>{
        return cuenta
    }
    fun getEquiposPendientes(): LiveData<List<CheckEquipoDiarioEntity>> {
       return interactorCheckList.allChecks

    }
    fun getDeshieloPendientes(): LiveData<List<DeshieloEntity>> {
        return interactorDeshielo.allDeshielo
    }
    fun getInspecAeronaveCount(): LiveData<Int> {
        return inspecAeronavePendientesCount
    }
    fun getCargaCombustibleCount(): LiveData<List<CargacombustibleEntity>> {
        return interactorORdernCarga.allCargaCombustible
    }

    fun getPrimerVueloCount(): LiveData<List<CheckPrimeVueloEntity>> {
        return interactorPrimerVuelo.allForms
    }
    fun getNotocCount(): LiveData<Int>{
        return notocCuenta
    }


}