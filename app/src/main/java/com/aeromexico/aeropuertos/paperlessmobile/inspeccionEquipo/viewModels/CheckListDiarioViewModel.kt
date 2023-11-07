package com.aeromexico.aeropuertos.paperlessmobile.inspeccionEquipo.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aeromexico.aeropuertos.paperlessmobile.PaperlessApplication
import com.aeromexico.aeropuertos.paperlessmobile.common.database.interactors.CheckListDiarioInteractor
import com.aeromexico.aeropuertos.paperlessmobile.common.database.interactors.PreguntasCheckInteractor
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionEquipo.dataSource.CheckListDataSource
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionEquipo.pojos.CheckToServer
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionEquipo.pojos.PreguntasCheckListToServer
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionEquipo.repository.CheckListRepository
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.CheckListResponseObjects

class CheckListDiarioViewModel(private val repository: CheckListRepository) : ViewModel() {
    private val interactorCheckList: CheckListDiarioInteractor =
        CheckListDiarioInteractor(PaperlessApplication.database.checkListEquipoDriarioDAO())
    private val interactorPreguntasCheclist: PreguntasCheckInteractor =
        PreguntasCheckInteractor(PaperlessApplication.database.preguntasCheckDAO())

    lateinit var checkListDatasource: CheckListDataSource

    fun SentCheckList(
        data: CheckToServer
    ): MutableLiveData<CheckListResponseObjects> {
        checkListDatasource = repository.requestCheckListDataSource()
        checkListDatasource.requestSendCheckList(data)
        return checkListDatasource.responseCheckList
    }

    fun addCheckBD(
        data: CheckToServer,
        lista: List<PreguntasCheckListToServer>
    ): MutableLiveData<Long> {
        addImagesCheckList(lista, data.fecha)
        return interactorCheckList.addCheckEquipo(data)
    }

    private fun addImagesCheckList(lista: List<PreguntasCheckListToServer>, fecha: String) {
        Log.i("Iamgenes recivbidas", "-> ${lista.size}")
        interactorPreguntasCheclist.addPreguntasCheck(fecha, lista)
    }
}