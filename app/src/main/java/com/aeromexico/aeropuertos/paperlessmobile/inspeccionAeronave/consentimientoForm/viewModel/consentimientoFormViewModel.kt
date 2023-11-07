package com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronave.ConsentimientoForm.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aeromexico.aeropuertos.paperlessmobile.common.CORE.CoreDataSource
import com.aeromexico.aeropuertos.paperlessmobile.common.CORE.CoreRepository
import com.aeromexico.aeropuertos.paperlessmobile.common.database.entities.ResponsableEstibaEntiy
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.RequestState
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.CORE_Base
import com.aeromexico.aeropuertos.paperlessmobile.webService.WebServiceApi

class ConsentimientoFormViewModel: ViewModel() {

    //Firmas y datos de empleados
    private var ofOpEsExterno = MutableLiveData<Boolean>(false);
    val b64_oficial_operaciones = MutableLiveData<String>();
    val nombre_oficial_operaciones = MutableLiveData<String>();
    val no_emp_oficial_operaciones = MutableLiveData<String>();

    private var tecManEsExterno = MutableLiveData<Boolean>(false);
    val b64_tecnico_mantenimiento = MutableLiveData<String>();
    val nombre_tecnico_mantenimientos = MutableLiveData<String>();
    val no_emp_tecnico_mantenimiento = MutableLiveData<String>();

    //CORE
    lateinit var responseCore: MutableLiveData<CORE_Base>;
    private val repositoryCore: CoreRepository = CoreRepository(WebServiceApi().getCoreApi());
    var coreDatasource: CoreDataSource = repositoryCore.getCoreDataSource();
    var responseStateCore: MutableLiveData<RequestState> = coreDatasource.responseStateCore;

    private var respEstibaEsExterno = MutableLiveData<Boolean>(false);
    var responsablesEstiba = MutableLiveData<MutableList<ResponsableEstibaEntiy>>();

    //Setters
    fun setOfOpEsExterno(ofOpEsAM: Boolean){
        this.ofOpEsExterno.value = ofOpEsAM;
    }
    fun setBase64OfOperaciones(base64: String){
        this.b64_oficial_operaciones.value = base64;
    }
    fun setNombreOfOperaciones(nombre: String){
        this.nombre_oficial_operaciones.value = nombre;
    }
    fun setNoEmpOfOperaciones(noEmpleado: String){
        this.no_emp_oficial_operaciones.value = noEmpleado;
    }

    fun setTecManEsExterno(tecManEsAM: Boolean){
        this.tecManEsExterno.value = tecManEsAM;
    }
    fun setBase64TecMantenimiento(base64: String){
        this.b64_tecnico_mantenimiento.value = base64;
    }
    fun setNombreTecMantenimiento(nombre: String){
        this.nombre_tecnico_mantenimientos.value = nombre;
    }
    fun setNoEmpTecMantenimiento(noEmpleado: String){
        this.no_emp_tecnico_mantenimiento.value = noEmpleado;
    }

    fun setRespEstibaEsExterno(respEstibaEsAM: Boolean){
        this.respEstibaEsExterno.value = respEstibaEsAM;
    }


    //Getters
    fun getTecManEsExterno(): LiveData<Boolean> {
        return tecManEsExterno;
    }
    fun getBase64OfOperaciones(): LiveData<String> {
        return b64_oficial_operaciones;
    }
    fun getNombreOfOperaciones(): LiveData<String> {
        return nombre_oficial_operaciones;
    }
    fun getNoEmpOfOperaciones(): LiveData<String> {
        return no_emp_oficial_operaciones;
    }


    fun getOfOpEsExterno(): LiveData<Boolean> {
        return ofOpEsExterno;
    }
    fun getBase64TecMantenimiento(): LiveData<String> {
        return b64_tecnico_mantenimiento;
    }
    fun getNombreTecMantenimiento(): LiveData<String> {
        return nombre_tecnico_mantenimientos;
    }
    fun getNoEmpTecMantenimiento(): LiveData<String> {
        return no_emp_tecnico_mantenimiento;
    }

    fun getResponsablesEstiba():  LiveData<MutableList<ResponsableEstibaEntiy>> {
        return responsablesEstiba;
    }

    fun getRespEstibaEsExterno(): LiveData<Boolean> {
        return respEstibaEsExterno;
    }


    //Métodos
    fun buscarOficial(noEmpleado: String): MutableLiveData<CORE_Base> {
        coreDatasource = repositoryCore.getCoreDataSource();
       // responseCore = coreDatasource.responseCore;
        coreDatasource.getEmpleado(noEmpleado);
        return coreDatasource.responseCore;
    }

    fun buscarTecnico(noEmpleado: String): MutableLiveData<CORE_Base> {
        coreDatasource = repositoryCore.getCoreDataSource();
        // responseCore = coreDatasource.responseCore;
        coreDatasource.getEmpleado(noEmpleado);
        return coreDatasource.responseCore;
    }

    fun buscarResponsable(noEmpleado: String): MutableLiveData<CORE_Base> {
        coreDatasource = repositoryCore.getCoreDataSource();
        // responseCore = coreDatasource.responseCore;
        coreDatasource.getEmpleado(noEmpleado);
        return coreDatasource.responseCore;
    }


    fun getByEmployeeNumber(employeeNumber: String?): ResponsableEstibaEntiy?{
        val index = this.responsablesEstiba.value?.indexOfFirst { r -> r.noEmpleado.replace("AM", "", ignoreCase = true) == employeeNumber  };

        return if(index != -1){
            this.responsablesEstiba.value?.get(index!!);
        }else{
            null;
        }
    }

    fun addResponsableEstiba(responsableEstiba: ResponsableEstibaEntiy){
        var lista = this.responsablesEstiba.value;
        lista?.add(responsableEstiba);
        this.responsablesEstiba.value = lista!!;
    }

    fun deleteResponsableEstiba(responsableEstiba: ResponsableEstibaEntiy){
        val index = this.responsablesEstiba.value?.indexOf(responsableEstiba);
        if(index != -1){
            var lista = this.responsablesEstiba.value;
            lista?.removeAt(index!!);
            this.responsablesEstiba.value = lista!!;
        }
    }


    //Reiniciar VM
    fun reiniciarVM(){
        ofOpEsExterno.value = false
        b64_oficial_operaciones.value = ""
        nombre_oficial_operaciones.value = ""
        no_emp_oficial_operaciones.value = ""

        tecManEsExterno.value = false
        b64_tecnico_mantenimiento.value = ""
        nombre_tecnico_mantenimientos.value = ""
        no_emp_tecnico_mantenimiento.value = ""

        respEstibaEsExterno.value = false
        responsablesEstiba = MutableLiveData<MutableList<ResponsableEstibaEntiy>>()
    }

}