package com.aeromexico.aeropuertos.paperlessmobile.comunicadosACK.datasource

import androidx.lifecycle.MutableLiveData
import com.aeromexico.aeropuertos.paperlessmobile.comunicadosACK.pojos.ComunicadoPdfResponse
import com.aeromexico.aeropuertos.paperlessmobile.comunicadosACK.pojos.ComunicadosResponse
import com.aeromexico.aeropuertos.paperlessmobile.comunicadosACK.pojos.RequestEnvioCuesitonario
import com.aeromexico.aeropuertos.paperlessmobile.comunicadosACK.pojos.ResponseEnvioCuestionario
import com.aeromexico.aeropuertos.paperlessmobile.webService.IAPI.ComunicadosAPI
import com.google.gson.Gson
import com.google.gson.JsonParser
import getFileResponse
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class comunicadosDataSource(var api: ComunicadosAPI) {
    var responseComunicados: MutableLiveData<ComunicadosResponse> =
        MutableLiveData<ComunicadosResponse>()
    var isLoading: MutableLiveData<Boolean> =
        MutableLiveData<Boolean>()

    fun getComunicados() {
        isLoading.postValue(true)
        api.getComunicados().enqueue(object : Callback<ComunicadosResponse> {
            override fun onResponse(
                call: Call<ComunicadosResponse>,
                response: Response<ComunicadosResponse>
            ) {
                isLoading.value = (false)
                if (response.isSuccessful) {
                    responseComunicados.postValue(response.body())
                } else {
                    responseComunicados.postValue(null)
                }
            }

            override fun onFailure(call: Call<ComunicadosResponse>, t: Throwable) {
                isLoading.value = (false)
                responseComunicados.postValue(null)
            }

        })
    }
   fun  getDownloadDocumentById(idArchivo: Int): MutableLiveData<getFileResponse?> {
       isLoading.postValue(true)
       var respons: MutableLiveData<getFileResponse?> =
           MutableLiveData<getFileResponse?>()

       api.getDownloadDocumentById(idArchivo).enqueue(object : Callback<getFileResponse> {
           override fun onResponse(
               call: Call<getFileResponse>,
               response: Response<getFileResponse>
           ) {
               isLoading.value = (false)
               if (response.isSuccessful) {
                   respons.postValue(response.body())
               } else
                   respons.postValue(null)
           }

           override fun onFailure(call: Call<getFileResponse>, t: Throwable) {
               isLoading.value = (false)
               respons.postValue(null)
           }

       })
       return respons
   }
    fun getComunicadoById(comunicadoId: Int): MutableLiveData<ComunicadoPdfResponse?> {
        isLoading.postValue(true)
        var respons: MutableLiveData<ComunicadoPdfResponse?> =
            MutableLiveData<ComunicadoPdfResponse?>()

        api.getComunicadoById(comunicadoId).enqueue(object : Callback<ComunicadoPdfResponse> {
            override fun onResponse(
                call: Call<ComunicadoPdfResponse>,
                response: Response<ComunicadoPdfResponse>
            ) {
                isLoading.value = (false)
                if (response.isSuccessful) {
                    respons.postValue(response.body())
                } else
                    respons.postValue(null)
            }

            override fun onFailure(call: Call<ComunicadoPdfResponse>, t: Throwable) {
                isLoading.value = (false)
                respons.postValue(null)
            }

        })
        return respons
    }

    fun sendComunicadoResuelto(requestEnvioCuesitonario: RequestEnvioCuesitonario): MutableLiveData<ResponseEnvioCuestionario?> {
        var respons: MutableLiveData<ResponseEnvioCuestionario?> =
            MutableLiveData<ResponseEnvioCuestionario?>()

        api.sendComunicadoResuelto(
            JsonParser.parseString(
            Gson().toJson(
               requestEnvioCuesitonario
            ).toString()
        ).asJsonObject).enqueue(object : Callback<ResponseEnvioCuestionario>{
            override fun onResponse(
                call: Call<ResponseEnvioCuestionario>,
                response: Response<ResponseEnvioCuestionario>
            ) {
                if (response.isSuccessful) {
                    respons.postValue(response.body())
                } else{
                    val jObjError: JSONObject? = JSONObject(response.errorBody()!!.string())
                    val gson = Gson()
                    respons.postValue(
                        gson.fromJson(
                            jObjError.toString(),
                            ResponseEnvioCuestionario::class.java
                        )
                    )


                }

            }

            override fun onFailure(call: Call<ResponseEnvioCuestionario>, t: Throwable) {
                respons.postValue(null)
            }
        })
        return respons
    }

}