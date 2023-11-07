package com.aeromexico.aeropuertos.paperlessmobile.common.CORE

import androidx.lifecycle.MutableLiveData
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.RequestState
import com.aeromexico.aeropuertos.paperlessmobile.metar.objects.MetarResponse
import com.aeromexico.aeropuertos.paperlessmobile.webService.IAPI.CoreApi
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.CORE_Base
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.EquipoResponseObjects
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.PDFB64FILEResponse
import com.google.gson.Gson
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CoreDataSource(private val api: CoreApi) {
    var responseStateCore = MutableLiveData<RequestState>()
    var responseCore = MutableLiveData<CORE_Base>()
    var responseEquipo = MutableLiveData<EquipoResponseObjects>()

    var responseMetar = MutableLiveData<MetarResponse>()

    fun getMetar( guidVuelo:String){
        responseStateCore.value = RequestState(RequestState.REQ_IN_PROGRESS)
        api.getMetar(guidVuelo).enqueue(object : Callback<MetarResponse>{
            override fun onFailure(call: Call<MetarResponse>, t: Throwable) {
                responseStateCore.value = RequestState(RequestState.REQ_BAD)
                responseMetar.postValue(null)
            }

            override fun onResponse(call: Call<MetarResponse>, response: Response<MetarResponse>) {
                if(response.isSuccessful){
                    responseStateCore.value = RequestState(RequestState.REQ_OK)
                    responseMetar.postValue(response.body())
                }
                else{
                    responseStateCore.value = RequestState(RequestState.REQ_BAD)
                    responseMetar.postValue(null)

                }
            }
        })
    }


    fun getEmpleado(noEmpleado: String){
        responseStateCore.value = RequestState(RequestState.REQ_IN_PROGRESS)
        api.getEmpleado(noEmpleado).enqueue(object : Callback<CORE_Base>{
            override fun onFailure(call: Call<CORE_Base>, t: Throwable) {
                responseStateCore.value = RequestState(RequestState.REQ_BAD)
                responseCore.postValue(null)
            }

            override fun onResponse(call: Call<CORE_Base>, response: Response<CORE_Base>) {
                if(response.isSuccessful){
                    responseStateCore.value = RequestState(RequestState.REQ_OK)
                    responseCore.postValue(response.body())
                }
                else{
                    responseStateCore.value = RequestState(RequestState.REQ_BAD)
                    responseCore.postValue(null)

                }
            }
        })
    }

    fun getEquipo(idEquipo: String) {
        responseStateCore.value = RequestState(RequestState.REQ_IN_PROGRESS)

        api.getEquipo(idEquipo).enqueue(object : Callback<EquipoResponseObjects> {
            override fun onResponse(
                call: Call<EquipoResponseObjects>,
                response: Response<EquipoResponseObjects>
            ) {
                if (response.isSuccessful) {
                    responseStateCore.value = RequestState(RequestState.REQ_OK)
                    responseEquipo.postValue(response.body())
                } else {
                    responseStateCore.value = RequestState(RequestState.REQ_BAD)
                    val jObjError: JSONObject? = JSONObject(response.errorBody()!!.string())
                    val gson = Gson()
                    responseEquipo.postValue(
                        gson.fromJson(
                            jObjError.toString(),
                            EquipoResponseObjects::class.java
                        )
                    )
                }
            }


            override fun onFailure(call: Call<EquipoResponseObjects>, t: Throwable) {
                responseStateCore.value = RequestState(RequestState.REQ_BAD)
                responseEquipo.postValue(null)
            }
        })
    }


    fun getPDFileB64(): MutableLiveData<PDFB64FILEResponse?> {
        responseStateCore.value = RequestState(RequestState.REQ_IN_PROGRESS)
        var pdfImageResponse = MutableLiveData<PDFB64FILEResponse?>()

        api.getPDFFileB64().enqueue(object:Callback<PDFB64FILEResponse>{
            override fun onResponse(
                call: Call<PDFB64FILEResponse>,
                response: Response<PDFB64FILEResponse>
            ) {
                if (response.isSuccessful) {
                    responseStateCore.value = RequestState(RequestState.REQ_OK)
                    pdfImageResponse.postValue(response.body())
                } else {
                    responseStateCore.value = RequestState(RequestState.REQ_BAD)
                    pdfImageResponse.postValue(null)

                }

            }

            override fun onFailure(call: Call<PDFB64FILEResponse>, t: Throwable) {
                responseStateCore.value = RequestState(RequestState.REQ_BAD)
                pdfImageResponse.postValue(null)
            }

        })
        return pdfImageResponse

    }
}