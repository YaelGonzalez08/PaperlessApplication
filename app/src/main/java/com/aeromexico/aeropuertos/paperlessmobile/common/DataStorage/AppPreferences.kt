package com.aeromexico.aeropuertos.paperlessmobile.common.DataStorage

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import com.aeromexico.aeropuertos.paperlessmobile.home.MenuModule
import com.aeromexico.aeropuertos.paperlessmobile.webService.Usuario
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AppPreferences(context: Context) {
    private val appPreference = context.createDataStore(name = "app_preference")

    companion object{
        val RECENT_MODULE_KEY = preferencesKey<String>("RECENT_MODULE_KEY")

        //Gson().fromJson(existInBase!!.datos,Array<MenuModule>::class.java)
    }

    val getRecentModule: Flow<Array<MenuModule>> = appPreference.data.map {
        Gson().fromJson(it[RECENT_MODULE_KEY] ?: DataStorageLogin.NOT_DATA, Array<MenuModule>::class.java)
    }

    
    suspend fun saveRecentModule(module: MenuModule) {
        /*


        var list: Array<MenuModule>? =  getRecentModule
        list.add(module)
        list.removeLast()
        appPreference.edit {
            it[RECENT_MODULE_KEY] = Gson().toJson(list)
        }*/
    }
}