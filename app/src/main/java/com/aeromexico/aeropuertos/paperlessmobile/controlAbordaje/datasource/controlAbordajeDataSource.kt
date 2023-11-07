package com.aeromexico.aeropuertos.paperlessmobile.controlAbordaje.datasource

import androidx.lifecycle.MutableLiveData
import com.aeromexico.aeropuertos.paperlessmobile.controlAbordaje.pojos.*
import com.aeromexico.aeropuertos.paperlessmobile.webService.IAPI.ControlAbordajeAPI
import com.google.gson.Gson
import com.google.gson.JsonParser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class controlAbordajeDataSource(var controlAPI: ControlAbordajeAPI) {

    var responseControl = MutableLiveData<PasajerosReponse>()
    var responseCheckIn = MutableLiveData<PasajerosReponse>()
    var responseEmpleadoInfo = MutableLiveData<GetEmployeeInfoResponse>()
    var resultInsertInfoOficial = MutableLiveData<resultInsertInfoOficial>()


    fun getEmployeeInfo(fligth: String) {
        controlAPI.getEmployeeInfo(fligth).enqueue(object : Callback<GetEmployeeInfoResponse> {
            override fun onResponse(
                call: Call<GetEmployeeInfoResponse>,
                response: Response<GetEmployeeInfoResponse>
            ) {
                if (response.isSuccessful) {
                    responseEmpleadoInfo.postValue(response.body())
                } else {
                    responseEmpleadoInfo.postValue(null)
                }
            }

            override fun onFailure(call: Call<GetEmployeeInfoResponse>, t: Throwable) {
                responseEmpleadoInfo.postValue(null)
            }
        })
    }
    fun getBoardedPasajeros(fligth: String) {
        controlAPI.getBoardedPasajeros(fligth).enqueue(object : Callback<PasajerosReponse> {
            override fun onResponse(
                call: Call<PasajerosReponse>,
                response: Response<PasajerosReponse>
            ) {
                if (response.isSuccessful) {
                    responseControl.postValue(response.body())
                } else {
                    responseControl.postValue(null)
                }
            }

            override fun onFailure(call: Call<PasajerosReponse>, t: Throwable) {
                responseControl.postValue(null)
            }
        })
    }
    fun requestNotBoardedPasajeros(fligth: String) {
        controlAPI.getNotBoardedPasajeros(fligth).enqueue(object : Callback<PasajerosReponse> {
            override fun onResponse(
                call: Call<PasajerosReponse>,
                response: Response<PasajerosReponse>
            ) {
                if (response.isSuccessful) {
                    responseControl.postValue(response.body())
                } else {
                    responseControl.postValue(null)
                }
            }

            override fun onFailure(call: Call<PasajerosReponse>, t: Throwable) {
                responseControl.postValue(null)
            }
        })
    }

    fun setChecinPax(pax: PasajerosQuitarCheckInRequest) {
        controlAPI.setChecinPax(
            JsonParser.parseString(
                Gson().toJson(pax).toString()
            ).asJsonObject
        ).enqueue(object : Callback<PasajerosReponse> {
            override fun onResponse(
                call: Call<PasajerosReponse>,
                response: Response<PasajerosReponse>
            ) {
                if (response.isSuccessful) {
                    responseCheckIn.postValue(response.body())
                } else {
                    responseCheckIn.postValue(null)
                }
            }

            override fun onFailure(call: Call<PasajerosReponse>, t: Throwable) {
                responseCheckIn.postValue(null)
            }
        })
    }

    fun InsertInfoOficial(datos: InsertInfoOficial) {

        var j = JsonParser.parseString(
            Gson().toJson(datos).toString()
        ).asJsonObject

        controlAPI.InsertInfoOficial(j).enqueue(object : Callback<resultInsertInfoOficial> {
            override fun onResponse(
                call: Call<resultInsertInfoOficial>,
                response: Response<resultInsertInfoOficial>
            ) {
                if (response.isSuccessful) {
                    resultInsertInfoOficial.postValue(response.body())
                } else {
                    resultInsertInfoOficial.postValue(null)
                }
            }

            override fun onFailure(call: Call<resultInsertInfoOficial>, t: Throwable) {
                resultInsertInfoOficial.postValue(null)
            }
        })
    }


}