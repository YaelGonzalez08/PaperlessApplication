package com.aeromexico.aeropuertos.paperlessmobile.campanaNotificaciones.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.aeromexico.aeropuertos.paperlessmobile.PaperlessApplication
import com.aeromexico.aeropuertos.paperlessmobile.common.database.entities.CheckEquipoDiarioEntity
import com.aeromexico.aeropuertos.paperlessmobile.common.database.entities.PreguntasCheckEntity
import com.aeromexico.aeropuertos.paperlessmobile.common.database.interactors.CheckListDiarioInteractor
import com.aeromexico.aeropuertos.paperlessmobile.common.database.interactors.PreguntasCheckInteractor
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionEquipo.pojos.CheckToServer

class CheckListPendientesViewModel : ViewModel() {
    private val interactorCheckList: CheckListDiarioInteractor =
        CheckListDiarioInteractor(PaperlessApplication.database.checkListEquipoDriarioDAO())
    private val interactorPreguntasCheclist: PreguntasCheckInteractor = PreguntasCheckInteractor(PaperlessApplication.database.preguntasCheckDAO())

    var allChecks: LiveData<List<CheckEquipoDiarioEntity>>

    init {
        allChecks = interactorCheckList.allChecks
    }

    fun readImegesChech(date:String): LiveData<List<PreguntasCheckEntity>> {
        return interactorPreguntasCheclist.readPreguntakByFecha(date)
    }

    fun deleteCheck(index: Int, checkToServer: CheckToServer){
        interactorCheckList.deleteCheckEquipoById(index)
        interactorPreguntasCheclist.deletePreguntakByFecha(checkToServer.fecha)
    }


}