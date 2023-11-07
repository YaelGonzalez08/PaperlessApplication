package com.aeromexico.aeropuertos.paperlessmobile.campanaNotificaciones.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.aeromexico.aeropuertos.paperlessmobile.PaperlessApplication
import com.aeromexico.aeropuertos.paperlessmobile.common.database.entities.DeshieloEntity
import com.aeromexico.aeropuertos.paperlessmobile.common.database.interactors.DeshieloInteractor
import com.aeromexico.aeropuertos.paperlessmobile.desHielo.pojos.DeshieloToServer

class DeshieloPendienteViewModel : ViewModel() {
    private val interactorDeshielo: DeshieloInteractor = DeshieloInteractor(PaperlessApplication.database.deshieloDao())

    fun getDeshieloPendientes(): LiveData<List<DeshieloEntity>> {
        return interactorDeshielo.allDeshielo
    }

    fun deleteFromBD(index: Int, obj: DeshieloToServer) {
        interactorDeshielo.deleteDeshielocheckById(index)
    }

}