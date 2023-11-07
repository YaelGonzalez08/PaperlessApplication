package com.aeromexico.aeropuertos.paperlessmobile.common.database.interactors

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aeromexico.aeropuertos.paperlessmobile.PaperlessApplication
import com.aeromexico.aeropuertos.paperlessmobile.common.database.daos.InspeccionAeronaveDAO
import com.aeromexico.aeropuertos.paperlessmobile.common.database.daos.InspeccionDatosDAO
import com.aeromexico.aeropuertos.paperlessmobile.common.database.entities.DeshieloEntity
import com.aeromexico.aeropuertos.paperlessmobile.common.database.entities.ImagenesEntity
import com.aeromexico.aeropuertos.paperlessmobile.common.database.entities.InspeccionDatosEntity
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronaveREF.pojos.InspeccionAeronave
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.Imagenes
import com.google.gson.Gson
import java.util.ArrayList

class InspeccionAeronavesInteractor {
    var daoInspeccion: InspeccionAeronaveDAO =
        PaperlessApplication.database.getInspeccionAeronaveDAO()
    var datosInspeccion: InspeccionDatosDAO = PaperlessApplication.database.getInspeccionDatosDAO()


    private fun readAllImagenesEntity(): List<ImagenesEntity> {
        return daoInspeccion.readAllImagenesEntity()
    }

    fun addImagenesAverias(imagenEntity: ImagenesEntity) {
        var allImagenes: List<ImagenesEntity> = readAllImagenesEntity()
        if (allImagenes.isNullOrEmpty()) {
            daoInspeccion.addImagen(imagenEntity)
        } else {
            var existInBase: ImagenesEntity? = null

            allImagenes?.forEach { imgOnTable ->
                if (imagenEntity.guid == imgOnTable.guid && imagenEntity.IsLlegada == imgOnTable.IsLlegada && imagenEntity.codigo == imgOnTable.codigo) {
                    imgOnTable.datos = imagenEntity.datos
                    existInBase = imgOnTable
                }
            }
            if (existInBase != null) {
                daoInspeccion.addImagen(existInBase!!)
            } else {
                daoInspeccion.addImagen(imagenEntity)
            }
        }
    }

    fun addInspeccionLocal(inspeccionAeronave: InspeccionAeronave) {

        inspeccionAeronave.averias?.forEach { averia ->
            addImagenesAverias(
                ImagenesEntity(
                    guid = inspeccionAeronave.guidVuelo.toString(),
                    IsLlegada = inspeccionAeronave.esLlegada!!,
                    codigo = averia.Codigo.toString(),
                    datos = Gson().toJson(averia.imagenes).toString()
                )
            )
            averia.imagenes = arrayListOf()
        }
        saveDatosInspeccion(inspeccionAeronave)
        print(inspeccionAeronave.toString())

    }

    private fun saveDatosInspeccion(inspeccionAeronave: InspeccionAeronave) {
        //aqui ya no tenemos imagenes pero si la demas info
        var listDatos:List<InspeccionDatosEntity> = datosInspeccion.readAllInspeccionDatosEntity()

        var found = listDatos.firstOrNull {
            inspeccionAeronave.guidVuelo == it.guid && inspeccionAeronave.esLlegada == it.IsLlegada
        }
        if(found != null){
            found.datos = Gson().toJson(inspeccionAeronave)
            datosInspeccion.addDatosInspeccion(found)
        }else{
            datosInspeccion.addDatosInspeccion(InspeccionDatosEntity(guid = inspeccionAeronave.guidVuelo.toString(), IsLlegada = inspeccionAeronave.esLlegada!!, datos = Gson().toJson(inspeccionAeronave)))
        }


    }

    fun consultarExisLocal(guid: String, llegada: Boolean): MutableLiveData<InspeccionAeronave?> {
        var result =  MutableLiveData<InspeccionAeronave?>()
        //aqui ya no tenemos imagenes pero si la demas info
        var listDatos:List<InspeccionDatosEntity> = datosInspeccion.readAllInspeccionDatosEntity()

        var found = listDatos.firstOrNull {
            guid == it.guid && llegada == it.IsLlegada
        }
        if(found != null){
            var formSinImgs = Gson().fromJson(found.datos,InspeccionAeronave::class.java)
            formSinImgs.averias?.forEach {
                it.imagenes = getImagenByAveria(formSinImgs.guidVuelo,formSinImgs.esLlegada,it.Codigo)?.toCollection(ArrayList<Imagenes>())
            }

            result.postValue(formSinImgs)

        }else{
            result.postValue(null)
        }
        return result
    }

    private fun getImagenByAveria(
        guidVuelo: String?,
        esLlegada: Boolean?,
        codigo: String?
    ): Array<Imagenes>? {
        var allImagenes: List<ImagenesEntity> = readAllImagenesEntity()
        var arrayImgs:Array<Imagenes>? = arrayOf()

        if (allImagenes.isNullOrEmpty()) {
            return arrayImgs
        } else {
            var existInBase: ImagenesEntity? = null

            allImagenes?.forEach { imgOnTable ->
                if (guidVuelo == imgOnTable.guid && esLlegada == imgOnTable.IsLlegada && codigo == imgOnTable.codigo) {
                    existInBase = imgOnTable
                }
            }

            if (existInBase != null) {
                arrayImgs= Gson().fromJson(existInBase!!.datos,Array<Imagenes>::class.java)
            }
        }
        return arrayImgs
    }

    fun deleteByguid(guid: String,isLlegada:Boolean){
        daoInspeccion.deleteByGuid(guid,isLlegada)
        datosInspeccion.deleteByGuid(guid, isLlegada)
    }

}