package com.aeromexico.aeropuertos.paperlessmobile.webService.Responses

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CheckListResponseObjects (

    @SerializedName("Status") val status : String?,
    @SerializedName("Code") val code : Int?,
    @SerializedName("ResponseCode") val responseCode : String?,
    @SerializedName("Message") val message : String?,
    @SerializedName("Error") val error : Boolean?,
    @SerializedName("ErrorMessage") val errorMessage : String?,
    @SerializedName("Result") val result : ResultCheckList?
): Parcelable

@Parcelize
data class ResultCheckList (
    @SerializedName("Folio") val folio : Int
): Parcelable