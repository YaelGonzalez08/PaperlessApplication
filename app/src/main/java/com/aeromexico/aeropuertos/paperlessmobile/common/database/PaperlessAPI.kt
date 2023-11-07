package com.aeromexico.aeropuertos.paperlessmobile.common.database

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

class PaperlessAPI constructor(context: Context) {
    companion object{
        @Volatile
        private var INSTANCE: PaperlessAPI? =null

        fun getInstance(context: Context) = INSTANCE?: synchronized(this){
            INSTANCE?: PaperlessAPI(context).also { INSTANCE = it }
        }
    }

    val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(context.applicationContext)
    }

    fun <T> addToRequestQueue(req: Request<T>){
        requestQueue.add(req)
    }
}