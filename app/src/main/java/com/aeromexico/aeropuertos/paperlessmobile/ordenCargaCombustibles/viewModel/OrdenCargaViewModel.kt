package com.aeromexico.aeropuertos.paperlessmobile.ordenCargaCombustibles.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aeromexico.aeropuertos.paperlessmobile.PaperlessApplication
import com.aeromexico.aeropuertos.paperlessmobile.common.database.entities.CargacombustibleEntity
import com.aeromexico.aeropuertos.paperlessmobile.common.database.interactors.CargaCombustibleInteractor
import com.aeromexico.aeropuertos.paperlessmobile.ordenCargaCombustibles.dataSource.OrdenCargaDataSource
import com.aeromexico.aeropuertos.paperlessmobile.ordenCargaCombustibles.entities.RequestOrdenCarga
import com.aeromexico.aeropuertos.paperlessmobile.ordenCargaCombustibles.entities.ResponseCheckOrdenCarga
import com.aeromexico.aeropuertos.paperlessmobile.ordenCargaCombustibles.repository.OrdenCargaRepository
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.OrdeCargaResponse
import com.aeromexico.aeropuertos.paperlessmobile.webService.WebServiceApi

class OrdenCargaViewModel: ViewModel() {
    var ordenCargaDataSource: OrdenCargaDataSource = OrdenCargaRepository(WebServiceApi().getOrdenCargaApi()).requestOrdenCargaDataSource()
    val interactorCargaCombustible:CargaCombustibleInteractor = CargaCombustibleInteractor(PaperlessApplication.database.cargaCombustible())

    var allCargaCombustible: LiveData<List<CargacombustibleEntity>>

    init {
        allCargaCombustible = interactorCargaCombustible.allCargaCombustible
    }

    fun insertarOrdenCarga(requestOrdenCarga: RequestOrdenCarga): MutableLiveData<OrdeCargaResponse>{
        ordenCargaDataSource.insertOrdenCarga(requestOrdenCarga)
        return ordenCargaDataSource.ordeCargaResponse
    }

    fun checkOrdenCarga(guiVuelo:String): MutableLiveData<ResponseCheckOrdenCarga> {
        ordenCargaDataSource.checkOrdenCarga(guiVuelo)
        return ordenCargaDataSource.responseCheckOrdenCarga
    }

    fun guardarOrdenCarga(requestOrdenCarga: RequestOrdenCarga): MutableLiveData<Long> {
        return interactorCargaCombustible.addCargaCombustible(requestOrdenCarga)
    }
    fun updateCargaCOmbustible(requestOrdenCarga: CargacombustibleEntity): MutableLiveData<Int> {
        return interactorCargaCombustible.updateCargaCOmbustible(requestOrdenCarga)
    }

    fun deleteCargaCombustiblecheckById(id:Int){
        interactorCargaCombustible.deleteCargaCombustiblecheckById(id)
    }
    fun readAllCarfaCombustible(): LiveData<List<CargacombustibleEntity>> {
        return interactorCargaCombustible.allCargaCombustible
    }
}