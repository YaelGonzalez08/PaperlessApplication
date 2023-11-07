package com.aeromexico.aeropuertos.paperlessmobile.webService.Responses

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class PDFB64FILEResponse (

    @SerializedName("Status") val status : String,
    @SerializedName("Code") val code : Int,
    @SerializedName("ResponseCode") val responseCode : String,
    @SerializedName("Message") val message : String,
    @SerializedName("Error") val error : Boolean,
    @SerializedName("ErrorMessage") val errorMessage : String,
    @SerializedName("Result") val result : PDFB64FILEResult
): Parcelable


@Parcelize
data class PDFB64FILEResult (

    @SerializedName("ArchivoB64") val archivoB64 : String,
    @SerializedName("NombreArchivo") val nombreArchivo : String
): Parcelable
@Parcelize
data class PdfImageResponse (

    @SerializedName("Status") val status : String,
    @SerializedName("Code") val code : Int,
    @SerializedName("ResponseCode") val responseCode : String,
    @SerializedName("Message") val message : String,
    @SerializedName("Error") val error : Boolean,
    @SerializedName("ErrorMessage") val errorMessage : String,
    @SerializedName("Result") val result : ResultPDFImage
): Parcelable

@Parcelize
data class ResultPDFImage (

    @SerializedName("ArchivoB64") val archivoB64 : String,
    @SerializedName("NombreArchivo") val nombreArchivo : String
): Parcelable

