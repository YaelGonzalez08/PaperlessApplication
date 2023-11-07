package com.aeromexico.aeropuertos.paperlessmobile.common.utils

import android.app.Application
import android.content.Context

class AppContextSIngleton(val context: Context?) {

    companion object {
        var instance: Context? = null
            private set

        fun getInstance (context: Context?) : Context? {

            if (instance == null) {
                instance = context
            }

            return instance
        }
    }
}