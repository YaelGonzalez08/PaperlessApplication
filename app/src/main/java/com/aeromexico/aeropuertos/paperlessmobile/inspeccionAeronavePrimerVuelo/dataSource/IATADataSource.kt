package com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronavePrimerVuelo.dataSource

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.RequestState
import com.aeromexico.aeropuertos.paperlessmobile.webService.IAPI.IATAApi
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.IATASResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class IATADataSource(private val iataApi: IATAApi) {
    var responseState = MutableLiveData<RequestState>()
    var responseIATA = MutableLiveData<IATASResponse>()

    fun requestIATAS(){
        responseState.value = RequestState(RequestState.REQ_IN_PROGRESS)

        iataApi.getIatas().enqueue(object : Callback<IATASResponse> {
            override fun onResponse(call: Call<IATASResponse>, response: Response<IATASResponse>) {
                if (response.isSuccessful) {
                    responseState.value = RequestState(RequestState.REQ_OK)
                    Log.i("response login", response.body().toString())
                    responseIATA.postValue(response.body())
                } else
                    responseState.value = RequestState(RequestState.REQ_BAD)
            }

            override fun onFailure(call: Call<IATASResponse>, t: Throwable) {
                responseState.value = RequestState(RequestState.REQ_BAD)
            }

        })
    }
}