package com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronave.DataSource

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.aeromexico.aeropuertos.paperlessmobile.common.database.entities.InspeccionAeronaveEntity
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.Constants
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.RequestState
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.requestAverias
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronaveREF.pojos.InspeccionAeronave
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronaveREF.pojos.RequestExisteInspeccion
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronaveREF.pojos.ResponseExisteInspeccion
import com.aeromexico.aeropuertos.paperlessmobile.webService.IAPI.InspeccionAeronaveAPI
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.Averia
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.CreateInspeccionResponsaBase
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.GetTiposAveriaResponsaBase
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.ResponseMOBaseDetalle
import com.google.gson.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InspeccionAeronaveDataSource(private val api: InspeccionAeronaveAPI) {

    var responseStateCreateInspeccion = MutableLiveData<RequestState>()
    var responseCreateInspeccion = MutableLiveData<CreateInspeccionResponsaBase>()



    var responseGetTiposAveria = MutableLiveData<GetTiposAveriaResponsaBase>()



    fun createInspeccionAeronave(inspeccionAeronaveEntity: InspeccionAeronaveEntity){
        responseStateCreateInspeccion.value = RequestState(RequestState.REQ_IN_PROGRESS)
        val jsonObj_ = JsonObject()

        jsonObj_.addProperty("GuidVuelo", inspeccionAeronaveEntity.GuidVuelo.trim())
        jsonObj_.addProperty("CreadoPor", inspeccionAeronaveEntity.CreadoPor.trim())
        jsonObj_.addProperty("FechaCreacion", inspeccionAeronaveEntity.FechaCreacion)
        jsonObj_.addProperty("NombreOficialOperaciones", inspeccionAeronaveEntity.NombreOficialOperaciones)
        jsonObj_.addProperty("NoEmpleadoOficialOperaciones", inspeccionAeronaveEntity.NoEmpleadoOficialOperaciones)
        jsonObj_.addProperty("FirmaB64OficialOperaciones", inspeccionAeronaveEntity.FirmaB64OficialOperaciones)
        if(inspeccionAeronaveEntity.NombreTecnicoMantenimiento != ""){
            jsonObj_.addProperty("NombreTecnicoMantenimiento", inspeccionAeronaveEntity.NombreTecnicoMantenimiento)
        }
        if(inspeccionAeronaveEntity.NoEmpleadoTecnicoMantenimiento != ""){
            jsonObj_.addProperty("NoEmpleadoTecnicoMantenimiento", inspeccionAeronaveEntity.NoEmpleadoTecnicoMantenimiento)
        }
        if(inspeccionAeronaveEntity.FirmaB64TecnicoMantenimiento != ""){
            jsonObj_.addProperty("FirmaB64TecnicoMantenimiento", inspeccionAeronaveEntity.FirmaB64TecnicoMantenimiento)
        }
        jsonObj_.addProperty("Pernocta", inspeccionAeronaveEntity.Pernocta)
        jsonObj_.addProperty("EsLlegada", inspeccionAeronaveEntity.EsLlegada)

        if(inspeccionAeronaveEntity.EsLlegada!!){
            jsonObj_.addProperty("ResponsablesEstiba", "")

        }else{
            jsonObj_.addProperty("ResponsablesEstiba", inspeccionAeronaveEntity.ResponsablesEstiba)

        }



        val averias = JsonArray()
        inspeccionAeronaveEntity.Averias.forEach{
            var averia = JsonObject()

            averia.addProperty("CodigoAveria", it.CodigoAveria.trim())
            averia.addProperty("TieneAveria", it.TieneAveria)
            averia.addProperty("DescripcionAveria", it.DescripcionAveria?.trim())


            if(it.CodigoAveria == Constants.TiposAveria.otro.name){
                val imagenes = JsonArray()
                it.Imagenes?.forEach {
                    val imagen = JsonObject()

                    imagen.addProperty("ImgB64", it)
                    imagenes.add(imagen)
                }
                averia.add("Imagenes", imagenes)

            }else{
                val imagenes = JsonArray()

                if(!it.ImgB64.isNullOrEmpty()){
                    val imagen = JsonObject()

                    imagen.addProperty("ImgB64", it.ImgB64)
                    imagenes.add(imagen)
                }

                averia.add("Imagenes", imagenes)
            }

            averias.add(averia)
        }

        jsonObj_.add("Averias", averias)


        var b = jsonObj_.toString()

        api.createInspeccionAeronave(jsonObj_).enqueue(object : Callback<CreateInspeccionResponsaBase> {
            override fun onFailure(call: Call<CreateInspeccionResponsaBase>, t: Throwable) {
                responseStateCreateInspeccion.value = RequestState(RequestState.REQ_BAD)

            }

            override fun onResponse(call: Call<CreateInspeccionResponsaBase>, response: Response<CreateInspeccionResponsaBase>) {
                if(response.isSuccessful){
                    responseStateCreateInspeccion.value = RequestState(RequestState.REQ_OK)
                    responseCreateInspeccion.postValue(response.body())
                }
                else {
                    responseStateCreateInspeccion.value = RequestState(RequestState.REQ_BAD)
                    val jObjError: JSONObject? = JSONObject(response.errorBody()?.string())
                    val gson = Gson()
                    responseCreateInspeccion.postValue(
                        gson.fromJson(
                           jObjError.toString(),
                            CreateInspeccionResponsaBase::class.java
                        )
                    )
                }
            }
        })
    }


    fun GetInspeccion(
        guid: String,
        isLlegada: Boolean
    ): MutableLiveData<ResponseExisteInspeccion?> {
        var responseExisteInspeccion = MutableLiveData<ResponseExisteInspeccion?>()
        var j: JsonObject = JsonParser.parseString(
            Gson().toJson(RequestExisteInspeccion(guid, isLlegada)).toString()
        ).asJsonObject

        api.GetInspeccion(j).enqueue(object : Callback<ResponseExisteInspeccion> {
            override fun onResponse(
                call: Call<ResponseExisteInspeccion>,
                response: Response<ResponseExisteInspeccion>
            ) {
                if (response.isSuccessful) {
                    responseExisteInspeccion.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<ResponseExisteInspeccion>, t: Throwable) {
                responseExisteInspeccion.postValue(null)
            }

        })
        return responseExisteInspeccion

    }

    fun getTiposAveria(esSalida:Boolean,esLlegada:Boolean): MutableLiveData<GetTiposAveriaResponsaBase> {
        var responseStateGetTiposAveria = MutableLiveData<RequestState>()
        var j: JsonObject = JsonParser.parseString(
            Gson().toJson(requestAverias(esSalida,esLlegada)).toString()
        ).asJsonObject

        api.getTiposAveria(j).enqueue(object : Callback<GetTiposAveriaResponsaBase> {

            override fun onFailure(call: Call<GetTiposAveriaResponsaBase>, t: Throwable) {
                responseStateGetTiposAveria.value = RequestState(RequestState.REQ_BAD)
            }

            override fun onResponse(call: Call<GetTiposAveriaResponsaBase>, response: Response<GetTiposAveriaResponsaBase>) {
                if(response.isSuccessful){
                    responseStateGetTiposAveria.value = RequestState(RequestState.REQ_OK)
                    responseGetTiposAveria.postValue(response.body())
                }
                else {
                    responseStateGetTiposAveria.value = RequestState(RequestState.REQ_BAD)
                    val jObjError: JSONObject? = JSONObject(response.errorBody()?.string())
                    val gson = Gson()
                    responseGetTiposAveria.postValue(
                        gson.fromJson(
                            jObjError.toString(),
                            GetTiposAveriaResponsaBase::class.java
                        )
                    )
                }
            }


        })
        return responseGetTiposAveria

    }
    var responseError = MutableLiveData<String?>()

    fun saveInspeccion(inspeccionAeronave: InspeccionAeronave): MutableLiveData<CreateInspeccionResponsaBase?> {
        var lista = arrayListOf<Averia>()
        inspeccionAeronave.averias?.forEach {
            lista.add(it)
        }
        lista.forEach {
            if (it.naSelected) {
                it.descripcionAveria = "N/A"
            } else if (it.respNumerica == true) {

            } else if (it.tieneAveria == null) {
                inspeccionAeronave.averias?.remove(it)
            }
        }
        var responseExisteInspeccion = MutableLiveData<CreateInspeccionResponsaBase?>()
        var j: JsonObject = JsonParser.parseString(
            Gson().toJson(inspeccionAeronave).toString()
        ).asJsonObject
        api.createInspeccionAeronave(j).enqueue(object : Callback<CreateInspeccionResponsaBase> {
            override fun onResponse(
                call: Call<CreateInspeccionResponsaBase>,
                response: Response<CreateInspeccionResponsaBase>
            ) {
                if (response.isSuccessful) {
                    responseExisteInspeccion.postValue(response.body())
                }else{
                    responseError.postValue("Error en petición\n${response.errorBody()}")

                    responseExisteInspeccion.postValue(null)
                }
            }

            override fun onFailure(call: Call<CreateInspeccionResponsaBase>, t: Throwable) {
                responseError.postValue("Error de Conexión\n${t.localizedMessage}")

                responseExisteInspeccion.postValue(null)
            }

        })
        return responseExisteInspeccion
    }
}