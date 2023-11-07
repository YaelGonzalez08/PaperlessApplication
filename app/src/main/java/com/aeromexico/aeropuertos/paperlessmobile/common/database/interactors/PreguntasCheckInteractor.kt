package com.aeromexico.aeropuertos.paperlessmobile.common.database.interactors

import androidx.lifecycle.LiveData
import com.aeromexico.aeropuertos.paperlessmobile.common.database.daos.PreguntasCheckDAO
import com.aeromexico.aeropuertos.paperlessmobile.common.database.entities.PreguntasCheckEntity
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionEquipo.pojos.PreguntasCheckListToServer
import org.jetbrains.anko.doAsync

class PreguntasCheckInteractor(private val daoPreguntas: PreguntasCheckDAO) {

    fun addPreguntasCheck(date: String, lista: List<PreguntasCheckListToServer>): Long {

        for (pregunta: PreguntasCheckListToServer in lista) {

            var aux = PreguntasCheckEntity(
                fecha = date,
                id = pregunta.id,
                estate = pregunta.estate ?: 0,
                observation = pregunta.observation,
                photo = pregunta.photo
            )

            doAsync {
                daoPreguntas.addPreguntaCheck(aux)
            }
        }

        return 0
    }

    fun readPreguntakByFecha(date: String): LiveData<List<PreguntasCheckEntity>> {
        return daoPreguntas.readPreguntakByFecha(date)
    }

    fun deletePreguntakByFecha(date: String) {
        doAsync {
            daoPreguntas.deletePreguntakByFecha(date)
        }
    }
}