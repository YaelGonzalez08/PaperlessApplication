package com.aeromexico.aeropuertos.paperlessmobile.webService

import android.util.Log
import com.aeromexico.aeropuertos.paperlessmobile.BuildConfig
import com.aeromexico.aeropuertos.paperlessmobile.PaperlessApplication
import com.aeromexico.aeropuertos.paperlessmobile.webService.IAPI.*
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class WebServiceApi {
    private var retrofit: Retrofit? = null

    fun getComuncadisApi(): ComunicadosAPI {
        return getClient()!!.create(ComunicadosAPI::class.java)
    }

    fun getControlAbordajeApi(): ControlAbordajeAPI {
        return getClient()!!.create(ControlAbordajeAPI::class.java)
    }

    fun getOrdenCargaApi(): OrdenCargaAPI {
        return getClient()!!.create(OrdenCargaAPI::class.java)
    }

    fun getPrimerVueloDiaApi(): PrimerVueloDiaApi {
        return getClient()!!.create(PrimerVueloDiaApi::class.java)
    }

    fun getUserApi(): UserApi {
        return getClient()!!.create(
            UserApi::class.java
        )
    }

    fun getVuelosApi(): VueloAPI {
        return getClient()!!.create(
            VueloAPI::class.java
        )
    }

    fun getMOApi(): MensajesOperacionalesAPI {
        return getClient()!!.create(
            MensajesOperacionalesAPI::class.java
        )
    }

    fun getInspecAeronaveApi(): InspeccionAeronaveAPI {
        return getClient()!!.create(
            InspeccionAeronaveAPI::class.java
        )
    }

    fun getCoreApi(): CoreApi {
        return getClient()!!.create(
            CoreApi::class.java
        )
    }

    fun getCheckListApi(): CheckListAPI {
        return getClient()!!.create(
            CheckListAPI::class.java
        )
    }

    fun getSearchListApi(): SearchListApi {
        return getClient()!!.create(
            SearchListApi::class.java
        )
    }

    fun getInfoSearchList(): GetInfoApi {
        return getClient()!!.create(
            GetInfoApi::class.java
        )
    }

    fun getNotocApi(): NotocApi {
        return getClient()!!.create(
            NotocApi::class.java
        )
    }

    fun getManifiestoCargaAPI(): ManifiestoCargaAPI {
        return getClient()!!.create(
            ManifiestoCargaAPI::class.java
        )
    }

    fun getClient(): Retrofit? {
        var token = PaperlessApplication.tokenSession
        Log.e("token", "$token")
        val gson: Gson = GsonBuilder()
            .setLenient() // Permite JSON mal formado
            .create()

        if (retrofit == null) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY

            val client = OkHttpClient.Builder()
                .writeTimeout(180, TimeUnit.SECONDS)
                .readTimeout(180, TimeUnit.SECONDS)
                .connectTimeout(180, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .callTimeout(180, TimeUnit.SECONDS)
                .addInterceptor(interceptor).addInterceptor { chain ->
                    if (!token.isNullOrEmpty()) {
                        val newRequest: Request = chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer $token")
                            .build()
                        chain.proceed(newRequest)
                    } else {
                        val newRequest: Request = chain.request().newBuilder()
                            .build()
                        chain.proceed(newRequest)
                    }

                }.build()

            retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.PAPERLESS_URL_API)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        }
        return retrofit
    }

    fun getIATAApi(): IATAApi {
        return getClient()!!.create(
            IATAApi::class.java
        )
    }

    fun getOptBdmApi(): OptBdmAPI {
        return getClient()!!.create(
            OptBdmAPI::class.java
        )
    }

}
