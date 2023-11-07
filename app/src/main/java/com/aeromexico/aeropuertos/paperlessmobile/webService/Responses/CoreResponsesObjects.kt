package com.aeromexico.aeropuertos.paperlessmobile.webService.Responses

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


data class ResultCORE (

        @SerializedName("UsuarioCore") val usuarioCore : UsuarioCore
)
@Parcelize
data class UsuarioCore (

        @SerializedName("employeeNumber") var employeeNumber : Int?=0,
        @SerializedName("name") var name : String?="",
        @SerializedName("apellidoMaterno") var apellidoMaterno : String?="",
        @SerializedName("apellidoPaterno") var apellidoPaterno : String?="",
        @SerializedName("Puesto") var puesto: String? = ""
) : Parcelable
data class CORE_Base (

        @SerializedName("Status") val status : String,
        @SerializedName("Code") val code : Int,
        @SerializedName("ResponseCode") val responseCode : String,
        @SerializedName("Message") val message : String,
        @SerializedName("Error") val error : Boolean,
        @SerializedName("ErrorMessage") val errorMessage : String,
        @SerializedName("Result") val result : ResultCORE
)