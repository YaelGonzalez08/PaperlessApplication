package com.aeromexico.aeropuertos.paperlessmobile.webService.Responses


import android.os.Parcelable
import com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronavePrimerVuelo.entities.RequestFirstFlightForm
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PrimerVueloResponse(
    @SerializedName("Code")
    var code: Int = 0,
    @SerializedName("Error")
    var error: Boolean = false,
    @SerializedName("ErrorMessage")
    var errorMessage: String = String(),
    @SerializedName("Message")
    var message: String = "",
    @SerializedName("ResponseCode")
    var responseCode: String = "",
    @SerializedName("Result")
    var result: ResultForm = ResultForm(),
    @SerializedName("Status")
    var status: String = ""
) : Parcelable

@Parcelize
data class ResultForm(
    @SerializedName("PrimerVuelo")
    var primerVuelo: PrimerVuelo = PrimerVuelo()
) : Parcelable

@Parcelize
data class PrimerVuelo(
    @SerializedName("idPrimerVuelo")
    var idPrimerVuelo: Int = 0
) : Parcelable


data class PrimerVueloResponseGET(
    @SerializedName("Status") val status : String,
    @SerializedName("Code") val code: Int,
    @SerializedName("ResponseCode") val responseCode : String,
    @SerializedName("Message") val message : String,
    @SerializedName("Error") val error: Boolean,
    @SerializedName("ErrorMessage") val errorMessage: String,
    @SerializedName("Result") val result: ResultGETPrimerVuelo,

)

data class ResultGETPrimerVuelo (

    @SerializedName("Item3") val item3 : RequestFirstFlightForm,
    @SerializedName("tieneInfo") val tieneInfo : Boolean
)