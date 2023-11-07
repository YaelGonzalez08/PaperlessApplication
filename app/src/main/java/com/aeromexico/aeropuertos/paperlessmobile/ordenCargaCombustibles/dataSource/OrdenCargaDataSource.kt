package com.aeromexico.aeropuertos.paperlessmobile.ordenCargaCombustibles.dataSource

import androidx.lifecycle.MutableLiveData
import com.aeromexico.aeropuertos.paperlessmobile.ordenCargaCombustibles.entities.GuidFormCheck
import com.aeromexico.aeropuertos.paperlessmobile.ordenCargaCombustibles.entities.RequestOrdenCarga
import com.aeromexico.aeropuertos.paperlessmobile.ordenCargaCombustibles.entities.ResponseCheckOrdenCarga
import com.aeromexico.aeropuertos.paperlessmobile.webService.IAPI.OrdenCargaAPI
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.OrdeCargaResponse
import com.google.gson.Gson
import com.google.gson.JsonParser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrdenCargaDataSource(private val ordenCargaAPI: OrdenCargaAPI) {
    var ordeCargaResponse = MutableLiveData<OrdeCargaResponse>()
    var responseCheckOrdenCarga = MutableLiveData<ResponseCheckOrdenCarga>()

    fun checkOrdenCarga(guiVuelo:String) {

        ordenCargaAPI.checkOrdenCarga(
            JsonParser.parseString(
                Gson().toJson(
                    GuidFormCheck(guidVuelo= guiVuelo)
                ).toString()
            ).asJsonObject
        ).enqueue(object : Callback<ResponseCheckOrdenCarga> {
            override fun onResponse(
                call: Call<ResponseCheckOrdenCarga>,
                response: Response<ResponseCheckOrdenCarga>
            ) {
                if (response.isSuccessful)
                    responseCheckOrdenCarga.postValue(response.body())
                else
                    responseCheckOrdenCarga.postValue(null)
            }

            override fun onFailure(call: Call<ResponseCheckOrdenCarga>, t: Throwable) {
                responseCheckOrdenCarga.postValue(null)
            }

        })
    }


    fun insertOrdenCarga(requestOrdenCarga: RequestOrdenCarga) {
        ordenCargaAPI.sendOrdenCarga(
            JsonParser.parseString(
                Gson().toJson(
                    requestOrdenCarga
                ).toString()
            ).asJsonObject
        ).enqueue(object : Callback<OrdeCargaResponse> {
            override fun onResponse(
                call: Call<OrdeCargaResponse>,
                response: Response<OrdeCargaResponse>
            ) {
                if (response.isSuccessful)
                    ordeCargaResponse.postValue(response.body())
                else
                    ordeCargaResponse.postValue(null)
            }

            override fun onFailure(call: Call<OrdeCargaResponse>, t: Throwable) {
                ordeCargaResponse.postValue(null)
            }

        })
    }
}