package com.aeromexico.aeropuertos.paperlessmobile.busquedaVuelo.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aeromexico.aeropuertos.paperlessmobile.busquedaVuelo.model.BusquedaVueloDataSource
import com.aeromexico.aeropuertos.paperlessmobile.busquedaVuelo.VueloRepository
import com.aeromexico.aeropuertos.paperlessmobile.busquedaVuelo.model.RequestBuscarVuelo
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.RequestState
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.ResponseBase

class BuscarVueloViewModel(private val repository: VueloRepository?) : ViewModel() {
    private var selectedModule= MutableLiveData<String>()

    var requestBuscarVuelo= RequestBuscarVuelo()
    lateinit var responseBuscarVuelo: MutableLiveData<ResponseBase>
    var vueloDataSource: BusquedaVueloDataSource? = repository?.requestBusquedaVuelo()
    //response state
    var responseState: MutableLiveData<RequestState>? = vueloDataSource?.responseState

    fun buscarVuelo(){
        responseBuscarVuelo = vueloDataSource!!.responseBuscarVuelo
        vueloDataSource?.requestBuscarVuelo(requestBuscarVuelo)
    }

    fun setSelectedModule(module: String){
        selectedModule.value = module
    }
    fun getSelectedModule():LiveData<String>{
        return selectedModule
    }



}