package com.aeromexico.aeropuertos.paperlessmobile.common.DataStorage

import android.content.Context
import android.content.SharedPreferences
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import com.aeromexico.aeropuertos.paperlessmobile.home.MenuModule
import com.aeromexico.aeropuertos.paperlessmobile.home.MenuModuleRecientes
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class AppPreferences(context: Context) {
    private val appPreference = context.createDataStore(name = "app_preference")

    companion object {
        val RECENT_MODULE_KEY = preferencesKey<String>("RECENT_MODULE_KEY")
    }

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveModule(module: MenuModule) {
        var actualList = getList() as ArrayList<MenuModuleRecientes>
        var newRecent = MenuModuleRecientes(module.name,module.image)
        if (!actualList.contains(newRecent)){
            actualList.add(0,newRecent)
        }
        if (actualList.count() > 3) {
            saveList(actualList.take(3))
        } else {
            saveList(actualList)
        }

    }

    private fun saveList(list: List<MenuModuleRecientes>) {
        val editor = sharedPreferences.edit()
        val json = gson.toJson(list)
        editor.putString(RECENT_MODULE_KEY.toString(), json)
        editor.apply()
    }

    fun getList(): List<MenuModuleRecientes> {
        val json = sharedPreferences.getString(RECENT_MODULE_KEY.toString(), "")
        return if (!json.isNullOrEmpty()) {
            val type = object : TypeToken<List<MenuModuleRecientes>>() {}.type
            gson.fromJson(json, type)
        } else {
            arrayListOf()
        }
    }
}