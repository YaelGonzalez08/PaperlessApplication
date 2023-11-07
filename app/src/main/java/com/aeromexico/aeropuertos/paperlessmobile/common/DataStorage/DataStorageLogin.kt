package com.aeromexico.aeropuertos.paperlessmobile.common.DataStorage

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import com.aeromexico.aeropuertos.paperlessmobile.webService.Result
import com.aeromexico.aeropuertos.paperlessmobile.webService.Usuario
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStorageLogin(context: Context) {
    private val dataLogin = context.createDataStore(name = "data_login")

    companion object {
        const val NOT_DATA = ""
        val USER_LOGIN_KEY = preferencesKey<String>("USER_LOGIN_KEY")
        val TOKEN_SESSION = preferencesKey<String>("TOKEN_SESSION")
        val LAST_RENOVATION = preferencesKey<String>("LAST_RENOVATION")
    }

    suspend fun guardarUserLogin(datosSession: Result,timeinMilis:String) {
        var user = Gson().toJson(datosSession.usuario).toString()

        dataLogin.edit {
            it[USER_LOGIN_KEY] = user
        }
        guardarToken(datosSession.token,timeinMilis)
    }

    suspend fun guardarToken(tokenS: String,timeinMilis:String) {
        dataLogin.edit {
            it[TOKEN_SESSION] = tokenS
        }
        saveLastRenovationTkenTime(timeinMilis)
    }
    suspend fun saveLastRenovationTkenTime(timeinMilis:String){
        dataLogin.edit {
            it[LAST_RENOVATION] = timeinMilis
        }
    }

    val getUserLogin: Flow<Usuario> = dataLogin.data.map {
        Gson().fromJson(it[USER_LOGIN_KEY] ?: NOT_DATA, Usuario::class.java)
    }

    val getTokenSession: Flow<String> = dataLogin.data.map {
        it[TOKEN_SESSION] ?: NOT_DATA
    }
    val getLastrenovationTime: Flow<String> = dataLogin.data.map {
        it[LAST_RENOVATION] ?: "0"
    }
}