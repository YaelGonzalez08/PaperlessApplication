package com.aeromexico.aeropuertos.paperlessmobile.webService.Responses

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.util.*

data class ResponseBase (

        @SerializedName("Status") val status : String,
        @SerializedName("Code") val code : Int,
        @SerializedName("ResponseCode") val responseCode : String,
        @SerializedName("Message") val message : String,
        @SerializedName("Error") val error : Boolean,
        @SerializedName("ErrorMessage") val errorMessage : String,
        @SerializedName("Result") val result : Result
)
data class Result (

        @SerializedName("Vuelos") val vuelos : List<Vuelos>
)

@Parcelize
data class Vuelos (
        @SerializedName("Origen") val origen : String,
        @SerializedName("Destino") val destino : String,
        @SerializedName("NumVuelo") val numVuelo : Int,
        @SerializedName("FechaVueloLocal") val fechaVueloLocal : String,
        @SerializedName("Aerolinea") val aerolinea : String,
        @SerializedName("ETD") val eTD : String,
        @SerializedName("ETA") val eTA : String,
        @SerializedName("Matricula") val matricula : String,
        @SerializedName("Equipo") val equipo : String,
        @SerializedName("FlightReferenceNumber") val flightReferenceNumber : Long,
        @SerializedName("Guid") val guid : String,
        @SerializedName("TipoCabina") val tipoCabina : String,
        @SerializedName("CompaniaEquipo") val companiaEquipo: String,
        @SerializedName("CapacidadGasolina") val capacidadGasolina: Double,
        @SerializedName("TanqueIzq") val tanqueIzquierdo:Double,
        @SerializedName("TanqueDer") val tanqueDerecho:Double,
        @SerializedName("TanqueCent") val tanqueCentral:Double
        ) : Parcelable

