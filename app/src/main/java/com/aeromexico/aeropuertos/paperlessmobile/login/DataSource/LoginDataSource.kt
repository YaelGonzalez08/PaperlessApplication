package com.aeromexico.aeropuertos.paperlessmobile.login.DataSource

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.RequestState
import com.aeromexico.aeropuertos.paperlessmobile.login.pojos.DominiosResponse
import com.aeromexico.aeropuertos.paperlessmobile.webService.IAPI.UserApi
import com.aeromexico.aeropuertos.paperlessmobile.webService.LoginMainReponse
import com.aeromexico.aeropuertos.paperlessmobile.webService.TokenResponse
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import org.json.JSONObject
import com.google.gson.Gson

public class LoginDataSource(private val userApi: UserApi) {

    var responseState = MutableLiveData<RequestState>()
    var responseLogin = MutableLiveData<LoginMainReponse>()
    var responseToken = MutableLiveData<TokenResponse>()
    var responseDomians = MutableLiveData<DominiosResponse>()

    fun requestGetDomains() {
        responseState.value = RequestState(RequestState.REQ_IN_PROGRESS)

        userApi.getdomains().enqueue(object : Callback<DominiosResponse> {
            override fun onResponse(
                call: Call<DominiosResponse>,
                response: Response<DominiosResponse>
            ) {
                if (response.isSuccessful) {
                    responseState.value = RequestState(RequestState.REQ_OK)
                    Log.i("response domians", response.body().toString())
                    responseDomians.postValue(response.body())

                } else {
                    responseState.value = RequestState(RequestState.REQ_BAD)
                    responseDomians.postValue(null)
                }
            }

            override fun onFailure(p0: Call<DominiosResponse>, p1: Throwable) {
                responseState.value = RequestState(RequestState.REQ_BAD)
                responseDomians.postValue(null)

            }

        })

    }

    fun requestLogin(loginUser: String, passwordUser: String) {
        responseState.value = RequestState(RequestState.REQ_IN_PROGRESS)

        val jsonObj_ = JsonObject()
        jsonObj_.addProperty("Correo", loginUser)
        jsonObj_.addProperty("Password", passwordUser)
        userApi.loginRequest(jsonObj_).enqueue(object : Callback<LoginMainReponse> {
            override fun onFailure(call: Call<LoginMainReponse>, t: Throwable) {
                responseState.value = RequestState(RequestState.REQ_BAD)
                responseLogin.postValue(null)
                FirebaseCrashlytics.getInstance().recordException(t)
            }

            override fun onResponse(
                call: Call<LoginMainReponse>,
                response: Response<LoginMainReponse>
            ) {
                if (response.isSuccessful) {
                    responseState.value = RequestState(RequestState.REQ_OK)
                    Log.i("response login", response.body().toString())
                    responseLogin.postValue(response.body())

                } else {
                    responseState.value = RequestState(RequestState.REQ_BAD)

                    try {
                        val jObjError: JSONObject? = JSONObject(response.errorBody()!!.string())
                        Log.i("response login", jObjError.toString())
                        val gson = Gson()
                        responseLogin.postValue(
                            gson.fromJson(
                                jObjError.toString(),
                                LoginMainReponse::class.java
                            )
                        )
                    } catch (e: Exception) {
                        responseLogin.postValue(null)
                        FirebaseCrashlytics.getInstance().recordException(e)
                    }

                }

            }

        })

    }

    fun requesRenewToken() {
        responseState.value = RequestState(RequestState.REQ_IN_PROGRESS)

        userApi.renewToken().enqueue(object : Callback<TokenResponse> {
            override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
                responseState.value = RequestState(RequestState.REQ_BAD)
                FirebaseCrashlytics.getInstance().recordException(t)
            }

            override fun onResponse(
                call: Call<TokenResponse>,
                response: Response<TokenResponse>
            ) {
                if (response.isSuccessful) {
                    responseState.value = RequestState(RequestState.REQ_OK)
                    Log.i("response login", response.body().toString())
                    responseToken.postValue(response.body())

                } else {
                    responseState.value = RequestState(RequestState.REQ_BAD)
                    try {
                        val jObjError: JSONObject? = JSONObject(response.errorBody()!!.string())
                        Log.i("response login", jObjError.toString())
                        val gson = Gson()
                        responseToken.postValue(
                            gson.fromJson(
                                jObjError.toString(),
                                TokenResponse::class.java
                            )
                        )
                    } catch (e: Exception) {
                        responseToken.postValue(null)
                        FirebaseCrashlytics.getInstance().recordException(e)
                    }

                }

            }

        })

    }

}