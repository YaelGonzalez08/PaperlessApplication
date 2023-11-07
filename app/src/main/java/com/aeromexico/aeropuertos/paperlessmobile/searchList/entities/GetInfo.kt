package com.aeromexico.aeropuertos.paperlessmobile.searchList.entities


import com.google.gson.annotations.SerializedName

data class GetInfo(
    @SerializedName("GuidVuelo")
    var guidVuelo: String = ""
)