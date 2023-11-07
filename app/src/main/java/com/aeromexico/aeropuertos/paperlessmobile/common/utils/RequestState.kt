package com.aeromexico.aeropuertos.paperlessmobile.common.utils

import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class RequestState(
    val state: String
) {
    companion object {
        val REQ_IN_PROGRESS = "INPROGRESS"
        val REQ_BAD = "BAD"
        val REQ_OK = "OK"
    }

}
data class requestAverias(
    @SerializedName("esSalida") val esSalida : Boolean,
    @SerializedName("esLlegada") val esLlegada : Boolean)
